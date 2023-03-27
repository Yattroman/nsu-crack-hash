package ru.nsu.ccfit.service.specific;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import ru.nsu.ccfit.model.HashCrackingTask;
import ru.nsu.ccfit.schema.crack_hash_request.CrackHashManagerRequest;
import ru.nsu.ccfit.service.communication.IManagerCommunicationService;

@Component
@RequiredArgsConstructor
public class WorkerFlowService implements IWorkerFlowService {

    private final IManagerCommunicationService managerCommunicationService;
    private final ICrackHashService crackHashService;

    @Override
    public void crackHash(CrackHashManagerRequest request) {
        crackHashService.crackHash(prepareTaskFromRequest(request))
                .thenAccept(managerCommunicationService::sendHashCrackingTaskResultToManager);
    }

    private HashCrackingTask prepareTaskFromRequest(CrackHashManagerRequest request) {
        return HashCrackingTask.builder()
                .requestId(request.getRequestId())
                .maxLength(request.getMaxLength())
                .partCount(request.getPartCount())
                .partNumber(request.getPartNumber())
                .hash(request.getHash())
                .stringAlphabetSymbols(request.getAlphabet().getSymbols())
                .build();
    }

}
