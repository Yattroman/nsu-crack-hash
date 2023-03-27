package ru.nsu.ccfit.dto.crack_hash.status;

import java.util.List;

public record CrackHashStatusResponse(CrackStatus status, List<String> data) {}
