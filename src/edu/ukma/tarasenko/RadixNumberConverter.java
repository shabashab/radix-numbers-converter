package edu.ukma.tarasenko;

import java.math.BigDecimal;
import java.util.Locale;

public class RadixNumberConverter {
  private final int maxFractionDigitsCount = 32;
  private final char[] chars;

  public RadixNumberConverter() {
    chars = "0123456789abcdef".toCharArray();
  }

  private String prepareString(String input) {
    return input.toLowerCase(Locale.ROOT);
  }

  private int evaluateCharacter(char value) {
    for(int i = 0; i < value; i++) {
      if(value == chars[i])  {
        return i;
      }
    }

    return 0;
  }

  private int convertStringToDecimalInt(String input, int radix) {
    int result = 0;
    char[] preparedStringChars = prepareString(input).toCharArray();

    for (int digitCounter = 0, stringIterator = preparedStringChars.length - 1; stringIterator >= 0; digitCounter++, stringIterator--) {
      int characterValue = evaluateCharacter(preparedStringChars[stringIterator]);

      result += characterValue * ((int)Math.pow(radix, digitCounter));
    }

    return result;
  }

  private BigDecimal convertStringToDecimal(String input, int radix) {
    String[] parts = input.split("[,.]");

    if(parts.length == 1)
      return new BigDecimal(convertStringToDecimalInt(input, radix));

    if(parts.length != 2)
      return new BigDecimal(0);

    String integerPart = parts[0];
    String fractionPart = parts[1];

    int integerPartValue = convertStringToDecimalInt(integerPart, radix);

    char[] fractionPartCharArray = fractionPart.toCharArray();
    BigDecimal fractionPartValue = new BigDecimal(0);

    for(int i = 0; i < fractionPart.length(); i++) {
      int charValue = evaluateCharacter(fractionPartCharArray[i]);
      fractionPartValue = fractionPartValue.add(new BigDecimal(charValue * (Math.pow(radix, -1 * (i + 1)))));
    }

    return fractionPartValue.add(new BigDecimal(integerPartValue));
  }

  private String convertIntToString(int input, int radix) {
    StringBuilder resultStringBuilder = new StringBuilder();

    while(input > 0) {
      int numberValue = input % radix;
      input /= radix;
      resultStringBuilder.append(chars[numberValue]);
    }

    resultStringBuilder.reverse();

    return resultStringBuilder.toString();
  }

  private String convertDecimalToString(BigDecimal input, int radix) {
    int integerPart = input.intValue();
    BigDecimal fractionPart = input.subtract(new BigDecimal(integerPart));

    String integerPartString = convertIntToString(integerPart, radix);

    StringBuilder fractionPartStringBuilder = new StringBuilder();

    for(int i = 0; i < maxFractionDigitsCount; i++) {
      fractionPart = fractionPart.multiply(new BigDecimal(radix));

      int value = fractionPart.intValue();
      fractionPart = fractionPart.subtract(new BigDecimal(value));

      fractionPartStringBuilder.append(chars[value]);
    }

    String fractionPartString = fractionPartStringBuilder.toString();

    return integerPartString + "," + fractionPartString;
  }

  private String convertToRadixDouble(String input, int inputRadix, int outputRadix) {
    BigDecimal value = convertStringToDecimal(input, inputRadix);
    return convertDecimalToString(value, outputRadix);
  }

  private String convertToRadixInt(String input, int inputRadix, int outputRadix) {
    int value = convertStringToDecimalInt(input, inputRadix);
    return convertIntToString(value, outputRadix);
  }

  public String convertToRadix(String input, int inputRadix, int outputRadix) {
    if(input.matches("[0-9]+[,.][0-9]+")) {
      return convertToRadixDouble(input, inputRadix, outputRadix)   ;
    }

    return convertToRadixInt(input, inputRadix, outputRadix);
  }
}
