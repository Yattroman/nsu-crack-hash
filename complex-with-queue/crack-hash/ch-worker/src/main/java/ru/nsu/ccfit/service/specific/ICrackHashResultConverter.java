package ru.nsu.ccfit.service.specific;

import ru.nsu.ccfit.model.CrackHashResult;
import ru.nsu.ccfit.schema.crack_hash_response.CrackHashWorkerResponse;

import java.util.List;

public interface ICrackHashResultConverter {
    CrackHashWorkerResponse prepareCrackHashWorkerResponse(CrackHashResult crackHashResult);

}
