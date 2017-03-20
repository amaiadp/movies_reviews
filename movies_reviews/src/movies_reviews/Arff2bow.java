package movies_reviews;


import weka.core.Instances;
import weka.core.tokenizers.WordTokenizer;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class Arff2bow {

	
	public void bagOfWords(Instances data){
		StringToWordVector filter = new StringToWordVector();
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
			ArffKargatu.arffSortu("bow.arff", newdata);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Arff2bow a = new Arff2bow();
		try {
			a.bagOfWords(ArffKargatu.instantziakIrakurri("dev.arff"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
