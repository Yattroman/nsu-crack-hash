package ru.nsu.ccfit.service.specific;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.nsu.ccfit.model.HashCrackingTask;
import ru.nsu.ccfit.schema.crack_hash_request.CrackHashManagerRequest;
import ru.nsu.ccfit.port.out.rabbitmq.IManagerCommunicationService;

@Component
@RequiredArgsConstructor
public class WorkerFlowService implements IWorkerFlowService {

    private final ICrackHashService crackHashService;
    private final IManagerCommunicationService managerCommunicationService;

    @Override
    public void crackHash(CrackHashManagerRequest request) {
        var taskResult = crackHashService.crackHash(prepareTaskFromRequest(request));
        managerCommunicationService.sendHashCrackingTaskResultToManager(taskResult);
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
