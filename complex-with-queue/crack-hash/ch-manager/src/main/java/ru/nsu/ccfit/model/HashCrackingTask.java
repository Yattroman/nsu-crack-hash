package ru.nsu.ccfit.model;

import lombok.Builder;

@Builder
public record HashCrackingTask(String taskId, String hash, int maxLength, int partNumber, int partCount) {
    public static final int TIMEOUT_SECONDS = 2 * 60;

}
