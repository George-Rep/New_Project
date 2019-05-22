package part1.lesson11.task01;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

/**
 * вспомогательный класс
 */
public class MathUtils {
    /**
     * generateNamePart генерирует одно слово.
     * Латинскими буквами, первая буква слов заглавная, слова случайной длины от 2 символов
     * до значения параметра maxLength.
     *
     * @param maxLength максимальная длина генерируемого слова
     * @return сгененрированное слово
     */
    String generateNamePart(int maxLength) {
        SecureRandom secureRandom = new SecureRandom();
        String letters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        int strlength = 2 + secureRandom.nextInt(maxLength - 1);
        StringBuilder stringBuilder = new StringBuilder(strlength);
        int charIndex = secureRandom.nextInt(26);
        stringBuilder.append(letters.charAt(charIndex + 26));
        for (int i = 1; i < strlength; i++) {
            charIndex = secureRandom.nextInt(26);
            stringBuilder.append(letters.charAt(charIndex));
        }
        return stringBuilder.toString();
    }
}
