package aurreProzesamendu;

import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;
import weka.filters.unsupervised.attribute.StringToWordVector;
import weka.filters.unsupervised.instance.SparseToNonSparse;

import java.util.ArrayList;

//import weka.filters.supervised.attribute.AttributeSelection;
import weka.attributeSelection.AttributeSelection;
import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.Ranker;

public class FssInfoGain {
	
	public static int[] selectedAttributes;
	
	public static Instances fssInfoGain(Instances data){
		data.setClass(data.attribute("klasea"));
		Ranker rnk = new Ranker();
		InfoGainAttributeEval igattreval = new InfoGainAttributeEval();
		AttributeSelection as = new AttributeSelection();
		rnk.setThreshold(0.0);
		rnk.setNumToSelect(data.numInstances()/10);
		as.setSearch(rnk);
		as.setEvaluator(igattreval);
		Instances newData;
		try {
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
	
	public static ArrayList<Instances> TFIDF(Instances train, Instances dev, Instances test){
		train.setClass(train.attribute("klasea"));
		dev.setClass(dev.attribute("klasea"));
		test.setClass(test.attribute("klasea"));
		Instances denak = new Instances(train);
		denak.addAll(dev);
		denak.addAll(test);
		ArrayList<Instances> erantzuna = new ArrayList<Instances>();
		
		
		StringToWordVector filter = new StringToWordVector();
		filter.setDoNotOperateOnPerClassBasis(true);
		filter.setWordsToKeep(denak.numInstances()/10);
		filter.setOutputWordCounts(true);
		filter.setLowerCaseTokens(true);
		filter.setTFTransform(true);
		filter.setIDFTransform(true);
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

		
	}
}