package edu.ukma.tarasenko;

import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Scanner;

public class Main {
  private static String validateRadix(int radix, RadixCharactersCollection collection) {
    if(radix < 1 || radix > collection.getMaxRadix()) {
      return "Invalid radix value. Should be greater than 1 and less than " + collection.getMaxRadix();
    }

    return "";
  }

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);

    RadixCharactersCollection collection = RadixCharactersCollection.radixMax36;
    NumberStringValidator validator = new NumberStringValidator(collection);

    int maxRadix = collection.getMaxRadix();

    System.out.print("Enter input: ");
    String input = scanner.nextLine();

    String inputValidationResult = validator.validateString(input);
    if(inputValidationResult.length() != 0) {
      System.out.println(inputValidationResult);
      System.exit(-1);
    }

    System.out.printf("Enter input radix (max %d): ", maxRadix);
    int inputRadix = readRadix(scanner, collection);

    System.out.printf("Enter output radix (max %d): ", maxRadix);
    int outputRadix = readRadix(scanner, collection);

    RadixNumberConverter converter = new RadixNumberConverter(collection);

    String result = converter.convertToRadix(input, inputRadix, outputRadix);
    System.out.printf("The result is: %s\n", result.toUpperCase(Locale.ROOT));
  }

  private static int readRadix(Scanner scanner, RadixCharactersCollection collection) {
    int radix = 0;
    try {
      radix = scanner.nextInt();
    } catch (InputMismatchException exception) {
      System.out.println("Radix should be a number.");
      System.exit(-2);
    }

    String radixValidationResult = validateRadix(radix, collection);

    if(radixValidationResult.length() != 0) {
      System.out.println(radixValidationResult);
      System.exit(-1);
    }
    return radix;
  }
}
