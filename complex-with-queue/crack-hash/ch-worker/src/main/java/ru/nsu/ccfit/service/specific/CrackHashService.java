package ru.nsu.ccfit.service.specific;

import io.micrometer.common.util.StringUtils;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;
import ru.nsu.ccfit.model.HashCrackingTask;
import ru.nsu.ccfit.model.CrackHashResult;
import ru.nsu.ccfit.utils.WordsGenerator;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Component
@NoArgsConstructor
public class CrackHashService implements ICrackHashService {

    @Override
    public CrackHashResult crackHash(HashCrackingTask task) {
        return CrackHashResult.builder()
                .requestId(task.requestId())
                .partNumber(task.partNumber())
                .answer(findAnswers(task))
                .build();
    }

    private List<String> findAnswers(HashCrackingTask task) {
        var isLastPart = (task.partCount() - 1) == task.partNumber();

        var generalVolume = calculateTaskVolume(task.maxLength(), task.stringAlphabetSymbols().size());
        var additionalVolume = isLastPart ? generalVolume % task.partCount() : 0;
        var fundamentalVolume = generalVolume / task.partCount();

        var startIndexInclusive = task.partNumber() * fundamentalVolume + 1;
        var endIndexExclusive = (task.partNumber() + 1) * fundamentalVolume + additionalVolume + 1;
        var characterAlphabetSymbols = stringAlphabetToCharacterAlphabet(task.stringAlphabetSymbols());
        var wordsSubset = WordsGenerator.generateWordsSubsetsUsingAlphabetWithRepetitionSlow(
                characterAlphabetSymbols, startIndexInclusive, endIndexExclusive
        );

        log.info("General volume: {}; Part number: {}; Total parts: {}; Is last?: {}",
                generalVolume, task.partNumber(), task.partCount(), isLastPart);

        log.info("Start index inclusive: {}; End index exclusive: {}; Volume: {}",
                startIndexInclusive, endIndexExclusive, fundamentalVolume + additionalVolume);

        return wordsSubset.stream()
                .filter(word -> task.hash().equals(DigestUtils.md5Hex(word)))
                .collect(Collectors.toList());
    }

    private int calculateTaskVolume(int maxLength, int alphabetLength) {
        return IntStream.rangeClosed(1, maxLength)
                .map(length -> calculateEmplacementsNumberWithRepetitions(length, alphabetLength))
                .sum();
    }

    private int calculateEmplacementsNumberWithRepetitions(int k, int n) {
        return Math.toIntExact(Math.round(Math.pow(n, k)));
    }

    private List<Character> stringAlphabetToCharacterAlphabet(List<String> alphabet) {
        return alphabet.stream()
                .filter(StringUtils::isNotBlank)
                .map(symbol -> symbol.charAt(0))
                .toList();
    }

}
