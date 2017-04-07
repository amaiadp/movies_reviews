package movies_reviews;


import java.util.ArrayList;

import weka.core.Instances;
import weka.core.tokenizers.WordTokenizer;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;
import weka.filters.unsupervised.instance.SparseToNonSparse;

public class Arff2bow {

	
	public static Instances bagOfWords(Instances data){
		System.out.println(data.relationName() + " fitxategiaren Bag of Words lortzen...");
		StringToWordVector filter = new StringToWordVector();
		filter.setWordsToKeep(Integer.MAX_VALUE);
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
	
	public static ArrayList<Instances> bagOfWords(Instances train, Instances dev, Instances test){
		Instances denak = new Instances(train);
		denak.addAll(dev);
		denak.addAll(test);
		ArrayList<Instances> erantzuna = new ArrayList<Instances>();
		
		StringToWordVector filter = new StringToWordVector();
		filter.setWordsToKeep(Integer.MAX_VALUE);
		filter.setOutputWordCounts(false);
		filter.setLowerCaseTokens(true);
		filter.setTFTransform(false);
		filter.setIDFTransform(false);
		filter.setMinTermFreq(1);
		try {
			filter.setInputFormat(denak);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		

		Instances databow=null;
		
		try {
			databow = Filter.useFilter(denak, filter);
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
		
		Instances newTrain = new Instances(dataNS, 0, train.numInstances());
		Instances newDev = new Instances(dataNS,  train.numInstances(),dev.numInstances());
		Instances newTest = new Instances(dataNS, train.numInstances()+dev.numInstances(), test.numInstances());
		
		erantzuna.add(newTrain);
		erantzuna.add(newDev);
		erantzuna.add(newTest);
		return erantzuna;
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Arff2bow a = new Arff2bow();

		Instances newdata= null;
		try {
			newdata= a.bagOfWords(ArffKargatu.instantziakIrakurri("devHASIERA.arff"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArffKargatu.arffSortu("devbow.arff", newdata);

	}

}
