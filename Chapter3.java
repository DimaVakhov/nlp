import nlp.examples.UsingRegularExpressions;
import nlp.examples.UsingBreakIterator;
import nlp.examples.UsingOpenNLP;
import nlp.examples.UsingStanfordPipeline;
import nlp.examples.UseLingPipeExamples;

public class Chapter3 {
    public static void main(String[] args) {
        String paragraph = "Here is an example paragraph that will be processed.";
        
        UsingRegularExpressions.process(paragraph);
        UsingBreakIterator.process(paragraph);
        UsingOpenNLP.process(paragraph, "/path/to/opennlp/models");
        UsingStanfordPipeline.process(paragraph);
        UseLingPipeExamples.process(paragraph);
    }
}
