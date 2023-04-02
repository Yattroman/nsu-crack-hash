package ru.nsu.ccfit.port.in.rabbimq.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import ru.nsu.ccfit.model.exception.ServiceException;
import ru.nsu.ccfit.port.out.rabbitmq.IManagerCommunicationService;
import ru.nsu.ccfit.schema.crack_hash_request.CrackHashManagerRequest;
import ru.nsu.ccfit.service.specific.ICrackHashService;
import ru.nsu.ccfit.service.specific.IWorkerFlowService;

@Slf4j
@Component
@RequiredArgsConstructor
public class TaskGettingListener {

    private final IWorkerFlowService workerFlowService;

    @RabbitListener(queues = "${app.communication.rabbitmq-config.crack-hash-process.task-queue}", ackMode = "AUTO")
    public void handleMessage(CrackHashManagerRequest managerRequest) throws ServiceException {
        validateMessage(managerRequest);

        log.info("Got message for getting answers for {} | part number: {}, hash: {}, part count: {}",
                managerRequest.getRequestId(), managerRequest.getPartNumber(),
                managerRequest.getHash(), managerRequest.getPartCount()
        );

        workerFlowService.crackHash(managerRequest);
    }

    private void validateMessage(CrackHashManagerRequest managerRequest) throws ServiceException {
        if (managerRequest.getRequestId() == null)
            throw new ServiceException("Manager request with empty request id!");
    }

}
