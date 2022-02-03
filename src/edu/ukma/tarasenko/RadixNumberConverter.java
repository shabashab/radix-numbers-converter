package edu.ukma.tarasenko;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class RadixNumberConverter {
  private final RadixCharactersCollection _charsCollection;

  public RadixNumberConverter(RadixCharactersCollection charsCollection) {
    this._charsCollection = charsCollection;
  }

  private int convertStringToDecimalInt(String input, int radix) {
    int result = 0;
    char[] preparedStringChars = prepareString(input).toCharArray();

    for (int digitCounter = 0, stringIterator = preparedStringChars.length - 1; stringIterator >= 0; digitCounter++, stringIterator--) {
      int characterValue = _charsCollection.evaluateCharacter(preparedStringChars[stringIterator]);

      result += characterValue * ((int) Math.pow(radix, digitCounter));
    }

    return result;
  }

  private BigDecimal convertStringToDecimal(String input, int radix) {
    String[] parts = input.split("[,.]");

    if (parts.length == 1)
      return new BigDecimal(convertStringToDecimalInt(input, radix));

    if (parts.length != 2)
      return new BigDecimal(0);

    String integerPart = parts[0];
    String fractionPart = parts[1];

    int integerPartValue = convertStringToDecimalInt(integerPart, radix);

    char[] fractionPartCharArray = fractionPart.toCharArray();
    BigDecimal fractionPartValue = new BigDecimal(0);

    for (int i = 0; i < fractionPart.length(); i++) {
      int charValue = _charsCollection.evaluateCharacter(fractionPartCharArray[i]);
      fractionPartValue = fractionPartValue.add(new BigDecimal(charValue * (Math.pow(radix, -1 * (i + 1)))));
    }

    return fractionPartValue.add(new BigDecimal(integerPartValue));
  }

  private String convertIntToString(int input, int radix) {
    StringBuilder resultStringBuilder = new StringBuilder();

    while (input > 0) {
      int numberValue = input % radix;
      input /= radix;
      resultStringBuilder.append(_charsCollection.getCharByValue(numberValue));
    }

    resultStringBuilder.reverse();

    return resultStringBuilder.toString();
  }

  private String convertDecimalToString(BigDecimal input, int radix, int fractionDigitsCount) {
    int integerPart = input.intValue();
    BigDecimal fractionPart = input.subtract(new BigDecimal(integerPart));
    fractionPart = fractionPart.setScale(15, RoundingMode.FLOOR);

    String integerPartString = convertIntToString(integerPart, radix);

    StringBuilder fractionPartStringBuilder = new StringBuilder();

    for (int i = 0; i < fractionDigitsCount; i++) {
      fractionPart = fractionPart.multiply(new BigDecimal(radix));

      int value = fractionPart.intValue();
      fractionPart = fractionPart.subtract(new BigDecimal(value));

      fractionPartStringBuilder.append(_charsCollection.getCharByValue(value));

      if (fractionPart.compareTo(new BigDecimal(0)) == 0)
        break;
    }

    String fractionPartString = fractionPartStringBuilder.toString();

    return integerPartString + "," + fractionPartString;
  }

  private String convertToRadixDouble(String input, int inputRadix, int outputRadix, int fractionDigitsCount) {
    BigDecimal value = convertStringToDecimal(input, inputRadix);
    return convertDecimalToString(value, outputRadix, fractionDigitsCount);
  }

  private String convertToRadixInt(String input, int inputRadix, int outputRadix) {
    int value = convertStringToDecimalInt(input, inputRadix);
    return convertIntToString(value, outputRadix);
  }

  //Returns a string with an error or empty string if no error
  private String validateString(String input) {
    if(input.matches("[0-9a-z]+[,.][0-9a-z]+"))
      return "";

    return "Invalid string format";
  }

  private boolean checkIsStringNumberHasFractionPart(String input) {
    return input.matches("[0-9a-z]+[,.][0-9a-z]+");
  }

  private String prepareString(String input) {
    return input.toLowerCase();
  }

  public String convertToRadix(String input, int inputRadix, int outputRadix) {
    String preparedString = prepareString(input);

    final int fractionDigitsCount = 250;

    String validationResult = validateString(preparedString);

    if (validationResult.length() != 0)
      throw new IllegalArgumentException("An error occurred while validating input string: " + validationResult);

    if (checkIsStringNumberHasFractionPart(preparedString)) {
      return convertToRadixDouble(input, inputRadix, outputRadix, fractionDigitsCount);
    }

    return convertToRadixInt(input, inputRadix, outputRadix);
  }
}
