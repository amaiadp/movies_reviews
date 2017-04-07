package inferentzia;

import java.util.Random;

import movies_reviews.ArffKargatu;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.instance.RemovePercentage;

public class Inferentzia {

	public static void inferentzia(Instances train, Instances dev, Instances traindev, int clas){
		RandomForest params = ParametroEkorketa.parametroEkorketa(train, dev, clas);
		String[] opt = params.getOptions();
		System.out.println("Sortuko den modeloaren parametroak: ");
		ParametroEkorketa.inprimatumk(opt);
		RandomForest rf = new RandomForest();
		try {
			rf.setOptions(opt);
			ebaluatu(rf, traindev, clas);
			rf.buildClassifier(traindev);
			sortuModeloa(rf, "RandomForest.model");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void inferentziaNB(Instances traindev, int clas){
		NaiveBayes nb = new NaiveBayes();
		ArffKargatu.arffSortu("traindev.arff", traindev);
		try {
			System.out.println(nb.toString());
			ebaluatuNB(traindev, clas);
			nb.buildClassifier(traindev);
			sortuModeloa(nb, "NaiveBayes.model");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void ebaluatuNB(Instances traindev, int clas) {
		Evaluation eval;
		try {
			System.out.println("Klase minoritarioa: "+ clas);
			NaiveBayes nb = new NaiveBayes();
			System.out.println("NaiveBayes ezzintzoa:");
			ezzintzoa(nb, traindev, clas);
			nb = new NaiveBayes();
			System.out.println("NaiveBayes 10 cross:");
			tencross(nb, traindev, clas);
			nb = new NaiveBayes();
			System.out.println("NaiveBayes hold out:");
			holdout(nb,traindev, clas);
			
			 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
		

	public static Instances sortuTrainDev(Instances train, Instances dev){
		Instances traindev = new Instances(train);
		for (int i = 0; i<dev.numInstances(); i++){
			traindev.add(dev.instance(i));
		}
		traindev.setClass(traindev.attribute("klasea"));
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
	
	public static void ebaluatu(RandomForest cl, Instances traindev, int clas){
		try {
			System.out.println(cl.toString());
			System.out.println("Klase minoritarioa: "+ clas);
			RandomForest rf = new RandomForest();
			rf.setOptions(cl.getOptions());
			System.out.println("RandomForest ezzintzoa:");
			ezzintzoa(rf, traindev, clas);
			rf = new RandomForest();
			rf.setOptions(cl.getOptions());
			System.out.println("RandomForest 10 cross validation:");
			tencross(rf, traindev, clas);
			rf = new RandomForest();
			rf.setOptions(cl.getOptions());
			System.out.println("RandomForest hold out:");
			holdout(rf,traindev, clas);
			
			 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private static void holdout(Classifier cl, Instances data, int clas){
		try{
			//Hold-out
			data.randomize(new Random(4));
			RemovePercentage rp = new RemovePercentage();
			rp.setInputFormat(data);
			rp.setPercentage(70);
			rp.setInvertSelection(true);
			Instances train = Filter.useFilter(data, rp);
			rp = new RemovePercentage();
	        rp.setPercentage(70);
	        rp.setInputFormat(data);
	        Instances test = Filter.useFilter(data, rp);
	        cl.buildClassifier(train);
	        Evaluation eval = new Evaluation(train);
	        eval.evaluateModel(cl, test);
			inprimatu("hold-out",eval,data, clas);	
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	private static void tencross(Classifier cl, Instances data, int clas) {
		Evaluation eval;
		try {
			eval = new Evaluation(data);
			cl.buildClassifier(data);
			eval.crossValidateModel(cl, data, 10, new Random(4));
			inprimatu("10-cross validation",eval,data, clas);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//10-cross validation
		
		
	}

	private static void ezzintzoa(Classifier cl, Instances data, int clas){
		Evaluation eval;
		try {
			eval = new Evaluation(data);
			cl.buildClassifier(data);
			eval.evaluateModel(cl, data);
			inprimatu("ez-zintzoa",eval,data, clas);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void inprimatu(String string, Evaluation eval, Instances data, int clas) {
		System.out.println("F-measure: " +eval.fMeasure(clas));
		System.out.println("Precision: "+ eval.precision(clas));
		System.out.println("Recall: " +eval.recall(clas));
		System.out.println("Acc: "+eval.pctCorrect());
		
	}
	public static void main(String[] args) {
		try {
			Instances train = ArffKargatu.instantziakIrakurri(args[0]);
			int clas = ParametroEkorketa.lortuKlaseMinoritarioa(train);
			Instances dev = ArffKargatu.instantziakIrakurri(args[1]);
			Instances traindev = Inferentzia.sortuTrainDev(train, dev);
			Inferentzia.inferentziaNB(traindev, clas);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
