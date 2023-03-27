package ru.nsu.ccfit.service.communication;

import ru.nsu.ccfit.model.HashCrackingResult;
import ru.nsu.ccfit.schema.crack_hash_response.CrackHashWorkerResponse;

public interface IManagerCommunicationService {
    void sendHashCrackingTaskResultToManager(HashCrackingResult task);
}
