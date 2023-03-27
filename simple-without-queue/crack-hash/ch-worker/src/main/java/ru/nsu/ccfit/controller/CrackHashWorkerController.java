package ru.nsu.ccfit.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.nsu.ccfit.schema.crack_hash_request.CrackHashManagerRequest;
import ru.nsu.ccfit.service.specific.IWorkerFlowService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/api/worker/hash/crack")
public class CrackHashWorkerController {

    private final IWorkerFlowService workerFlowService;

    @PostMapping("/task")
    public void initializeHashCracking(@RequestBody CrackHashManagerRequest request){
        workerFlowService.crackHash(request);
    }

}
