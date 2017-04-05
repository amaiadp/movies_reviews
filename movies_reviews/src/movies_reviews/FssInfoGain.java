package movies_reviews;

import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.supervised.attribute.AttributeSelection;
//import weka.attributeSelection.AttributeSelection;
import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.Ranker;

public class FssInfoGain {
	
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
			as.setInputFormat(data);
			newData = Filter.useFilter(data, as);
			return newData;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		
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