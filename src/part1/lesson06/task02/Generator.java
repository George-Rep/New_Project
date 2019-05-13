package part1.lesson06.task02;


import java.io.*;
import java.security.SecureRandom;
import java.util.ArrayList;

import static part1.lesson06.task02.Main.LINESEPARATOR;

/**
 * класс для генерации файлов
 * @see Main
 *
 *
 */
public class Generator {
private    int[] sentenceWordsCount = new int[2];
    private    int[] sentenceLength = new int[2];
    private    Sentence[] sentence = new Sentence[2];
    private    int[] wordIndex = new int[2];
    private    Word[] wordFromSentence = new Word[2];
    private    int estimatedParagraphSentencesCount;
    private    int paragraphSentencesCounter;
    private    int randomSentenceWordsCount;
    private    int randomDictWordPos;
    private    int flagVar;
    private    int charactersRemaining;
    private    int sentenceLastIndex = 0;
    private    int randomEndSequencePosition;
    private    int word1Length, word2Length;
    private    int sentencesBorder;

    private    SecureRandom secureRandom = new SecureRandom();
    private    BufferedWriter bufferedWriter;

    /**
     * метод случайной генерации строки. Длина может быть неслучайной, см. параметры.
     *
     * @param maxLength длина нового слова. Если randomLength=true, то длина нового слова -
     * случайная от 1 до maxLength, если randomLength=false, то длина нового слова равна maxLength.
     * @param randomLength признак случайной или фиксированной длины слова, см. параметр maxLength
     * @return Возвращает случайно сгенерированную строку, см. параметры.
     */
    String generateNamePart(int maxLength, boolean randomLength) {
        int strLength;
        String letters = "abcdefghijklmnopqrstuvwxyz";

        if (randomLength) {
            strLength = 1 + secureRandom.nextInt(maxLength);
        } else {
            strLength = maxLength;
        }

        StringBuilder stringBuilder = new StringBuilder(strLength);
        for (int i = 0; i < strLength; i++) {
            int charIndex = secureRandom.nextInt(26);
            stringBuilder.append(letters.charAt(charIndex));
        }
        return stringBuilder.toString();
    }
    /**
     * Метод увеличения предложения sentence на количество символов addAmount.
     * Параметр wordIndex это индекс слова в предложении (т.е. слова sentence.getWords().get(wordIndex)), данное слово
     * по итогам работы метода останется неизменным. В случае, когда в предложении есть слово из словаря
     * (словарь- параметр words в getFiles), то wordIndex это индекс слова из словаря.
     * Формируется массив addArray с исходными длинами слов в предложении. Элементы массива, кроме элемента с индексом
     * wordIndex, увеличиваются (не превышая максимальную длину слова, т.е. 15).
     * Слова в предложении sentence дополняются случайными символами на основании данных из массива addArray.
     *
     * @param sentence предложение для увеличения
     * @param addAmount количество символов, на которое нужно увеличить предложение sentence
     * @param wordIndex индекс слова, которое нужно оставить неизменным, в предложении sentence.
     */
private    void addToSentence(Sentence sentence, int addAmount, int wordIndex) {
        int[] addArray = new int[15];
        int size = sentence.getWords().size();
        for (int i = 0; i < size; i++)
            addArray[i] = sentence.getWords().get(i).getWordString().length();

        int index = secureRandom.nextInt(size);
        while (addAmount > 0) {
            if ((index != wordIndex) && (addArray[index] != 15)) {
                addArray[index]++;
                addAmount--;
            }
            index++;
            if (index == size) index = 0;
        }
        for (int i = 0; i < size; i++) {
            if (addArray[i] > sentence.getWords().get(i).getWordString().length()) {
                sentence.getWords().get(i).setWordString(sentence.getWords().get(i).getWordString()
                        + generateNamePart(addArray[i] - sentence.getWords().get(i).getWordString().length(), false));
            }
        }
    }

