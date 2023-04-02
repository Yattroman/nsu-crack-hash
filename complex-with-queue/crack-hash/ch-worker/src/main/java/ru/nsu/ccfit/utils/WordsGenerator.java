package ru.nsu.ccfit.utils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class WordsGenerator {

    public static List<String> generateWordsSubsetsUsingAlphabetWithRepetitionSlow(List<Character> alphabet,
                                                                                   int subsetStartIdxInclusive,
                                                                                   int subsetEndIdxExclusive) {
        return IntStream.range(subsetStartIdxInclusive, subsetEndIdxExclusive)
                .mapToObj(i -> getWordByIndex(alphabet, i))
                .collect(Collectors.toList());
    }

    private static String getWordByIndex(List<Character> alphabet, int n) {
        StringBuilder sb = new StringBuilder();

        while (n > 0){
            n--;
            sb.append(alphabet.get(n % alphabet.size()));
            n /= alphabet.size();
        }

        return sb.toString();
    }

}
