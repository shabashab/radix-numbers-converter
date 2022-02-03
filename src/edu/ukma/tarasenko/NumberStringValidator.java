package edu.ukma.tarasenko;

import java.util.Locale;

public class NumberStringValidator {
  private final RadixCharactersCollection _collection;

  NumberStringValidator(RadixCharactersCollection collection) {
    _collection = collection;
  }

  public String validateString(String input) {
    char[] inputChars = input.toLowerCase(Locale.ROOT).toCharArray();
    char[] chars = _collection.getCharactersArray();

    for (char inputChar : inputChars) {
      if (inputChar == ',' || inputChar == '.') {
        continue;
      }

      boolean foundChar = false;
      for (char aChar : chars) {
        if (inputChar == aChar) {
          foundChar = true;
          break;
        }
      }

      if(foundChar)
        continue;

      return "Invalid character " + inputChar;
    }

    return "";
  }
}
