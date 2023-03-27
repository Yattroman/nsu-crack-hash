package ru.nsu.ccfit.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.nsu.ccfit.dto.crack_hash.init.CrackHashInitializationRequest;
import ru.nsu.ccfit.dto.crack_hash.init.CrackHashInitializationResponse;
import ru.nsu.ccfit.dto.crack_hash.status.CrackHashStatusResponse;
import ru.nsu.ccfit.service.specific.IManagerFlowService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hash")
public class CrackHashOrderController {

    private final IManagerFlowService managerFlowService;

    @PostMapping("/crack")
    public CrackHashInitializationResponse initializeHashCracking(@RequestBody CrackHashInitializationRequest request){
        return managerFlowService.distributeTask(request);
    }

    @GetMapping("/status/{requestId}")
    public CrackHashStatusResponse getHashCrackingStatus(@PathVariable String requestId){
        return managerFlowService.getHashCrackingStatus(requestId);
    }

}
