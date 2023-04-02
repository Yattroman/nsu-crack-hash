package ru.nsu.ccfit.task;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.nsu.ccfit.model.exception.ServiceException;
import ru.nsu.ccfit.port.out.rabbitmq.IWorkerCommunicationService;
import ru.nsu.ccfit.repository.entity.CrackHashTaskProcessingStatusData;
import ru.nsu.ccfit.service.specific.ICrackHashInteractionService;
import ru.nsu.ccfit.service.specific.ICrackHashTaskConverter;

@Slf4j
@AllArgsConstructor
public class MessageInQueueCheckTask implements Runnable {

    private final ICrackHashInteractionService crackHashInteractionService;
    private final IWorkerCommunicationService workerCommunicationService;
    private final ICrackHashTaskConverter crackHashTaskConverter;

    @Override
    public void run() {
        try {
            var notHandledTasks = crackHashInteractionService.getNotHandledCompletelyHashCrackingTasks();
            for (CrackHashTaskProcessingStatusData notHandledTask : notHandledTasks) {
                handleNotHandled(notHandledTask);
            }
        } catch (ServiceException e) {
            log.error("Something went wrong while sending not sent: {}", e.getMessage());
        }
    }

    private void handleNotHandled(CrackHashTaskProcessingStatusData crackHashTaskStatusData) throws ServiceException {
        var decomposedTasks = crackHashTaskConverter.decomposeHashCrackingTask(
                crackHashTaskStatusData.getRequestId(),
                crackHashTaskStatusData.getHash(),
                crackHashTaskStatusData.getMaxLength(),
                crackHashTaskStatusData.getNotHandledParts(),
                crackHashTaskStatusData.getDecomposedPartsNumber()
        );

        log.info("Send not handled task part of {}: {}",
                crackHashTaskStatusData.getRequestId(),
                crackHashTaskStatusData.getNotHandledParts()
        );

        workerCommunicationService.sendHashCrackingTaskPartToWorkers(decomposedTasks);
    }

}
