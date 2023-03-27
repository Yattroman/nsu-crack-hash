package ru.nsu.ccfit.service.communication;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.nsu.ccfit.model.HashCrackingTask;
import ru.nsu.ccfit.schema.crack_hash_request.CrackHashManagerRequest;

import java.util.concurrent.CompletableFuture;

@Component
public class SimpleWorkerHttpCommunicationService implements IWorkerCommunicationService {

    private final RestTemplate restTemplate;

    @Value("${app.communication.worker.address}")
    private String workerAddress;

    @Value("${app.alphabet}")
    private String alphabetString;

    public SimpleWorkerHttpCommunicationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Async
    public CompletableFuture<String> sendHashCrackingTaskToWorker(HashCrackingTask task) {
        var request = prepareWorkerRequest(task);
        var workerCrackTaskApiUrl = workerAddress + "/internal/api/worker/hash/crack/task";
        var response = restTemplate.postForEntity(workerCrackTaskApiUrl, request, CrackHashManagerRequest.class);

        return response.getStatusCode().is2xxSuccessful() ?
                CompletableFuture.completedFuture(request.getRequestId()) :
                CompletableFuture.completedFuture(null);
    }

    private CrackHashManagerRequest prepareWorkerRequest(HashCrackingTask task) {
        var request = new CrackHashManagerRequest();

        request.setRequestId(task.taskId());
        request.setPartNumber(task.partNumber());
        request.setPartCount(task.partCount());
        request.setHash(task.hash());
        request.setMaxLength(task.maxLength());
        request.setAlphabet(prepareAlphabet());

        return request;
    }

    private CrackHashManagerRequest.Alphabet prepareAlphabet() {
        var alphabet = new CrackHashManagerRequest.Alphabet();
        var symbols = alphabet.getSymbols();

        alphabetString
                .chars()
                .mapToObj(i -> (char) i)
                .map(character -> Character.toString(character))
                .forEach(symbols::add);

        return alphabet;
    }

}
