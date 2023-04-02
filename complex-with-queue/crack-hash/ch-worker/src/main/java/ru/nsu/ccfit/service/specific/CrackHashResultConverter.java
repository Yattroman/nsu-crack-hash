package ru.nsu.ccfit.service.specific;

import org.springframework.stereotype.Component;
import ru.nsu.ccfit.model.CrackHashResult;
import ru.nsu.ccfit.schema.crack_hash_response.CrackHashWorkerResponse;

import java.util.List;

@Component
public class CrackHashResultConverter implements ICrackHashResultConverter{

    @Override
    public CrackHashWorkerResponse prepareCrackHashWorkerResponse(CrackHashResult crackHashResult) {
        var response = new CrackHashWorkerResponse();

        var answers = new CrackHashWorkerResponse.Answers();
        answers.getWords().addAll(crackHashResult.answer());
        response.setAnswers(answers);

        response.setRequestId(crackHashResult.requestId());
        response.setPartNumber(crackHashResult.partNumber());

        return response;
    }
}
