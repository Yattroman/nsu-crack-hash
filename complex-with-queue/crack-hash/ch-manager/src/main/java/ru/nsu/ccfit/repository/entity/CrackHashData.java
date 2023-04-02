package ru.nsu.ccfit.repository.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Getter
@Setter
@Document("crack_hash_data")
public class CrackHashData {

    @MongoId
    private String requestId;
    private String hash;
    private int partCount;
    private int maxLength;

}
