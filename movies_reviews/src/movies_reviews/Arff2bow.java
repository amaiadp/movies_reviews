package movies_reviews;


import weka.core.Instances;
import weka.core.tokenizers.WordTokenizer;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class Arff2bow {

	
	public static Instances bagOfWords(Instances data){
		System.out.println(data.relationName() + " fitxategiaren Bag of Words lortzen...");
		StringToWordVector filter = new StringToWordVector();
		filter.setWordsToKeep(9999);
		filter.setOutputWordCounts(false);
		filter.setLowerCaseTokens(true);
		filter.setTFTransform(false);
		filter.setIDFTransform(false);
		filter.setMinTermFreq(1);
		try {
			filter.setInputFormat(data);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}


		try {
			Instances newdata = Filter.useFilter(data, filter);
			System.out.println("Bag of Words amaituta");
			return newdata;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			return null;
		}
		
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Arff2bow a = new Arff2bow();

		Instances newdata= null;
		try {
			newdata= a.bagOfWords(ArffKargatu.instantziakIrakurri("train.arff"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArffKargatu.arffSortu("bow.arff", newdata);

	}

}
