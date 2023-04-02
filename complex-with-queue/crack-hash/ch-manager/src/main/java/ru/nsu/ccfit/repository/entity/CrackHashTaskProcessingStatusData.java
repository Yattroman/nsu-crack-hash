package ru.nsu.ccfit.repository.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;
import ru.nsu.ccfit.port.dto.crack_hash.status.PublicCrackStatus;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Document("progressing_status_data")
public class CrackHashTaskProcessingStatusData {

    @MongoId
    private String requestId;
    private PublicCrackStatus publicStatus;
    private String hash;
    private Integer maxLength;
    private Integer decomposedPartsNumber;
    private Integer currentWorkingWorkersNumber;
    private List<String> answers;
    private Set<Integer> notHandledParts;
    private Instant startTime;

    public CrackHashTaskProcessingStatusData(String requestId, PublicCrackStatus publicStatus,
                                             String hash, Integer maxLength, Integer decomposedPartsNumber,
                                             Integer currentWorkingWorkersNumber, Instant startTime) {
        this.requestId = requestId;
        this.publicStatus = publicStatus;
        this.currentWorkingWorkersNumber = currentWorkingWorkersNumber;
        this.hash = hash;
        this.maxLength = maxLength;
        this.decomposedPartsNumber = decomposedPartsNumber;
        this.startTime = startTime;
        this.answers = new ArrayList<>();
        this.notHandledParts = new HashSet<>();
    }

    public static CrackHashTaskProcessingStatusData createStartProcessingStatusData(String requestId,
                                                                                    int workersCount,
                                                                                    String hash,
                                                                                    int maxLength,
                                                                                    Instant startTime) {
        return new CrackHashTaskProcessingStatusData(
                requestId, PublicCrackStatus.IN_PROGRESS, hash, maxLength,  workersCount, workersCount, startTime
        );
    }

}
