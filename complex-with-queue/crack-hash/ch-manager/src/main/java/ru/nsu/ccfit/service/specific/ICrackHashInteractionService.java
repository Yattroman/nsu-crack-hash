package ru.nsu.ccfit.service.specific;

import ru.nsu.ccfit.port.dto.crack_hash.status.PublicCrackStatus;
import ru.nsu.ccfit.model.exception.ServiceException;
import ru.nsu.ccfit.repository.entity.CrackHashTaskProcessingStatusData;

import java.util.List;

public interface ICrackHashInteractionService {

    void startObserveHashCrackingTask(String requestId, String hash, int maxLength, int workersCount);
    CrackHashTaskProcessingStatusData getHashCrackingProgressingStatus(String requestId) throws ServiceException;
    List<CrackHashTaskProcessingStatusData> getNotHandledCompletelyHashCrackingTasks() throws ServiceException;
    void addHashCrackingAnswersWithWorkerResponse(String requestId, List<String> answerWords) throws ServiceException;
    void changeHashCrackingPublicStatus(String requestId, PublicCrackStatus status) throws ServiceException;
    void setHashCrackingPartAsNotHandled(String requestId, Integer partNumber) throws ServiceException;
    void setHashCrackingPartAsHandled(String requestId, Integer partNumber) throws ServiceException;
}
