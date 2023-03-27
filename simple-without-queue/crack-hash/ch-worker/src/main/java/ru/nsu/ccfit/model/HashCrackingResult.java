package ru.nsu.ccfit.model;

import lombok.Builder;

import java.util.List;

@Builder
public record HashCrackingResult(
        String requestId,
        int partNumber,
        List<String> answer) {
}
