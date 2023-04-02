package ru.nsu.ccfit.service.specific;

import ru.nsu.ccfit.model.HashCrackingTask;
import ru.nsu.ccfit.model.CrackHashResult;

public interface ICrackHashService {

    CrackHashResult crackHash(HashCrackingTask task);

}
