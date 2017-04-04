package movies_reviews;

import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.supervised.attribute.AttributeSelection;
//import weka.attributeSelection.AttributeSelection;
import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.Ranker;

public class FssInfoGain {
	
	protected void fssInfoGain(Instances data, String path){
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
			ArffKargatu.arffSortu(path, newData);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		FssInfoGain infog = new FssInfoGain();
		try {
			infog.fssInfoGain(ArffKargatu.instantziakIrakurri(args[0]), args[1]);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}