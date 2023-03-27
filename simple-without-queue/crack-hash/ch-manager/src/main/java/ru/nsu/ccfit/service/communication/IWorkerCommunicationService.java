package ru.nsu.ccfit.service.communication;

import ru.nsu.ccfit.model.HashCrackingTask;

import java.util.concurrent.CompletableFuture;

public interface IWorkerCommunicationService {
    CompletableFuture<String> sendHashCrackingTaskToWorker(HashCrackingTask task);
}
