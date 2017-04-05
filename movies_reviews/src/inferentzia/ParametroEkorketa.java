package inferentzia;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;

import java.util.Random;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;

public class ParametroEkorketa {

	public static RandomForest parametroEkorketa(Instances train, Instances dev){
		RandomForest max = new RandomForest();
		int numdat;
		double maxfm = -1;
		double fm;
		train.setClassIndex(train.numAttributes()-1);
		for (int i = 2; i<train.numAttributes(); i++ ){
			numdat = train.numInstances()/i;
			for (int depth = 2; depth <numdat; depth++){
				RandomForest orain = new RandomForest();
				orain.setNumTrees(i);
				orain.setMaxDepth(depth);
				orain.setSeed(4);
				try {
					orain.buildClassifier(train);
					fm = ebaluatu(orain, train, dev);
					if(fm>maxfm){
						max = orain;
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
		return max;
	}
	
	public static double ebaluatu(RandomForest classifier, Instances train, Instances dev){
		try {
			Evaluation eval= new Evaluation(dev);
			eval.evaluateModel(classifier, dev);
			return eval.fMeasure(train.classIndex());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}
	
	
}
