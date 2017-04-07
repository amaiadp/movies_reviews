package inferentzia;
import weka.classifiers.trees.RandomForest;
import weka.classifiers.meta.CVParameterSelection;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

import java.util.Enumeration;
import java.util.Random;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;

public class ParametroEkorketa {

	public static RandomForest parametroEkorketa(Instances train, Instances dev, int clas){
		RandomForest max = new RandomForest();
		double maxfm = -1;
		double fm;
		
		train.setClassIndex(train.numAttributes()-1);		
		
		for (double m = 10; m<=100; m=m+10 ){
			for (int attr = 20; attr <=train.numAttributes()/2; attr=attr+20){
				RandomForest orain = new RandomForest();
				try {
					String[] options = weka.core.Utils.splitOptions("-M "+m+" -K "+attr);
					orain.setOptions(options);
					orain.setSeed(4);
					orain.buildClassifier(train);
					fm = ebaluatu(orain, train, dev, clas);
					if(fm>maxfm){
						max = orain;
						maxfm = fm;
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
		return max;
	}
	
	public static int lortuKlaseMinoritarioa(Instances train){
		int[] klas = new int[2];
		Double clas;
		train.setClassIndex(train.numAttributes()-1);;
		for(Instance i: train){
			clas = i.classValue();
			klas[clas.intValue()] = klas[clas.intValue()] + 1;
		}
		if(klas[0]<klas[1]){
			return 1;
		}
		return 0;
	}
	
	public static double ebaluatu(RandomForest classifier, Instances train, Instances dev, int clas){
		try {
			System.out.println("Evaluating the Classifier with these options: ");
			System.out.println(classifier.toString());
			train.setClassIndex(train.numAttributes()-1);
			dev.setClassIndex(dev.numAttributes()-1);
			Evaluation eval= new Evaluation(train);
			eval.evaluateModel(classifier, dev);
			double fm =eval.fMeasure(clas) ;
			System.out.println("F-measure: "+fm);
			return fm ;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}
	
	
}
