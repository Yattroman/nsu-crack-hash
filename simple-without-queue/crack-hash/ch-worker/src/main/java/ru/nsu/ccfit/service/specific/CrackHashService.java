package ru.nsu.ccfit.service.specific;

import io.micrometer.common.util.StringUtils;
import lombok.NoArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import ru.nsu.ccfit.model.HashCrackingTask;
import ru.nsu.ccfit.model.HashCrackingResult;
import ru.nsu.ccfit.utils.WordsGenerator;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Component
@NoArgsConstructor
public class CrackHashService implements ICrackHashService {

    @Async
    @Override
    public CompletableFuture<HashCrackingResult> crackHash(HashCrackingTask task) {
        return CompletableFuture.completedFuture(HashCrackingResult.builder()
                .requestId(task.requestId())
                .partNumber(task.partNumber())
                .answer(findAnswers(task))
                .build());
    }

    private List<String> findAnswers(HashCrackingTask task) {
        var startIndexInclusive = task.partNumber() * task.partCount() + 1;
        var endIndexExclusive = (task.partNumber() + 1) * task.partCount() + 1;
        var characterAlphabetSymbols = stringAlphabetToCharacterAlphabet(task.stringAlphabetSymbols());
        var wordsSubset = WordsGenerator.generateWordsSubsetsUsingAlphabetWithRepetitionSlow(
                characterAlphabetSymbols, startIndexInclusive, endIndexExclusive
        );

        return wordsSubset.stream()
                .filter(word -> task.hash().equals(DigestUtils.md5Hex(word)))
                .collect(Collectors.toList());
    }

    private List<Character> stringAlphabetToCharacterAlphabet(List<String> alphabet) {
        return alphabet.stream()
                .filter(StringUtils::isNotBlank)
                .map(symbol -> symbol.charAt(0))
                .toList();
    }

    private static String flattenSymbolsList(List<String> symbols) {
        return String.join("", symbols);
    }

}
