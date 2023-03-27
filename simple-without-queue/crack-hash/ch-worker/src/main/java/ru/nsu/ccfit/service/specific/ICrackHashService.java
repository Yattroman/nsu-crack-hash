package ru.nsu.ccfit.service.specific;

import ru.nsu.ccfit.model.HashCrackingTask;
import ru.nsu.ccfit.model.HashCrackingResult;

import java.util.concurrent.CompletableFuture;

public interface ICrackHashService {

    CompletableFuture<HashCrackingResult> crackHash(HashCrackingTask task);

}
