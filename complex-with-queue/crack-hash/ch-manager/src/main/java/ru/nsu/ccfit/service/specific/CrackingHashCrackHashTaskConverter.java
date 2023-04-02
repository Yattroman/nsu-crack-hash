package ru.nsu.ccfit.service.specific;

import org.springframework.stereotype.Component;
import ru.nsu.ccfit.port.dto.crack_hash.init.CrackHashInitializationResponse;
import ru.nsu.ccfit.port.dto.crack_hash.status.CrackHashStatusResponse;
import ru.nsu.ccfit.repository.entity.CrackHashTaskProcessingStatusData;
import ru.nsu.ccfit.model.HashCrackingTask;

import java.util.List;
import java.util.Set;


@Component
public class CrackingHashCrackHashTaskConverter implements ICrackHashTaskConverter {

    @Override
    public List<HashCrackingTask> decomposeHashCrackingTask(
            String requestId, String hash, int maxLength, Set<Integer> partsIndices, int partCount
    ) {
        return partsIndices.stream()
                .map(i -> prepareHashCrackingTask(requestId, hash, maxLength, i, partCount))
                .toList();
    }

    private HashCrackingTask prepareHashCrackingTask(
            String requestId, String hash, int maxLength, int partNumber, int partCount
    ){
        return HashCrackingTask.builder()
                .taskId(requestId)
                .partNumber(partNumber)
                .partCount(partCount)
                .hash(hash)
                .maxLength(maxLength)
                .build();
    }

    @Override
    public CrackHashStatusResponse prepareHashCrackingStatusResponse(CrackHashTaskProcessingStatusData statusData) {
        return new CrackHashStatusResponse(statusData.getPublicStatus(), statusData.getAnswers().stream().toList());
    }

    @Override
    public CrackHashInitializationResponse prepareCrackHashInitializationResponse(String requestId) {
        return new CrackHashInitializationResponse(requestId);
    }

}
