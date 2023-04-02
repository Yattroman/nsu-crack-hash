package ru.nsu.ccfit.port.out.rabbitmq;

import ru.nsu.ccfit.model.CrackHashResult;

public interface IManagerCommunicationService {
    void sendHashCrackingTaskResultToManager(CrackHashResult task);
}
