package inferentzia;

import java.util.Random;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.instance.RemovePercentage;

public class Inferentzia {

	public static void inferentzia(Instances train, Instances dev, String model, int clas){
		RandomForest params = ParametroEkorketa.parametroEkorketa(train, dev);
		String[] opt = params.getOptions();
		System.out.println("Sortuko den modeloaren parametroak: ");
		for (String op:opt){
			System.out.println(op);
		}
		Instances train_dev = sortuTrainDev(train,dev);
		RandomForest rf = new RandomForest();
		try {
			rf.setOptions(opt);
			rf.buildClassifier(train_dev);
			ebaluatu(rf, train_dev, clas);
			sortuModeloa(rf, model);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public static void inferentziaNB(Instances train, Instances dev, String model, int clas){
		NaiveBayes nb = new NaiveBayes();
		Instances traindev = sortuTrainDev(train, dev);
		try {
			nb.buildClassifier(traindev);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sortuModeloa(nb, model);
		ebaluatu(nb, traindev, clas);
	}
	
	public static Instances sortuTrainDev(Instances train, Instances dev){
		Instances traindev = new Instances(train);
		for (int i = 0; i<dev.numInstances(); i++){
			traindev.add(dev.instance(i));
		}
		return traindev;
	}
	
	public static void sortuModeloa(Classifier cls, String model){
		try {
			 weka.core.SerializationHelper.write(model, cls);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	public static void ebaluatu(Classifier cl, Instances data, int clas){
		Evaluation eval;
		data.setClassIndex(data.numAttributes()-1);
		try {
			
			//Ez-zintzoa
			eval = new Evaluation(data);
			eval.evaluateModel(cl, data);
			inprimatu("ez-zintzoa",eval,data, clas);
			
			//10-cross validation
			eval = new Evaluation(data);
			eval.crossValidateModel(cl, data, 10, new Random(4));
			inprimatu("10-cross validation",eval,data, clas);
			
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
			inprimatu("hold-out",eval,data, clas);	 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private static void inprimatu(String string, Evaluation eval, Instances data, int clas) {
		System.out.println("F-measure: " +eval.fMeasure(clas));
		System.out.println("Precision: "+ eval.precision(clas));
		System.out.println("Recall: " +eval.recall(clas));
		
	}
}
