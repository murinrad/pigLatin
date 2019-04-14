package murinrad;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import murinrad.Main.CasePreserver.MidWordPunctuationPreserver;

public class Main {

  private static final Set<Character> VOWELS = new HashSet<>(Arrays.asList(new Character[]{'a', 'e', 'i', 'o', 'u', 'y'}));

  private static final Set<Character> WORD_SEPARATOR = new HashSet<>
      (Arrays.asList(new Character[]{' ', ',', '.', '-', '"', '―', '?', '!', ':', ';',
          '\t', '\n', '\r', '<', '>', '='}));

  public static void main(String[] args) {
    if (args != null && args.length == 1) {
      System.out.println(new WordTokenizerApplicator().apply(args[0]));
    } else {
      throw new IllegalArgumentException("Please provide text to parse as a parameter. Example java - jar \"Hello\"");
    }
  }


  private static abstract class StringLogicApplicator {

    String apply(String word) {
      if (word == null || "".equals(word)) {
        return "";
      }
      return applyLogic(word);
    };

    protected abstract String applyLogic(String word);

  }

  /**
   * Takes text, and applies the pig latin logic filters. Entry point for the app
   */
  public static class WordTokenizerApplicator extends StringLogicApplicator {

    public String apply(String word) {
      return super.apply(word);
    }

    @Override
    protected String applyLogic(String text) {
      StringBuilder sb = new StringBuilder();
      int firstPos = 0;
      int endPos;
      for (endPos = 0; endPos < text.length(); endPos++) {
        if (WORD_SEPARATOR.contains(text.charAt(endPos))) {
          sb.append(new MidWordPunctuationPreserver().apply(text.substring(firstPos, endPos)));
          sb.append(text.charAt(endPos));
          firstPos = endPos + 1;
        }
        if (endPos == text.length() - 1) {
          sb.append(new MidWordPunctuationPreserver().apply(text.substring(firstPos, endPos + 1)));
        }
      }
      return sb.toString();
    }
  }

  /**
   * Applies the pig latin logic
   */
  public static class PigLatinApplicator extends StringLogicApplicator {

    private final String VOWEL_ENDING = "way";
    private final String CONSONANT_ENDING = "ay";


    @Override
    protected String applyLogic(String word) {
      if (word.endsWith(VOWEL_ENDING)) {
        return word;
      }
      if (VOWELS.contains(word.charAt(0))) {
        return word + VOWEL_ENDING;
      } else {
        return word.substring(1) + word.charAt(0) + CONSONANT_ENDING;
      }

    }
  }

  /**
   * Makes sure the case is preserved on the same location
   */
  public static class CasePreserver extends StringLogicApplicator {

    @Override
    protected String applyLogic(String word) {
      List<Integer> upperCaseLocations = new ArrayList<>();
      for (int i = 0; i < word.length(); i++) {
        if (Character.isUpperCase(word.charAt(i))) {
          upperCaseLocations.add(i);
        }
      }
      char[] piginatedWord = new PigLatinApplicator().apply(word.toLowerCase()).toCharArray();
      for (Integer upperCaseLocation : upperCaseLocations) {
        piginatedWord[upperCaseLocation] = Character.toUpperCase(piginatedWord[upperCaseLocation]);
      }
      return new String(piginatedWord);
    }

    /**
     * Makes sure punctuation which does not split words remains at the same position relative to
     * the end of the word
     */
    public static class MidWordPunctuationPreserver extends StringLogicApplicator {

      private static final Set<Character> MID_WORD_PUNCTUATION = new HashSet<>(Arrays.asList(new Character[]{'\'', '’'}));

      @Override
      protected String applyLogic(String word) {

        List<SimpleEntry<Integer, Character>> punctiationList = new ArrayList<>();

        StringBuilder cleanedWord = new StringBuilder();

        for (int i = 0; i < word.length(); i++) {
          char letter = word.charAt(i);
          if (MID_WORD_PUNCTUATION.contains(letter)) {
            punctiationList.add(new SimpleEntry<>(word.length() - 1 - i, letter));
          } else {
            cleanedWord.append(letter);
          }
        }
        StringBuffer pigginatedAndCasedWord = new StringBuffer(new CasePreserver().apply(cleanedWord.toString()));
        int pigginatedLength = pigginatedAndCasedWord.length();
        for (SimpleEntry<Integer, Character> entry : punctiationList) {
          pigginatedAndCasedWord.insert(pigginatedLength - entry.getKey(), entry.getValue());
        }
        return pigginatedAndCasedWord.toString();
      }
    }

  }
}
