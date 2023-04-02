package ru.nsu.ccfit.port.in.rabbitmq.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import ru.nsu.ccfit.model.exception.ServiceException;
import ru.nsu.ccfit.schema.crack_hash_response.CrackHashWorkerResponse;
import ru.nsu.ccfit.service.specific.ICrackHashInteractionService;

@Slf4j
@Component
@RequiredArgsConstructor
public class TaskResultGettingListener {

    private final ICrackHashInteractionService crackHashInteractionService;

    @RabbitListener(
            queues = "${app.communication.rabbitmq-config.crack-hash-process.task-result-queue}",
            ackMode = "AUTO"
    )
    public void handleMessage(CrackHashWorkerResponse workerResponse) throws ServiceException {
        validateMessage(workerResponse);
        log.info("Got message with answers for {} by worker which had worked with part number: {}",
                workerResponse.getRequestId(), workerResponse.getPartNumber());

        crackHashInteractionService.addHashCrackingAnswersWithWorkerResponse(
                workerResponse.getRequestId(), workerResponse.getAnswers().getWords()
        );
    }

    private void validateMessage(CrackHashWorkerResponse workerResponse) throws ServiceException {
        if (workerResponse.getRequestId() == null)
            throw new ServiceException("Worker response with empty request id!");
    }

}
