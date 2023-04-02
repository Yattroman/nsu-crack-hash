package ru.nsu.ccfit.port.in.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nsu.ccfit.port.dto.crack_hash.init.CrackHashInitializationRequest;
import ru.nsu.ccfit.port.dto.crack_hash.init.CrackHashInitializationResponse;
import ru.nsu.ccfit.port.dto.crack_hash.status.CrackHashStatusResponse;
import ru.nsu.ccfit.model.exception.ServiceException;
import ru.nsu.ccfit.service.specific.IManagerFlowService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hash")
public class CrackHashOrderController {

    private final IManagerFlowService managerFlowService;

    @ExceptionHandler(ServiceException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleServiceException(
            ServiceException exception
    ) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exception.getMessage());
    }

    @PostMapping("/crack")
    public CrackHashInitializationResponse initializeHashCracking(@RequestBody CrackHashInitializationRequest request)
            throws ServiceException {
        return managerFlowService.distributeTask(request);
    }

    @GetMapping("/status/{requestId}")
    public CrackHashStatusResponse getHashCrackingStatus(@PathVariable String requestId) throws ServiceException {
        return managerFlowService.getHashCrackingStatus(requestId);
    }

}
