package ru.nsu.ccfit.task;

import lombok.AllArgsConstructor;
import ru.nsu.ccfit.dto.crack_hash.status.CrackStatus;
import ru.nsu.ccfit.model.ProcessingStatusData;
import ru.nsu.ccfit.service.specific.ICrackHashInteractionService;

import java.time.Instant;

@AllArgsConstructor
public class TimeoutStatusCheckTask implements Runnable {

    private final ICrackHashInteractionService crackHashInteractionService;
    private final String requestId;

    @Override
    public void run() {
        var taskStatus = crackHashInteractionService.getHashCrackingStatus(requestId);
        var taskStartTime = taskStatus.getStartTime().get();
        var currentTime = Instant.now();
        var currentStatus = crackHashInteractionService.getHashCrackingStatus(requestId).getStatus().get();
        if (currentTime.isAfter(taskStartTime.plusSeconds(ProcessingStatusData.TIMEOUT_SECONDS)) &&
                currentStatus.equals(CrackStatus.IN_PROGRESS)) {
            crackHashInteractionService.changeHashCrackingStatus(requestId, CrackStatus.ERROR);
        }
    }
}
