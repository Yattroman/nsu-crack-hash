package ru.nsu.ccfit.service.specific;

import org.apache.commons.math3.util.CombinatoricsUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.nsu.ccfit.dto.crack_hash.init.CrackHashInitializationRequest;
import ru.nsu.ccfit.dto.crack_hash.init.CrackHashInitializationResponse;
import ru.nsu.ccfit.dto.crack_hash.status.CrackHashStatusResponse;
import ru.nsu.ccfit.service.communication.IWorkerCommunicationService;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

@Component
public class ManagerFlowService implements IManagerFlowService {

    private final ICrackHashInteractionService crackHashInteractionService;
    private final IWorkerCommunicationService workerCommunicationService;
    private final ICrackHashTaskConverter crackHashTaskConverter;
    private int workersCount = 1;
    @Value("${app.alphabet}")
    private String alphabetString;

    public ManagerFlowService(ICrackHashInteractionService crackHashInteractionService,
                              IWorkerCommunicationService workerCommunicationService,
                              ICrackHashTaskConverter crackHashTaskConverter) {
        this.crackHashInteractionService = crackHashInteractionService;
        this.workerCommunicationService = workerCommunicationService;
        this.crackHashTaskConverter = crackHashTaskConverter;
    }

    @Override
    public CrackHashInitializationResponse distributeTask(CrackHashInitializationRequest request) {
        List<CompletableFuture<String>> allFuturesResults = new ArrayList<>();

        var taskVolume = calculateTaskVolume(request.maxLength(), alphabetString.length());
        var taskId = UUID.randomUUID().toString();

        for (int i = 0; i < workersCount; i++) {
            int addition = i == 0 ? taskVolume % workersCount : 0;
            int partCount = taskVolume / workersCount + addition;
            allFuturesResults.add(
                    startObserveHashCrackingTask(taskId, request.hash(), request.maxLength(), i, partCount)
            );
        }

        var requestIds = allFuturesResults.stream()
                .map(CompletableFuture::join)
                .filter(Objects::nonNull)
                .toList();

        var requestId = requestIds.size() == workersCount ? taskId : null;

        return crackHashTaskConverter.prepareCrackHashInitializationResponse(requestId);
    }

    private int calculateTaskVolume(int maxLength, int alphabetLength) {
        return IntStream.rangeClosed(1, maxLength)
                .map(length -> calculatePlacementsNumberWithRepetitions(length, alphabetLength))
                .sum();
    }

    private int calculatePlacementsNumberWithRepetitions(int k, int n) {
        return Math.toIntExact(Math.round(Math.pow(n, k)));
    }

    @Override
    public CrackHashStatusResponse getHashCrackingStatus(String requestId) {
        return crackHashTaskConverter.prepareHashCrackingStatusResponse(
                crackHashInteractionService.getHashCrackingStatus(requestId)
        );
    }

    private CompletableFuture<String> startObserveHashCrackingTask(String taskId, String hash, int maxLength,
                                                                   int partNumber, int partCount) {
        return workerCommunicationService.sendHashCrackingTaskToWorker(
                        crackHashTaskConverter.prepareHashCrackingTask(taskId, hash, maxLength, partNumber, partCount)
                )
                .thenApply(id ->
                        crackHashInteractionService.startObserveHashCrackingTask(taskId, workersCount)
                );
    }

}
