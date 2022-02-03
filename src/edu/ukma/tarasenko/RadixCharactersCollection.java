package edu.ukma.tarasenko;

public class RadixCharactersCollection {
  private final char[] _characters;

  public static RadixCharactersCollection radixMax36 = new RadixCharactersCollection("0123456789abcdefghijklmnopqrstuvwxyz".toCharArray());

  public RadixCharactersCollection(char[] characters) {
    _characters = characters;
  }

  public int evaluateCharacter(char character) {
    for(int i = 0; i < _characters.length; i++) {
      if(character == _characters[i])
        return i;
    }

    throw new IllegalArgumentException("The character does not exist in characters array");
  }

  public char getCharByValue(int value) {
    if(value >= _characters.length)
      throw new IllegalArgumentException("No character for given value has been found");

    return _characters[value];
  }

  public char[] getCharactersArray() {
    return _characters;
  }

  public int getMaxRadix() {
    return _characters.length;
  }
}