    /**
     * Метод уменьшения предложения sentence на количество символов subtractAmount.
     * Параметр wordIndex это индекс слова в предложении (т.е. слова sentence.getWords().get(wordIndex)), данное слово
     * по итогам работы метода останется неизменным. В случае, когда в предложении есть слово из словаря
     * (словарь- параметр words в getFiles), то wordIndex это индекс слова из словаря.
     * Формируется массив subtractArray с исходными длинами слов в предложении. Элементы массива, кроме элемента с индексом
     * wordIndex, уменьшаются (при этом длина слова не может быть меньше 1).
     * Слова в предложении sentence уменьшаются (лишние символы убираются) на основании данных из массива subtractArray.
     *
     * @param sentence предложение для уменьшения
     * @param subtractAmount количество символов, на которое нужно уменьшить предложение sentence
     * @param wordIndex индекс слова, которое нужно оставить неизменным, в предложении sentence.
     */
    private    void subFromSentence(Sentence sentence, int subtractAmount, int wordIndex) {

        int[] subtractArray = new int[15];
        int size = sentence.getWords().size();
        for (int i = 0; i < size; i++)
            subtractArray[i] = sentence.getWords().get(i).getWordString().length();

        int index = secureRandom.nextInt(size);
        while (subtractAmount > 0) {
            if ((index != wordIndex) && (subtractArray[index] != 1)) {
                subtractArray[index]--;
                subtractAmount--;
            }
            index++;
            if (index == size) index = 0;
        }
        for (int i = 0; i < size; i++) {
            if (subtractArray[i] < sentence.getWords().get(i).getWordString().length()) {
                sentence.getWords().get(i).setWordString(sentence.getWords().get(i).getWordString()
                        .substring(0, subtractArray[i]));
            }
        }
    }

    /**
     * Метод для случайной генерации знака препинания (для использования в конце предложения)
     * @return строку со знаком препинания.
     */
private    String randomPunctuation() {
        int randomPunctuation = secureRandom.nextInt(3);
        String str2 = null;
        if (randomPunctuation == 0) str2 = ".";
        if (randomPunctuation == 1) str2 = "!";
        if (randomPunctuation == 2) str2 = "?";
        return str2;
    }

