package part1.lesson06.task02;

import java.security.SecureRandom;
import java.util.ArrayList;

/**
 * Класс описывающий слово в составе предложения.
 * wordString собственно слово.
 * comma - признак наличия запятой после слова.
 * wordLength - длина слова в символах (+1 если есть запятая).
 *
 * @see Sentence
 */
class Word {
    String wordString;
    boolean comma;
    int wordLength;

    /**
     * конструктор1.
     * Гененрируется слово. Слово может быть неслучайной длины (см. параметры).
     * При необходимости фиксированной длины слова, слово создается с признаком отсутствия запятой.
     * Если длина слова случайна, то признак наличия запятой "Word.comma" ставится случайным образом.
     *
     * @param generator экземпляр Generator, для использования метода "Generator.generateNamePart" и
     * существующего в Generator экземпляра SecureRandom.
     * @param length длина нового слова. Если randomLength=true, то длина нового слова -
     * случайная от 1 до length, если randomLength=false, то длина нового слова равна length.
     * @param randomLength признак случайной или фиксированной длины слова, см. параметр length
     */
    Word(Generator generator, int length, boolean randomLength) {
        wordString = generator.generateNamePart(length, randomLength);
        wordLength = wordString.length() + 1;
        comma = true;
        if ((generator.getSecureRandom().nextInt(2) == 1) || !randomLength) {
            comma = false;
            wordLength--;
        }
    }
    /**
     * конструктор2.
     * Создается объект Word, соответствующий случайно выбранному слову из массива-словаря words.
     * Признак наличия запятой "Word.comma" ставится случайным образом.
     *
     * @param words массив-словарь, из которого будет выбрано слово
     * @param secureRandom SecureRandom
     */
    Word(String[] words, SecureRandom secureRandom) {
        wordString = words[secureRandom.nextInt(words.length)];
        wordLength = wordString.length() + 1;
        comma = true;
        if ((secureRandom.nextInt(2) == 1)) {
            comma = false;
            wordLength--;
        }
    }

    public int getWordLength() {
        return wordLength;
    }
    /**
     * сеттер "comma".
     * дополнительно корректируется поле wordLength в зависимости от старого и нового значения "comma"
     *
     * @param comma см. поля Word
     */
    public void setComma(boolean comma) {
        if (this.comma && (!comma)) wordLength--;
        if (!this.comma && comma) wordLength++;
        this.comma = comma;
    }

    public boolean isComma() {
        return comma;
    }

    public String getWordString() {
        return wordString;
    }
    /**
     * сеттер "wordString".
     * дополнительно корректируется поле wordLength в зависимости от старого и нового значения "wordString"
     *
     * @param wordString см. поля Word
     */
    public void setWordString(String wordString) {
        wordLength+= wordString.length()-this.wordString.length();
        this.wordString = wordString;
    }

}

/**
 * Класс описывающий предложение.
 * words - массив слов, описанных классом Word.
 * Знак препинания в конце предложения не учитывается.
 *
 * @see Word
 */
class Sentence {
    ArrayList<Word> words;

    public ArrayList<Word> getWords() {
        return words;
    }

    public void setWords(ArrayList<Word> words) {
        this.words = words;
    }

}

