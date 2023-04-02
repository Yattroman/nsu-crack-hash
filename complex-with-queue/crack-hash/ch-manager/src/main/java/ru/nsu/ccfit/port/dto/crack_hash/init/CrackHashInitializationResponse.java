package ru.nsu.ccfit.port.dto.crack_hash.init;

public record CrackHashInitializationResponse(String requestId) {
    public CrackHashInitializationResponse(String requestId) {
        this.requestId = requestId;
    }
}
