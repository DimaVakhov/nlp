import UsingRegularExpressions;
import UsingBreakIterator;
import UsingOpenNLP;
import UsingStanfordPipeline;
import UseLingPipeExamples;

public class Chapter3 {
    public static void main(String[] args) {
        String paragraph = "Here is an example paragraph that will be processed.";
        
        // Вызов методов из классов без использования импорта
        UsingRegularExpressions.process(paragraph);
        UsingBreakIterator.process(paragraph);
        UsingOpenNLP.process(paragraph, "/path/to/opennlp/models");
        UsingStanfordPipeline.process(paragraph);
        UseLingPipeExamples.process(paragraph);
    }
}
