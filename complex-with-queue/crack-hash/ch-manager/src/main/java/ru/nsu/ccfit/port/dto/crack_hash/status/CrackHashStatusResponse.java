package ru.nsu.ccfit.port.dto.crack_hash.status;

import java.util.List;

public record CrackHashStatusResponse(PublicCrackStatus status, List<String> data) {}
