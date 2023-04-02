package ru.nsu.ccfit.service.specific;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.ccfit.model.HashCrackingTask;
import ru.nsu.ccfit.model.exception.ServiceException;
import ru.nsu.ccfit.port.dto.crack_hash.status.PublicCrackStatus;
import ru.nsu.ccfit.repository.CrackHashProgressRepository;
import ru.nsu.ccfit.repository.entity.CrackHashTaskProcessingStatusData;
import ru.nsu.ccfit.task.TimeoutStatusCheckTask;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class CrackHashInteractionService implements ICrackHashInteractionService {

    private final CrackHashProgressRepository crackHashProgressRepository;
    private final ThreadPoolTaskScheduler threadPoolTaskScheduler;

    @Override
    public void startObserveHashCrackingTask(String requestId, String hash, int maxLength, int workersCount) {
        if (requestId == null) {
            return;
        }

        if (crackHashProgressRepository.existsByRequestId(requestId)) {
            return;
        }

        crackHashProgressRepository.insert(
                CrackHashTaskProcessingStatusData
                        .createStartProcessingStatusData(requestId, workersCount, hash, maxLength, Instant.now())
        );

        threadPoolTaskScheduler.schedule(
                new TimeoutStatusCheckTask(this, requestId),
                Instant.now().plusSeconds(HashCrackingTask.TIMEOUT_SECONDS)
        );
    }

    @Override
    public CrackHashTaskProcessingStatusData getHashCrackingProgressingStatus(String requestId)
            throws ServiceException {
        return Optional.ofNullable(requestId)
                .flatMap(crackHashProgressRepository::findByRequestId)
                .orElseThrow(() -> new ServiceException("No crack hash processing status data with such request id"));
    }

    @Override
    @Transactional
    public List<CrackHashTaskProcessingStatusData> getNotHandledCompletelyHashCrackingTasks() {
        return crackHashProgressRepository.findAll().stream()
                .filter(data -> PublicCrackStatus.IN_PROGRESS.equals(data.getPublicStatus()))
                .filter(data -> !data.getNotHandledParts().isEmpty())
                .toList();
    }

    @Override
    @Transactional
    public void addHashCrackingAnswersWithWorkerResponse(String requestId, List<String> answerWords)
            throws ServiceException {
        var processingStatusData = Optional.ofNullable(requestId)
                .flatMap(crackHashProgressRepository::findByRequestId)
                .orElseThrow(() -> new ServiceException("No crack hash processing status data with such request id"));

        if (PublicCrackStatus.ERROR.equals(processingStatusData.getPublicStatus())) {
            log.info("Too late to add answer! Timeout exceeded.");
            return;
        }

        processingStatusData.getAnswers().addAll(answerWords);

        processingStatusData.setCurrentWorkingWorkersNumber(
                processingStatusData.getCurrentWorkingWorkersNumber() - 1
        );

        if (processingStatusData.getCurrentWorkingWorkersNumber() == 0) {
            log.info("All workers sent results!");
            processingStatusData.setPublicStatus(PublicCrackStatus.READY);
        }

        crackHashProgressRepository.save(processingStatusData);

        log.info("Adding answer: {}", answerWords);
    }

    @Override
    @Transactional
    public void changeHashCrackingPublicStatus(String requestId, PublicCrackStatus status) throws ServiceException {
        var processingStatusData = Optional.ofNullable(requestId)
                .flatMap(crackHashProgressRepository::findByRequestId)
                .orElseThrow(() -> new ServiceException("No crack hash processing status data with such request id"));
        processingStatusData.setPublicStatus(status);
        crackHashProgressRepository.save(processingStatusData);
    }

    @Override
    @Transactional
    public void setHashCrackingPartAsNotHandled(String requestId, Integer partNumber) throws ServiceException {
        var processingStatusData = Optional.ofNullable(requestId)
                .flatMap(crackHashProgressRepository::findByRequestId)
                .orElseThrow(() -> new ServiceException("No crack hash processing status data with such request id"));

        if (processingStatusData.getNotHandledParts().contains(partNumber)) {
            return;
        }

        processingStatusData.getNotHandledParts().add(partNumber);
        crackHashProgressRepository.save(processingStatusData);
    }

    @Override
    @Transactional
    public void setHashCrackingPartAsHandled(String requestId, Integer partNumber) throws ServiceException {
        var processingStatusData = Optional.ofNullable(requestId)
                .flatMap(crackHashProgressRepository::findByRequestId)
                .orElseThrow(() -> new ServiceException("No crack hash processing status data with such request id"));

        if (!processingStatusData.getNotHandledParts().contains(partNumber)) {
            return;
        }

        processingStatusData.getNotHandledParts().remove(partNumber);
        crackHashProgressRepository.save(processingStatusData);
    }
}
