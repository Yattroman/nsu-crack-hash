package ru.nsu.ccfit.task;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.nsu.ccfit.port.dto.crack_hash.status.PublicCrackStatus;
import ru.nsu.ccfit.model.exception.ServiceException;
import ru.nsu.ccfit.model.HashCrackingTask;
import ru.nsu.ccfit.service.specific.ICrackHashInteractionService;

import java.time.Instant;

@Slf4j
@AllArgsConstructor
public class TimeoutStatusCheckTask implements Runnable {

    private final ICrackHashInteractionService crackHashInteractionService;
    private final String requestId;

    @Override
    public void run() {
        try {
            var taskStatus = crackHashInteractionService.getHashCrackingProgressingStatus(requestId);
            var taskStartTime = taskStatus.getStartTime();
            var currentStatus = taskStatus.getPublicStatus();
            var currentTime = Instant.now();
            if (currentTime.isAfter(taskStartTime.plusSeconds(HashCrackingTask.TIMEOUT_SECONDS)) &&
                    currentStatus.equals(PublicCrackStatus.IN_PROGRESS)) {
                crackHashInteractionService.changeHashCrackingPublicStatus(requestId, PublicCrackStatus.ERROR);
            }
        } catch (ServiceException e) {
            log.error("Something went wrong while changing status: {}", e.getMessage());
        }
    }
}
