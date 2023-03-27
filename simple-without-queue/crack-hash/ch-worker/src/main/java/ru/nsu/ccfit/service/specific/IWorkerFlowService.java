package ru.nsu.ccfit.service.specific;

import ru.nsu.ccfit.schema.crack_hash_request.CrackHashManagerRequest;

public interface IWorkerFlowService {

    void crackHash(CrackHashManagerRequest request);

}
