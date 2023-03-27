package ru.nsu.ccfit.model;

import lombok.Builder;

@Builder
public record HashCrackingTask(String taskId, String hash, int maxLength, int partNumber, int partCount) {
}
