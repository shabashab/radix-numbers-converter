package edu.ukma.tarasenko;

import java.util.Locale;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);

    System.out.print("Enter input: ");
    String input = scanner.nextLine();

    System.out.print("Enter input radix: ");
    int inputRadix = scanner.nextInt();

    System.out.print("Enter output radix: ");
    int outputRadix = scanner.nextInt();

    RadixNumberConverter converter = new RadixNumberConverter();

    String result = converter.convertToRadix(input, inputRadix, outputRadix);
    System.out.printf("The result is: %s\n", result.toUpperCase(Locale.ROOT));
  }
}
