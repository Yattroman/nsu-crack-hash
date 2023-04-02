package ru.nsu.ccfit.service.specific;

import ru.nsu.ccfit.port.dto.crack_hash.init.CrackHashInitializationRequest;
import ru.nsu.ccfit.port.dto.crack_hash.init.CrackHashInitializationResponse;
import ru.nsu.ccfit.port.dto.crack_hash.status.CrackHashStatusResponse;
import ru.nsu.ccfit.model.exception.ServiceException;

public interface IManagerFlowService {

    CrackHashInitializationResponse distributeTask(CrackHashInitializationRequest request) throws ServiceException;
    CrackHashStatusResponse getHashCrackingStatus(String requestId) throws ServiceException;

}
