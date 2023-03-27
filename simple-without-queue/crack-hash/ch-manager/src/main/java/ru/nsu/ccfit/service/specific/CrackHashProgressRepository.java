package ru.nsu.ccfit.service.specific;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;
import ru.nsu.ccfit.dto.crack_hash.status.CrackStatus;
import ru.nsu.ccfit.model.ProcessingStatusData;
import ru.nsu.ccfit.schema.crack_hash_response.CrackHashWorkerResponse;
import ru.nsu.ccfit.task.TimeoutStatusCheckTask;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@AllArgsConstructor
public class CrackHashProgressRepository implements ICrackHashInteractionService {

    private final Map<String, ProcessingStatusData> processingRequestWorkerCoherence = new ConcurrentHashMap<>();
    private final ThreadPoolTaskScheduler threadPoolTaskScheduler;

    @Override
    public String startObserveHashCrackingTask(String requestId, int workersCount) {
        if (requestId == null) {
            return null;
        }

        if (processingRequestWorkerCoherence.containsKey(requestId)) {
            return requestId;
        }

        processingRequestWorkerCoherence
                .put(requestId, ProcessingStatusData.createStartProcessingStatusData(workersCount));

        threadPoolTaskScheduler.schedule(
                new TimeoutStatusCheckTask(this, requestId),
                Instant.now().plusSeconds(ProcessingStatusData.TIMEOUT_SECONDS)
        );

        return requestId;
    }

    @Override
    public ProcessingStatusData getHashCrackingStatus(String requestId) {
        if (requestId == null) {
            return null;
        }
        return processingRequestWorkerCoherence.get(requestId);
    }

    @Override
    public void addHashCrackingAnswersWithWorkerResponse(CrackHashWorkerResponse workerResponse) {
        var answerWords = workerResponse.getAnswers().getWords();
        var data = processingRequestWorkerCoherence.get(workerResponse.getRequestId());
        var workersCount = data.getCurrentWorkingWorkers();
        data.getAnswers().addAll(answerWords);

        if (workersCount.decrementAndGet() == 0) {
            log.info("All workers sent results!");
            data.getStatus().set(CrackStatus.READY);
        }

        log.info("Adding answer: {}", answerWords);
    }

    @Override
    public void changeHashCrackingStatus(String requestId, CrackStatus status) {
        processingRequestWorkerCoherence.get(requestId).getStatus().set(status);
    }

}
