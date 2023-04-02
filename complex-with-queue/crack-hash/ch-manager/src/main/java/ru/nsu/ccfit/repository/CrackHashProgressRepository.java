package ru.nsu.ccfit.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.nsu.ccfit.repository.entity.CrackHashTaskProcessingStatusData;

import java.util.Optional;

@Repository
public interface CrackHashProgressRepository extends MongoRepository<CrackHashTaskProcessingStatusData, Long> {

    boolean existsByRequestId(String requestId);
    Optional<CrackHashTaskProcessingStatusData> findByRequestId(String requestId);

}
