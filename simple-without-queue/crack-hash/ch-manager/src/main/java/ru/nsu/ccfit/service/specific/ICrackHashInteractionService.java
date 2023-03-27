package ru.nsu.ccfit.service.specific;

import ru.nsu.ccfit.dto.crack_hash.status.CrackStatus;
import ru.nsu.ccfit.model.ProcessingStatusData;
import ru.nsu.ccfit.schema.crack_hash_response.CrackHashWorkerResponse;

public interface ICrackHashInteractionService {
    String startObserveHashCrackingTask(String requestId, int workersCount);
    ProcessingStatusData getHashCrackingStatus(String requestId);
    void addHashCrackingAnswersWithWorkerResponse(CrackHashWorkerResponse workerResponse);

    void changeHashCrackingStatus(String requestId, CrackStatus status);
}
