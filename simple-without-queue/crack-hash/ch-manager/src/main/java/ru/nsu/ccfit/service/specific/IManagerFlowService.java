package ru.nsu.ccfit.service.specific;

import ru.nsu.ccfit.dto.crack_hash.init.CrackHashInitializationRequest;
import ru.nsu.ccfit.dto.crack_hash.init.CrackHashInitializationResponse;
import ru.nsu.ccfit.dto.crack_hash.status.CrackHashStatusResponse;
import ru.nsu.ccfit.schema.crack_hash_request.CrackHashManagerRequest;

public interface IManagerFlowService {

    CrackHashInitializationResponse distributeTask(CrackHashInitializationRequest request);
    CrackHashStatusResponse getHashCrackingStatus(String requestId);

}
