package ru.nsu.ccfit.service.specific;

import ru.nsu.ccfit.dto.crack_hash.init.CrackHashInitializationResponse;
import ru.nsu.ccfit.dto.crack_hash.status.CrackHashStatusResponse;
import ru.nsu.ccfit.model.HashCrackingTask;
import ru.nsu.ccfit.model.ProcessingStatusData;

public interface ICrackHashTaskConverter {

    HashCrackingTask prepareHashCrackingTask(String requestId, String hash, int maxLength,
                                             int partNumber, int partCount);

    CrackHashStatusResponse prepareHashCrackingStatusResponse(ProcessingStatusData statusData);

    CrackHashInitializationResponse prepareCrackHashInitializationResponse(String requestId);
}