    /**
     * Метод для замены первой буквы строки на заглавную.
     * @param str строка для замены.
     * @return строку из параметра, первая буква строки переведена в верхний регистр.
     */
    private    String upperCaseFirst(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    /**
     * Метод для случайной генерации предложения. В массив sentence по индексу из параметра sentenceNumber в
     * предложение sentence[sentenceNumber] добавляются слова. Случайно определяется wordIndex, индекс специального
     * слова. По данному индексу помещается слово из wordFromSentence, т.е. в случае если по итогам расчетов в
     * предложении должно содержаться одно слово из массива words из параметра метода getFiles, то это слово
     * из массива words будет в wordFromSentence и в формируемом предложении будет размещено по индексу wordIndex.
     * Из последнего слова предложения убирается признак наличия запятой после слова. Подсчитывается длина
     * предложения, sentenceLength (без учета знака препинания в конце предложения).
     *
     * @param sentenceNumber "номер предложения". Алгоритм использует два предложения, массив из двух
     * элементов sentence. Номер это индекс в массиве, может быть равен 0 или 1.
     * @see Main
     */
    private    void generateSentence(int sentenceNumber) {
        for (int i = 0; i < sentenceWordsCount[sentenceNumber]; i++) {
            sentence[sentenceNumber].getWords().add(i, new Word(this, 15, true));
            sentenceLength[sentenceNumber] += sentence[sentenceNumber].getWords().get(i).getWordLength() + 1;
        }

        wordIndex[sentenceNumber] = secureRandom.nextInt(sentenceWordsCount[sentenceNumber]);
        sentenceLength[sentenceNumber] += wordFromSentence[sentenceNumber].getWordLength() - sentence[sentenceNumber].getWords().get(wordIndex[sentenceNumber]).getWordLength();
        sentence[sentenceNumber].getWords().set(wordIndex[sentenceNumber], wordFromSentence[sentenceNumber]);

        if (sentence[sentenceNumber].getWords().get(sentenceWordsCount[sentenceNumber] - 1).isComma()) {
            sentenceLength[sentenceNumber]--;
            sentence[sentenceNumber].getWords().get(sentenceWordsCount[sentenceNumber] - 1).setComma(false);
        }
    }
    /**
     * Метод для преобразования объекта Sentence (предложения) в строку,
     * ставятся пробелы между словами, случайный знак препинания и пробел в конце предложения,
     * первая буква первого слова делается заглавной.
     * @param sentence предложение для преобразования.
     * @param size количество слов в предложении.
     * @return строковое представление предложения sentence, со случайным знаком препинания в конце предложения(и пробелом).
     */
  private  String sentenceToString(Sentence sentence, int size) {
        String str = sentence.getWords().get(0).getWordString();
        if (sentence.getWords().get(0).isComma()) str += ",";
        str = upperCaseFirst(str) + " ";
        for (int i = 1; i < size; i++) {
            str += sentence.getWords().get(i).getWordString();
            if (sentence.getWords().get(i).isComma()) str += ",";
            str += " ";
        }
        return str.substring(0, str.length() - 1) + randomPunctuation() + " ";
    }

    /**
     * Метод для использования в методе generation120.
     * для размера файлов >=120
     * Основной цикл генерации, с контролируемой остановкой.
     * Организуется цикл, в котором в файл записываются абзацы с предложениями, выход из цикла осуществлется тогда,
     * когда после генерации очередного слова количество символов, которое осталось сгенерировать для достижения
     * необходимого размера файла, стало меньше порогового значения. Пороговое значение случайно выбирается из чисел
     * между 159 и 243. По достижении порогового значения генерация текущего предложения обрывается. Если слово из
     * словаря words случайно получило индекс в предложении (т.е. в т.ч. алгоритмом была определена необходимость
     * наличия данного слова в предложении), который оказался после места "обрыва", то слову из словаря words
     * присваивается новый, допустимый индекс в предложении. Если по расчетам это было последнее предложение
     * абзаца, ставятся разрыв строки и перенос каретки.
     *
     * @param probability см. probability в методе getFiles.
     * @param words см. words в методе getFiles
     * @throws IOException обрабатывается в методе getFiles
     */
private    void mainGenerationWithBreak(int probability,String[] words) throws IOException {
        while (true) {
            estimatedParagraphSentencesCount = 1 + secureRandom.nextInt(20);
            paragraphSentencesCounter = 0;
            for (int j = 0; j < estimatedParagraphSentencesCount; j++) {
                randomSentenceWordsCount = 1 + secureRandom.nextInt(15);
                sentence[0].setWords(new ArrayList<>(randomSentenceWordsCount));
                randomDictWordPos = -1;
                if ((probability == 1) || (secureRandom.nextFloat() < 1.0 / probability)) {
                    randomDictWordPos = secureRandom.nextInt(randomSentenceWordsCount);
                }
                for (int i = 0; i < randomSentenceWordsCount; i++) {
                    if (randomDictWordPos == i) {
                        sentence[0].getWords().add(i, new Word(words, secureRandom));
                    } else {
                        sentence[0].getWords().add(i, new Word(this, 15, true));
                    }

                    charactersRemaining -= sentence[0].getWords().get(i).getWordLength() + 1;
                    sentenceLastIndex = i;
                    if (charactersRemaining < randomEndSequencePosition) {
                        if (randomDictWordPos > sentenceLastIndex) {
                            randomDictWordPos = secureRandom.nextInt(sentenceLastIndex + 1);
                            int oldWordLength = sentence[0].getWords().get(randomDictWordPos).getWordLength();
                            sentence[0].getWords().set(randomDictWordPos, new Word(words, secureRandom));
                            charactersRemaining = charactersRemaining + oldWordLength - sentence[0].getWords().get(randomDictWordPos).getWordLength();
                            if (charactersRemaining < randomEndSequencePosition) {
                                flagVar = 1;
                                break;
                            }
                        } else {
                            flagVar = 1;
                            break;
                        }
                    }
                }

                if (sentence[0].getWords().get(sentenceLastIndex).isComma()) {
                    charactersRemaining++;
                    sentence[0].getWords().get(sentenceLastIndex).setComma(false);
                }
                charactersRemaining--;
                bufferedWriter.write(sentenceToString(sentence[0], sentenceLastIndex + 1));

                paragraphSentencesCounter++;
                if (flagVar == 1) break;
            }
            if (paragraphSentencesCounter == estimatedParagraphSentencesCount) {
                bufferedWriter.write("\n\r");
                charactersRemaining -= 2;
                paragraphSentencesCounter = 0;
            }

            if (flagVar == 1) break;
        }

    }

    /**
     * Метод для использования в методе getFiles.
     * для генерации при размере файлов >=120
     * После вызова mainGenerationWithBreak, для достижения необходимого размера файла size (см. getFiles) будет
     * сгенерировано 1 или 2 предложения. При необходимости определяются два слова из словаря, по одному на
     * будущее предложение. Случайно рассчитывается примерная длина двух предложений(с учетом что их совместная длина
     * должна равняться количеству символов для достижения необходимого размера файла, а длина одного не превышала бы
     * значительно 15*8, т.е. (количество слов * среднюю длину слова) ). Исходя из длины, делается предположение о
     * необходимом количестве слов в предложении. Генерируются 2 предложения. Если первое предложение превысило по длине
     * остаток до необходимого размера файла, то используется метод уменьшения предложения и только одно
     * предложение. Если не превысило, то используются два предложения, которые доводятся до расчетной длины
     * методами увеличения/уменьшения addToSentence/subFromSentence. В конце файла записываются разрыв строки и перенос
     * каретки. Они записываются после обоих предложений, если предложение до них было 19-м в абзаце.
     *
     * @param probability см. probability в методе getFiles.
     * @param words см. words в методе getFiles
     * @throws IOException обрабатывается в методе getFiles
     */
private    void generation120(int probability, String[] words) throws IOException {
    mainGenerationWithBreak(probability,words);
        if (paragraphSentencesCounter == 19) {
            charactersRemaining -= 6;
        } else {
            charactersRemaining -= 4;
        }

        if ((probability == 1) || (secureRandom.nextFloat() < 1.0 / probability)) {
            wordFromSentence[0] = new Word(words, secureRandom);
        } else wordFromSentence[0] = new Word(this,  15, true);

        if ((probability == 1) || (secureRandom.nextFloat() < 1.0 / probability)) {
            wordFromSentence[1] = new Word(words, secureRandom);
        } else wordFromSentence[1] = new Word(this, 15, true);

        word1Length = wordFromSentence[0].getWordString().length() + 1;
        word2Length = wordFromSentence[1].getWordString().length() + 1;
        if (Math.max(charactersRemaining - word1Length - 14 * 8, word2Length) < Math.min((word2Length + 14 * 8), (charactersRemaining - word1Length))) {
            sentencesBorder = Math.max(charactersRemaining - word1Length - 14 * 8, word2Length) + secureRandom.nextInt(Math.min((word2Length + 14 * 8), (charactersRemaining - word1Length)) - Math.max(charactersRemaining - word1Length - 14 * 8, word2Length) + 1);
            if ((sentencesBorder < (charactersRemaining - word1Length)) && (sentencesBorder > (charactersRemaining - word1Length - 3)))
                sentencesBorder = charactersRemaining - word1Length - 3;
            if ((sentencesBorder > (word2Length)) && (sentencesBorder < (word2Length + 3)))
                sentencesBorder = word2Length + 3;
            sentenceWordsCount[0] = (int) Math.ceil((charactersRemaining - word1Length - sentencesBorder) / 8.0) + 1;
            sentenceWordsCount[1] = (int) Math.ceil((sentencesBorder - word2Length) / 8.0) + 1;
        } else {
            sentencesBorder = Math.min((word2Length + 14 * 8), (charactersRemaining - word1Length)) + secureRandom.nextInt(Math.max(charactersRemaining - word1Length - 14 * 8, word2Length) - Math.min((word2Length + 14 * 8), (charactersRemaining - word1Length)) + 1);
            sentenceWordsCount[0] = 15;
            sentenceWordsCount[1] = 15;
        }
        sentenceLength[0] = 0;
        sentenceLength[1] = 0;
        sentence[0].setWords(new ArrayList<>(sentenceWordsCount[0]));
        sentence[1].setWords(new ArrayList<>(sentenceWordsCount[1]));
        generateSentence(0);
        generateSentence(1);

        flagVar = 0;
        if (sentenceLength[0] >= (charactersRemaining + 1)) {
            subFromSentence(sentence[0], sentenceLength[0] - (charactersRemaining + 1), wordIndex[0]);
            flagVar = 1;
        }
        if (flagVar == 0) {
            if (sentenceLength[0] > (charactersRemaining - sentencesBorder)) {
                subFromSentence(sentence[0], sentenceLength[0] - (charactersRemaining - sentencesBorder), wordIndex[0]);
            } else {
                addToSentence(sentence[0], charactersRemaining - sentencesBorder - sentenceLength[0], wordIndex[0]);
            }
            if (sentenceLength[1] > sentencesBorder) {
                subFromSentence(sentence[1], sentenceLength[1] - sentencesBorder, wordIndex[1]);
            } else {
                addToSentence(sentence[1], sentencesBorder - sentenceLength[1], wordIndex[1]);
            }
        }

        bufferedWriter.write(sentenceToString(sentence[0], sentence[0].getWords().size()));

        if (flagVar == 0) {
            if (paragraphSentencesCounter == 19)
                bufferedWriter.write("\n\r");

            bufferedWriter.write(sentenceToString(sentence[1], sentence[1].getWords().size()));
        }
        bufferedWriter.write("\n\r");

    }

    /**
     * метод для создания "n" файлов размера size в каталоге path.
     *
     * См. также описание параметров words и probability. Выводятся сообщения об ошибке при слишком малой величине size.
     * Минимальная величина 5 при (probability!=1) и 21 при (probability==1). 21 символ в данном случае позволяет взять
     * из словаря любое слово в 15 или менее символов и составить предложение из 2 слов. Одно слово из словаря, второе
     * слово используется для достижения необходимого размера файлов. Например 15 и 1 символ - собственно два слова,
     * плюс к этому пробел между словами, знак препинания, пробел в конце предложения и два символа конца абзаца.
     * Запятой в этом случае быть не может. Для размера файла 20 и (probability!=1) создается одно предложение
     * из двух слов. Для размера файла <20 и (probability!=1) создается одно предложение из одного слова. Иначе, для
     * размера файла <120 формируется одно предложение, содержащее до 15 слов. Если размер файла от 120 используется
     * метод generation120(probability,words);
     *
     * @param path каталог для создания файлов
     * @param n количество создаваемых файлов
     * @param size размер создаваемых файлов
     * @param words массив-словарь, с вероятностью "1/probability" в каждое предложение в формируемом файле
     * входит один из элементов данного массива.
     * @param probability "вероятность" использования слова из словаря, см. words
     */
    void getFiles(String path, int n, int size, String[] words, int probability) {
        String tempString;
        File outputFile;
        sentence[0] = new Sentence();
        sentence[1] = new Sentence();

        if ((size < 21) && (probability == 1)) {
            System.out.println("Ситуация, когда [размер файла] меньше 21," + LINESEPARATOR
                    + " и необходимо гарантированно включить в предложение в тексте" + LINESEPARATOR
                    + "файлов слово из \"words\", не реализована.");

        } else if ((size < 5) && (probability != 1)) {
            System.out.println("Минимальный размер файла: 5");

        } else {
            for (int numberOfFile = 1; numberOfFile < n + 1; numberOfFile++) {
                flagVar = 0;
                charactersRemaining = size;
                randomEndSequencePosition = 159 + secureRandom.nextInt(85);

                outputFile = new File(path + numberOfFile + ".txt");
                try {
                     bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "Windows-1251"));

                    if (size < 21) {
                        if (size == 20) {
                            tempString = generateNamePart(7, false)
                                    + " " + generateNamePart(8, false);
                            bufferedWriter.write(upperCaseFirst(tempString) + randomPunctuation() + " \n\r");
                        } else {
                            tempString = generateNamePart(size - 4, false);
                            bufferedWriter.write(upperCaseFirst(tempString) + randomPunctuation() + " \n\r");
                        }

                    } else if (size < 120) {
                        charactersRemaining = size - 3;
                        if ((probability == 1) || (secureRandom.nextFloat() < 1.0 / probability)) {
                            wordFromSentence[0] = new Word(words, secureRandom);
                        } else wordFromSentence[0] = new Word(this,  15, true);

                        word1Length = wordFromSentence[0].getWordString().length() + 1;
                        sentenceWordsCount[0] = (int) Math.ceil((charactersRemaining - word1Length) / 8.0) + 1;
                        if (sentenceWordsCount[0] > 15) sentenceWordsCount[0] = 15;
                        sentenceLength[0] = 0;
                        sentence[0].setWords(new ArrayList<>(sentenceWordsCount[0]));

                        generateSentence(0);
                        if ((charactersRemaining == 18)&&(word1Length==15)) {
                            if (sentence[0].getWords().get(0).isComma()) {
                                sentenceLength[0]--;
                                sentence[0].getWords().get(0).setComma(false);
                            }
                        }

                        if (sentenceLength[0] > charactersRemaining) {
                            subFromSentence(sentence[0], sentenceLength[0] - charactersRemaining, wordIndex[0]);
                        } else {
                            addToSentence(sentence[0], charactersRemaining - sentenceLength[0], wordIndex[0]);
                        }

                        bufferedWriter.write(sentenceToString(sentence[0], sentence[0].getWords().size()) + "\n\r");

                    } else {
                        generation120(probability,words);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                    try
                    {
                        if (bufferedWriter != null)
                            bufferedWriter.close();
                    }
                    catch (IOException ex)
                    {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }
    SecureRandom getSecureRandom() {
        return secureRandom;
    }

}
