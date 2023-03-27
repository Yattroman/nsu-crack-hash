package ru.nsu.ccfit.service.specific;

import org.springframework.stereotype.Component;
import ru.nsu.ccfit.dto.crack_hash.init.CrackHashInitializationResponse;
import ru.nsu.ccfit.dto.crack_hash.status.CrackHashStatusResponse;
import ru.nsu.ccfit.model.HashCrackingTask;
import ru.nsu.ccfit.model.ProcessingStatusData;


@Component
public class CrackingHashCrackHashTaskConverter implements ICrackHashTaskConverter {

    @Override
    public HashCrackingTask prepareHashCrackingTask(String requestId, String hash, int maxLength,
                                                    int partNumber, int partCount) {
        return HashCrackingTask.builder()
                .taskId(requestId)
                .partNumber(partNumber)
                .partCount(partCount)
                .hash(hash)
                .maxLength(maxLength)
                .build();
    }

    @Override
    public CrackHashStatusResponse prepareHashCrackingStatusResponse(ProcessingStatusData statusData) {
        return new CrackHashStatusResponse(statusData.getStatus().get(), statusData.getAnswers().stream().toList());
    }

    @Override
    public CrackHashInitializationResponse prepareCrackHashInitializationResponse(String requestId) {
        return new CrackHashInitializationResponse(requestId);
    }

}
