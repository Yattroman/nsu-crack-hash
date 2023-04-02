package ru.nsu.ccfit.service.specific;

import ru.nsu.ccfit.port.dto.crack_hash.init.CrackHashInitializationResponse;
import ru.nsu.ccfit.port.dto.crack_hash.status.CrackHashStatusResponse;
import ru.nsu.ccfit.repository.entity.CrackHashTaskProcessingStatusData;
import ru.nsu.ccfit.model.HashCrackingTask;

import java.util.List;
import java.util.Set;

public interface ICrackHashTaskConverter {

    List<HashCrackingTask> decomposeHashCrackingTask(
            String requestId, String hash, int maxLength, Set<Integer> partsIndices, int partCount
    );
    CrackHashStatusResponse prepareHashCrackingStatusResponse(CrackHashTaskProcessingStatusData statusData);
    CrackHashInitializationResponse prepareCrackHashInitializationResponse(String requestId);

}
