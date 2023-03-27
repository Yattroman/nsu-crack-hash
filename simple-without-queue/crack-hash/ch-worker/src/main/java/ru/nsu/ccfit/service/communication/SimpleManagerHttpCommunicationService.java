package ru.nsu.ccfit.service.communication;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.nsu.ccfit.model.HashCrackingResult;
import ru.nsu.ccfit.schema.crack_hash_response.CrackHashWorkerResponse;

@Component
public class SimpleManagerHttpCommunicationService implements IManagerCommunicationService {

    private final RestTemplate restTemplate;

    @Value("${app.communication.manager.address}")
    private String managerAddress;

    public SimpleManagerHttpCommunicationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void sendHashCrackingTaskResultToManager(HashCrackingResult task) {
        var workerResponse = prepareWorkerResponse(task);
        var url = managerAddress + "/internal/api/manager/hash/crack/request";
        restTemplate.patchForObject(url, workerResponse, CrackHashWorkerResponse.class);
    }

    private CrackHashWorkerResponse prepareWorkerResponse(HashCrackingResult result) {
        var response = new CrackHashWorkerResponse();

        var answers = new CrackHashWorkerResponse.Answers();
        answers.getWords().addAll(result.answer());
        response.setAnswers(answers);

        response.setRequestId(result.requestId());
        response.setPartNumber(result.partNumber());

        return response;
    }

}
