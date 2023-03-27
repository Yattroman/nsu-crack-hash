package ru.nsu.ccfit.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.nsu.ccfit.schema.crack_hash_response.CrackHashWorkerResponse;
import ru.nsu.ccfit.service.specific.ICrackHashInteractionService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/api/manager/hash")
public class CrackHashInternalController {

    private final ICrackHashInteractionService crackHashInteractionService;

    @PatchMapping("/crack/request")
    public void addHashCrackingAnswers(@RequestBody CrackHashWorkerResponse workerResponse){
        crackHashInteractionService.addHashCrackingAnswersWithWorkerResponse(workerResponse);
    }

}
