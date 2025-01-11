// package packt;

import com.aliasi.chunk.Chunk;
import com.aliasi.chunk.Chunker;
import com.aliasi.chunk.Chunking;
import com.aliasi.dict.DictionaryEntry;
import com.aliasi.dict.ExactDictionaryChunker;
import com.aliasi.dict.MapDictionary;
import com.aliasi.tokenizer.IndoEuropeanTokenizerFactory;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.NameSample;
import opennlp.tools.namefind.NameSampleDataStream;
import opennlp.tools.namefind.TokenNameFinderEvaluator;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.sentdetect.SentenceDetectorEvaluator;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.Span;
import java.io.*;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Chapter4 {

    private static final String[] sentences = {
            "Joe was the last person to see Fred. ",
            "He saw him in Boston at McKenzie's pub at 3:00 where he paid $2.45 for an ale. ",
            "Joe wanted to go to Vermont for the day to visit a cousin who works at IBM, but Sally and he had to look for Fred"
    };

    private static String regularExpressionText
            = "He left his email address (rgb@colorworks.com) and his "
            + "phone number,800-555-1234. We believe his current address "
            + "is 100 Washington Place, Seattle, CO 12345-1234. I "
            + "understand you can also call at 123-555-1234 between "
            + "8:00 AM and 4:30 most days. His URL is http://example.com "
            + "and he was born on February 25, 1954 or 2/25/1954.";

    private static MapDictionary<String> dictionary;

    // Основной метод для запуска всех подпрограмм
    public static void main(String[] args) {
        // Использование регулярных выражений
        usingRegularExpressions();

        System.out.println("\n\n--------------");
        // Пример использования OpenNLP
        usingOpenNLP();

        System.out.println("\n\n--------------");
        // Пример использования LingPipe для NER (раскомментировать если нужно)
        usingLingPipeNER();

        System.out.println("\n\n--------------");
        // Обучение модели OpenNLP (раскомментировать если нужно)
        // trainingOpenNLPNERModel();
    }

    // Метод для получения директории с моделями
    public static File getModelDir() {
        return new File("/home/share/4.142.2.23/openNLPModels");
    }

    // Подпрограмма для работы с регулярными выражениями
    private static void usingRegularExpressions() {
        usingJavaRegularExpressions(); // Пример использования Java регулярных выражений
    }

    // Пример использования Java регулярных выражений
    private static void usingJavaRegularExpressions() {
        String phoneNumberRE = "\\d{3}-\\d{3}-\\d{4}"; // Регулярное выражение для телефонных номеров
        String urlRegex = "\\b(https?|ftp|file|ldap)://"
                + "[-A-Za-z0-9+&@#/%?=~_|!:,.;]"
                + "*[-A-Za-z0-9+&@#/%=~_|]"; // Регулярное выражение для URL
        String zipCodeRegEx = "[0-9]{5}(\\-?[0-9]{4})?"; // Регулярное выражение для почтовых индексов
        String emailRegEx = "[a-zA-Z0-9'._%+-]+@"
                + "(?:[a-zA-Z0-9-]+\\.)"
                + "+[a-zA-Z]{2,4}"; // Регулярное выражение для email адресов
        String timeRE = "([01]?[0-9]|2[0-3]):[0-5][0-9]"; // Регулярное выражение для времени
        String dateRE = "(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/((19|20)\\\\d\\\\d)"; // Регулярное выражение для даты

        // Пример текста для поиска по регулярным выражениям
        regularExpressionText = "(888)555-1111 888-SEL-HIGH 888-555-2222-J88-W3S";
        Pattern pattern = Pattern.compile(phoneNumberRE + "|" + timeRE + "|" + emailRegEx); // Создание паттерна

        // Поиск всех совпадений в строке
        Matcher matcher = pattern.matcher(regularExpressionText);
        System.out.println("---Поиск ...");
        while (matcher.find()) {
            System.out.println(matcher.group() + " [" + matcher.start() + ":" + matcher.end() + "]");
        }
        System.out.println("---Поиск завершен");
    }

    // Подпрограмма для работы с OpenNLP (NER)
    private static void usingOpenNLP() {
        System.out.println("OpenNLP Примеры");
        usingOpenNLPNameFinderME(); // Пример работы с OpenNLP NameFinderME для NER
    }

    // Пример использования OpenNLP для поиска именованных сущностей
    private static void usingOpenNLPNameFinderME() {
        System.out.println("OpenNLP NameFinderME Примеры");

        try (InputStream tokenStream = new FileInputStream(new File(getModelDir(), "en-token.bin"));
             InputStream modelStream = new FileInputStream(new File(getModelDir(), "en-ner-person.bin"))) {

            // Инициализация моделей токенизации и поиска именованных сущностей
            TokenizerModel tokenModel = new TokenizerModel(tokenStream);
            Tokenizer tokenizer = new TokenizerME(tokenModel);
            TokenNameFinderModel entityModel = new TokenNameFinderModel(modelStream);
            NameFinderME nameFinder = new NameFinderME(entityModel);

            // Пример обработки одной фразы
            String sentence = "He was the last person to see Fred.";
            String[] tokens = tokenizer.tokenize(sentence);
            Span[] nameSpans = nameFinder.find(tokens); // Поиск именованных сущностей

            for (int i = 0; i < nameSpans.length; i++) {
                System.out.println("Сегмент: " + nameSpans[i].toString());
                System.out.println("Сущность: " + tokens[nameSpans[i].getStart()]);
            }
            System.out.println();

            // Обработка нескольких предложений
            for (String sentenceText : sentences) {
                String[] tokensArray = tokenizer.tokenize(sentenceText);
                Span[] nameSpansArray = nameFinder.find(tokensArray);
                double[] spanProbs = nameFinder.probs(nameSpansArray); // Получение вероятности найденных сущностей

                for (int i = 0; i < nameSpansArray.length; i++) {
                    System.out.println("Сегмент: " + nameSpansArray[i].toString());
                    System.out.println("Сущность: " + tokensArray[nameSpansArray[i].getStart()]);
                    System.out.println("Вероятность: " + spanProbs[i]);
                }
                System.out.println();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Инициализация словаря для использования с ExactDictionaryChunker
    private static void initializeDictionary() {
        dictionary = new MapDictionary<>();
        dictionary.addEntry(
                new DictionaryEntry<>("Joe", "PERSON", 1.0));
        dictionary.addEntry(
                new DictionaryEntry<>("Fred", "PERSON", 1.0));
        dictionary.addEntry(
                new DictionaryEntry<>("Boston", "PLACE", 1.0));
        dictionary.addEntry(
                new DictionaryEntry<>("pub", "PLACE", 1.0));
        dictionary.addEntry(
                new DictionaryEntry<>("Vermont", "PLACE", 1.0));
        dictionary.addEntry(
                new DictionaryEntry<>("IBM", "ORGANIZATION", 1.0));
        dictionary.addEntry(
                new DictionaryEntry<>("Sally", "PERSON", 1.0));
    }

    // Подпрограмма для использования ExactDictionaryChunker с LingPipe для поиска сущностей
    private static void usingExactDictionaryChunker() {
        initializeDictionary(); // Инициализация словаря
        System.out.println("\nСЛОВАРЬ\n" + dictionary);

        ExactDictionaryChunker dictionaryChunker = new ExactDictionaryChunker(
                dictionary, IndoEuropeanTokenizerFactory.INSTANCE, true, false);

        // Обработка каждого предложения
        for (String sentence : sentences) {
            System.out.println("\nТЕКСТ=" + sentence);
            displayChunkSet(dictionaryChunker, sentence); // Выводим найденные сущности
        }
    }

    // Вывод всех найденных сущностей для каждого предложения
    private static void displayChunkSet(Chunker chunker, String text) {
        Chunking chunking = chunker.chunk(text); // Разбиение текста на чанки
        Set<Chunk> set = chunking.chunkSet(); // Получаем все чанки
        for (Chunk chunk : set) {
            System.out.println("Тип: " + chunk.type() + " Сущность: ["
                    + text.substring(chunk.start(), chunk.end())
                    + "] Оценка: " + chunk.score());
        }
    }

    // Подпрограмма для использования LingPipe для NER
    private static void usingLingPipeNER() {
        usingExactDictionaryChunker(); // Используем ExactDictionaryChunker
    }
}
