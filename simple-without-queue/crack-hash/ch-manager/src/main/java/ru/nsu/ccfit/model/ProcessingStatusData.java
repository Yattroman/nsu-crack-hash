package ru.nsu.ccfit.model;

import lombok.Getter;
import lombok.Setter;
import ru.nsu.ccfit.dto.crack_hash.status.CrackStatus;

import java.time.Instant;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Getter
@Setter
public class ProcessingStatusData {

    public static final int TIMEOUT_SECONDS = 2 * 60;
    private AtomicReference<CrackStatus> status;
    private AtomicInteger currentWorkingWorkers;
    private ConcurrentLinkedQueue<String> answers;
    private AtomicReference<Instant> startTime;

    public ProcessingStatusData(CrackStatus status, int workersCount) {
        this.status = new AtomicReference<>(status);
        this.currentWorkingWorkers = new AtomicInteger(workersCount);
        this.startTime = new AtomicReference<>(Instant.now());
        this.answers = new ConcurrentLinkedQueue<>();
    }

    public static ProcessingStatusData createStartProcessingStatusData(int workersCount) {
        return new ProcessingStatusData(CrackStatus.IN_PROGRESS, workersCount);
    }

}
