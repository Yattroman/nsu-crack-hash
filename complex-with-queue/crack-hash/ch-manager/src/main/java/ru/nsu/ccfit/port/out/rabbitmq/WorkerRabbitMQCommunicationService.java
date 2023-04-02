package ru.nsu.ccfit.port.out.rabbitmq;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.nsu.ccfit.model.HashCrackingTask;
import ru.nsu.ccfit.model.exception.ServiceException;
import ru.nsu.ccfit.schema.crack_hash_request.CrackHashManagerRequest;
import ru.nsu.ccfit.service.specific.ICrackHashInteractionService;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class WorkerRabbitMQCommunicationService implements IWorkerCommunicationService {

    private final AmqpTemplate rabbitTemplate;
    private final ICrackHashInteractionService crackHashInteractionService;
    @Value("${app.alphabet}")
    private String alphabetString;
    @Value("${app.communication.rabbitmq-config.crack-hash-process.task-exchange}")
    private String taskGettingExchange;
    @Value("${app.communication.rabbitmq-config.crack-hash-process.task-routing}")
    private String taskGettingRouting;

    public void sendHashCrackingTaskPartToWorkers(List<HashCrackingTask> tasks) throws ServiceException {
        for (HashCrackingTask task : tasks) {
            sendHashCrackingTaskPartToWorker(task);
        }
    }

    private void sendHashCrackingTaskPartToWorker(HashCrackingTask task) throws ServiceException {
        var request = prepareWorkerRequest(task);
        log.info("Sending task for worker: hash {}, part number {}, part count: {}, max length: {}",
                request.getHash(), request.getPartNumber(), request.getPartCount(), request.getMaxLength());
        try {
            rabbitTemplate.convertAndSend(
                    taskGettingExchange,
                    taskGettingRouting,
                    request,
                    message -> {
                        // it is also by default
                        message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                        return message;
                    }
            );
            crackHashInteractionService.setHashCrackingPartAsHandled(
                    request.getRequestId(), request.getPartNumber()
            );
        } catch (AmqpException ae) {
            log.error("Part number: {} - wasn't send due to {}", request.getPartNumber(), ae.getMessage());
            crackHashInteractionService.setHashCrackingPartAsNotHandled(
                    request.getRequestId(), request.getPartNumber()
            );
        }
    }

    private CrackHashManagerRequest prepareWorkerRequest(HashCrackingTask task) {
        var request = new CrackHashManagerRequest();

        request.setRequestId(task.taskId());
        request.setPartNumber(task.partNumber());
        request.setPartCount(task.partCount());
        request.setHash(task.hash());
        request.setMaxLength(task.maxLength());
        request.setAlphabet(prepareAlphabet());

        return request;
    }

    private CrackHashManagerRequest.Alphabet prepareAlphabet() {
        var alphabet = new CrackHashManagerRequest.Alphabet();
        var symbols = alphabet.getSymbols();

        alphabetString
                .chars()
                .mapToObj(i -> (char) i)
                .map(character -> Character.toString(character))
                .forEach(symbols::add);

        return alphabet;
    }

}
