import java.io.FileInputStream;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.lemmatizer.DictionaryLemmatizer;
import java.io.InputStream;

public class Chapter2{
	public static void main(String[] args) {
		usingTheOpenNLPLemmatizer();
	}
	
	private static void usingTheOpenNLPLemmatizer() {
		try{
			String[] tokens = new String[]{"На", "улице", "солнечная", "погода", ".", "Один","раз","в","день","делай","упражнение","спина","болеть","не","будет"};

			InputStream posModelIn = new FileInputStream("/home/share/4.142.2.23/openNLPModels/opennlp-ru-ud-gsd-pos-1.2-2.5.0.bin");
			POSModel posModel = new POSModel(posModelIn);
			POSTaggerME posTagger = new POSTaggerME(posModel);

			String[] tags = posTagger.tag(tokens);

			InputStream dictLemmatizer = new FileInputStream("/home/share/4.142.2.23/Вахов Д.П/en-lemmatizer.txt");
			DictionaryLemmatizer lemmatizer = new DictionaryLemmatizer(dictLemmatizer);
			
			String[] tagsN = new String[tokens.length];
			for (int i = 0; i < tokens.length; i++) {
				switch (tags[i]) {
					case "NOUN": tagsN[i] = "NNN";
					break;
					case "DET": tagsN[i] = "DT";
					break;
					case "ADV": tagsN[i] = "RB";
					break;
					case "ADJ": tagsN[i] = "JJ";
					break;
					case "PROPN": tagsN[i] = "JJ";
					break;
					case "VERB": tags[i] = "VB";
					break;
					default: tagsN[i] = "PUNCT";
				}
			}
			String[] lemmas = lemmatizer.lemmatize(tokens, tags);

			System.out.println("\nPrinting lemmas for the given sentence...");
			System.out.println("WORD - POSTAG : LEMMA");
			for (int i = 0; i < tokens.length; i++) {
				System.out.println(tokens[i] + " - " + tags[i] + " : " + lemmas[i]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("-------------Завершение OpenNLPLemmatizer-----------\n");
	}
}
