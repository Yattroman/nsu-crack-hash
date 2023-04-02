package ru.nsu.ccfit.model;

import lombok.Builder;

import java.util.List;

@Builder
public record HashCrackingTask(
        String requestId,
        int partNumber,
        int partCount,
        String hash,
        int maxLength,
        List<String> stringAlphabetSymbols) {
}
