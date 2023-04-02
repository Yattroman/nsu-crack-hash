package ru.nsu.ccfit.service.specific;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.nsu.ccfit.model.exception.ServiceException;
import ru.nsu.ccfit.port.dto.crack_hash.init.CrackHashInitializationRequest;
import ru.nsu.ccfit.port.dto.crack_hash.init.CrackHashInitializationResponse;
import ru.nsu.ccfit.port.dto.crack_hash.status.CrackHashStatusResponse;
import ru.nsu.ccfit.port.out.rabbitmq.IWorkerCommunicationService;

import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
public class ManagerFlowService implements IManagerFlowService {

    private final ICrackHashInteractionService crackHashInteractionService;
    private final IWorkerCommunicationService workerCommunicationService;
    private final ICrackHashTaskConverter crackHashTaskConverter;

    @Value("${app.workers-number}")
    private int workersCount;

    @Override
    public CrackHashInitializationResponse distributeTask(CrackHashInitializationRequest request) {
        var taskId = UUID.randomUUID().toString();

        try {

            crackHashInteractionService
                    .startObserveHashCrackingTask(taskId, request.hash(), request.maxLength(), workersCount);

            var partsIndices = IntStream.range(0, workersCount).boxed().collect(Collectors.toSet());
            var decomposedTasks = crackHashTaskConverter
                    .decomposeHashCrackingTask(taskId, request.hash(), request.maxLength(), partsIndices, workersCount);

            workerCommunicationService
                    .sendHashCrackingTaskPartToWorkers(decomposedTasks);
        } catch (ServiceException e) {
            return crackHashTaskConverter.prepareCrackHashInitializationResponse(null);
        }

        return crackHashTaskConverter.prepareCrackHashInitializationResponse(taskId);
    }

    @Override
    public CrackHashStatusResponse getHashCrackingStatus(String requestId) throws ServiceException {
        return crackHashTaskConverter.prepareHashCrackingStatusResponse(
                crackHashInteractionService.getHashCrackingProgressingStatus(requestId)
        );
    }

}
