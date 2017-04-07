package movies_reviews;

import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;
import weka.filters.unsupervised.attribute.StringToWordVector;
import weka.filters.unsupervised.instance.SparseToNonSparse;
//import weka.filters.supervised.attribute.AttributeSelection;
import weka.attributeSelection.AttributeSelection;
import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.Ranker;

public class FssInfoGain {
	
	public static int[] selectedAttributes;
	
	public static Instances fssInfoGain(Instances data){
		Ranker rnk = new Ranker();
		InfoGainAttributeEval igattreval = new InfoGainAttributeEval();
		AttributeSelection as = new AttributeSelection();
		rnk.setThreshold(0.0);
		as.setSearch(rnk);
		as.setEvaluator(igattreval);
		Instances newData;
		try {
			data.setClassIndex(0);
			as.SelectAttributes(data);
			selectedAttributes = as.selectedAttributes();
			Remove remove = new Remove();
			remove.setAttributeIndicesArray(selectedAttributes); // atributu onak pasatzen diogu, gorde nahi ditugunak
			remove.setInvertSelection(true); // beraz, invert egitean gordetako atributuak ez ezik, besteak kentzen ditugu.
			remove.setInputFormat(data);
			newData = Filter.useFilter(data, remove);
			return newData;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		
	}
	
	public static Instances TFIDF(Instances data){
		StringToWordVector filter = new StringToWordVector();
		filter.setWordsToKeep(1000);
		filter.setOutputWordCounts(true);
		filter.setLowerCaseTokens(true);
		filter.setTFTransform(true);
		filter.setIDFTransform(true);
		filter.setMinTermFreq(1);
		try {
			filter.setInputFormat(data);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		

		Instances databow=null;
		
		try {
			databow = Filter.useFilter(data, filter);
			System.out.println("Bag of Words amaituta");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		SparseToNonSparse s2np = new SparseToNonSparse();
		Instances dataNS = null;
		try {
			s2np.setInputFormat(databow);
			dataNS = Filter.useFilter(databow, s2np);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dataNS;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		FssInfoGain infog = new FssInfoGain();
		try {
			Instances newData = infog.fssInfoGain(ArffKargatu.instantziakIrakurri(args[0]));
			ArffKargatu.arffSortu(args[1], newData);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}