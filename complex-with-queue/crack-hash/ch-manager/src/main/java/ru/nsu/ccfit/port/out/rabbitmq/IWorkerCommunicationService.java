package ru.nsu.ccfit.port.out.rabbitmq;

import ru.nsu.ccfit.model.HashCrackingTask;
import ru.nsu.ccfit.model.exception.ServiceException;

import java.util.List;

public interface IWorkerCommunicationService {
    void sendHashCrackingTaskPartToWorkers(List<HashCrackingTask> tasks) throws ServiceException;
}
