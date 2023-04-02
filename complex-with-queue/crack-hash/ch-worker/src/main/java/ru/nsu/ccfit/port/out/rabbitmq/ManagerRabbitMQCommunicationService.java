package ru.nsu.ccfit.port.out.rabbitmq;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.nsu.ccfit.model.CrackHashResult;
import ru.nsu.ccfit.service.specific.ICrackHashResultConverter;

@Slf4j
@Component
@RequiredArgsConstructor
public class ManagerRabbitMQCommunicationService implements IManagerCommunicationService {

    private final ICrackHashResultConverter crackHashResultConverter;
    private final AmqpTemplate rabbitTemplate;
    @Value("${app.communication.rabbitmq-config.crack-hash-process.task-result-exchange}")
    private String taskResultGettingExchange;
    @Value("${app.communication.rabbitmq-config.crack-hash-process.task-result-routing}")
    private String taskResultGettingRouting;

    @Override
    public void sendHashCrackingTaskResultToManager(CrackHashResult crackHashResult) {
        try {
            var workerResponse = crackHashResultConverter.prepareCrackHashWorkerResponse(crackHashResult);
            log.info("Sending task for manager: answers {}, part number {}",
                    workerResponse.getAnswers().getWords(), workerResponse.getPartNumber());
            rabbitTemplate.convertAndSend(
                    taskResultGettingExchange,
                    taskResultGettingRouting,
                    workerResponse,
                    message -> {
                        // it is also by default
                        message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                        return message;
                    }
            );
        } catch (AmqpException ae) {
            log.error("Part number: {} - wasn't send due to {}", crackHashResult.partNumber(), ae.getMessage());
        }
    }

}
