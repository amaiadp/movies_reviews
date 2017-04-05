package inferentzia;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.Random;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.instance.Randomize;
import weka.filters.unsupervised.instance.RemovePercentage;

public class Inferentzia {

	public static void inferentzia(Instances train, Instances dev, String model){
		RandomForest params = ParametroEkorketa.parametroEkorketa(train, dev);
		int numtrees = params.getNumTrees();
		int depth = params.getMaxDepth();
		int seed = params.getSeed();
		System.out.println("Sortuko den modeloaren parametroak: ");
		System.out.println("     NumTrees: "+numtrees);
		System.out.println("     MaxDepth: "+depth);
		Instances train_dev = sortuTrainDev(train,dev);
		sortuModeloa(train_dev, numtrees, depth, seed, model);

	}
	
	public static Instances sortuTrainDev(Instances train, Instances dev){
		Instances traindev = new Instances(train);
		for (int i = 0; i<dev.numInstances(); i++){
			traindev.add(dev.instance(i));
		}
		return traindev;
	}
	
	public static void sortuModeloa(Instances data, int nt, int d, int s, String model){
		RandomForest rf = new RandomForest();
		rf.setMaxDepth(d);
		rf.setNumTrees(nt);
		rf.setSeed(s);
		try {
			rf.buildClassifier(data);
			 weka.core.SerializationHelper.write(model, rf);
			ebaluatu(rf, data);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void ebaluatu(Classifier cl, Instances data){
		Evaluation eval;
		data.setClassIndex(data.numAttributes()-1);
		try {
			
			//Ez-zintzoa
			eval = new Evaluation(data);
			eval.evaluateModel(cl, data);
			eval.toSummaryString();
			
			//10-cross validation
			eval = new Evaluation(data);
			eval.crossValidateModel(cl, data, 10, new Random(4));
			eval.toSummaryString();
			
			//Hold-out
			data.randomize(new Random(4));
//			int trainSize = (int) Math.round(data.numInstances() * 0.7);
//			int testSize = data.numInstances() - trainSize;
//			Instances train = new Instances(data, 0, trainSize);
//			Instances test = new Instances(data, trainSize, testSize);
			//train
			RemovePercentage rp = new RemovePercentage();
			rp.setInputFormat(data);
			rp.setPercentage(70);
			rp.setInvertSelection(true);
			Instances train = Filter.useFilter(data, rp);
			//test
			rp = new RemovePercentage();
	        rp.setPercentage(70);
	        rp.setInputFormat(data);
	        Instances test = Filter.useFilter(data, rp);
	        eval = new Evaluation(train);
	        eval.evaluateModel(cl, test);
	        eval.toSummaryString();
	        
	
			 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}
