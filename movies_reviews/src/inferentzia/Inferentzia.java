package inferentzia;

import java.util.ArrayList;
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
			ebaluatu(rf, train, dev,  traindev, clas);
			rf.buildClassifier(traindev);
			sortuModeloa(rf, "RandomForest.model");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void inferentziaNB(Instances train, Instances dev, Instances traindev, int clas){
		NaiveBayes nb = new NaiveBayes();
		ArffKargatu.arffSortu("traindev.arff", traindev);
		try {
			System.out.println(nb.toString());
			ebaluatuNB(train, dev, traindev, clas);
			nb.buildClassifier(traindev);
			sortuModeloa(nb, "NaiveBayes.model");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void ebaluatuNB(Instances train, Instances dev, Instances traindev, int clas) {
		try {
			System.out.println("Klase minoritarioa: "+ clas);
			NaiveBayes nb = new NaiveBayes();
			System.out.println("NaiveBayes Ez-Zintzoa:");
			ezzintzoa(nb, traindev, clas);
			nb = new NaiveBayes();
			System.out.println("NaiveBayes 10 Cross-Validation:");
			tencross(nb, traindev, clas);
			System.out.println("NaiveBayes Hold Out:");
			holdoutNB(traindev, clas);
			nb = new NaiveBayes();
			System.out.println("NaiveBayes Hold Out (Train vs. Dev)");
			holdouttvsd(nb, train, dev, clas);
			 
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
	
	public static void ebaluatu(RandomForest cl, Instances train, Instances dev, Instances traindev, int clas){
		try {
			System.out.println("Klase minoritarioa: "+ clas);
			RandomForest rf = new RandomForest();
			rf.setOptions(cl.getOptions());
			System.out.println("RandomForest Ez Zintzoa:");
			ezzintzoa(rf, traindev, clas);
			rf = new RandomForest();
			rf.setOptions(cl.getOptions());
			System.out.println("RandomForest 10 Cross-Validation:");
			tencross(rf, traindev, clas);
			System.out.println("RandomForest Hold Out:");
			holdout(traindev, clas, cl.getOptions());
			rf = new RandomForest();
			rf.setOptions(cl.getOptions());
			System.out.println("NaiveBayes Hold Out (Train vs. Dev)");
			holdouttvsd(rf, train, dev, clas);
			 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void holdouttvsd(Classifier cl, Instances train, Instances dev, int clas){
		try{
			ArrayList<Double> fm0 = new ArrayList<Double>();
			ArrayList<Double> fm1 = new ArrayList<Double>();
			ArrayList<Double> fmw = new ArrayList<Double>();
			cl.buildClassifier(train);
			for(int i = 0; i>100; i++){
				Evaluation eval= new Evaluation(train);
				eval.evaluateModel(cl, dev);
				 fm0.add(eval.fMeasure(0));
			     fm1.add(eval.fMeasure(1));
			     fmw.add(eval.weightedFMeasure());
			}
			inprimatu2(fm0,fm1,fmw);
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	private static Double getMean(ArrayList<Double> data){
        Double sum = 0.0;
        for(Double a : data)
            sum += a;
        return sum/data.size();
    }

    private static Double getVariance(ArrayList<Double> data, Double mean){
        double temp = 0;
        for(double a :data)
            temp += (a-mean)*(a-mean);
        return temp/data.size();
    }
    
    private static double getStdDev(double var){
        return Math.sqrt(var);
    }
	
	private static void holdout(Instances data, int clas, String[] opt){
		ArrayList<Double> fm0 = new ArrayList<Double>();
		ArrayList<Double> fm1 = new ArrayList<Double>();
		ArrayList<Double> fmw = new ArrayList<Double>();
		RandomForest rf;
		try{
			for(int i= 1;i<=100; i++ ){
				//Hold-out
				rf = new RandomForest();
				rf.setOptions(opt);
				System.out.println("Hold out egiten Random("+(i+1)+")...");
				data.randomize(new Random(i+1));
				RemovePercentage rp = new RemovePercentage();
				rp.setInputFormat(data);
				rp.setPercentage(70);
				rp.setInvertSelection(true);
				Instances train = Filter.useFilter(data, rp);
				rp = new RemovePercentage();
		        rp.setPercentage(70);
		        rp.setInputFormat(data);
		        Instances test = Filter.useFilter(data, rp);
		        rf.buildClassifier(train);
		        Evaluation eval = new Evaluation(train);
		        eval.evaluateModel(rf, test);
		        fm0.add(eval.fMeasure(0));
		        fm1.add(eval.fMeasure(1));
		        fmw.add(eval.weightedFMeasure());
			}
			inprimatu2(fm0,fm1,fmw);
			
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	private static void holdoutNB(Instances data, int clas){
		ArrayList<Double> fm0 = new ArrayList<Double>();
		ArrayList<Double> fm1 = new ArrayList<Double>();
		ArrayList<Double> fmw = new ArrayList<Double>();
		NaiveBayes nb;
		try{
			for(int i= 1;i<=100; i++ ){
				//Hold-out
				nb = new NaiveBayes();
				System.out.println("Hold out egiten Random("+(i+1)+")...");
				data.randomize(new Random(i+1));
				RemovePercentage rp = new RemovePercentage();
				rp.setInputFormat(data);
				rp.setPercentage(70);
				rp.setInvertSelection(true);
				Instances train = Filter.useFilter(data, rp);
				rp = new RemovePercentage();
		        rp.setPercentage(70);
		        rp.setInputFormat(data);
		        Instances test = Filter.useFilter(data, rp);
		        nb.buildClassifier(train);
		        Evaluation eval = new Evaluation(train);
		        eval.evaluateModel(nb, test);
		        fm0.add(eval.fMeasure(0));
		        fm1.add(eval.fMeasure(1));
		        fmw.add(eval.weightedFMeasure());
			}
			inprimatu2(fm0,fm1,fmw);
			
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	private static void inprimatu2(ArrayList<Double> fm0, ArrayList<Double> fm1, ArrayList<Double> fmw){
		double media = getMean(fm0);
		double var = getVariance(fm0, media);
		double des = getStdDev(var);
		System.out.println("Fmeasure(0)-ren bataz bestekoa: "+media);
		System.out.println("Fmeasure(0)-ren desbiderapena: "+ des);
		media = getMean(fm1);
		var= getVariance(fm1, media);
		des = getStdDev(var);
		System.out.println("Fmeasure(1)-ren bataz bestekoa: "+media);
		System.out.println("Fmeasure(1)-ren desbiderapena: "+ des);
		media = getMean(fmw);
		var= getVariance(fmw, media);
		des = getStdDev(var);
		System.out.println("Weighted Fmeasure-ren bataz bestekoa: "+media);
		System.out.println("WeightedFmeasure-ren desbiderapena: "+ des+"\n");
	}
	
	private static void tencross(Classifier cl, Instances data, int clas) {
		Evaluation eval;
		try {
			ArrayList<Double> fm0 = new ArrayList<Double>();
			ArrayList<Double> fm1 = new ArrayList<Double>();
			ArrayList<Double> fmw = new ArrayList<Double>();
			cl.buildClassifier(data);
			for (int i = 1; i<=100; i++ ){
				eval = new Evaluation(data);
				System.out.println("10 fold cross validation egiten Random("+(i+1)+")...");
				eval.crossValidateModel(cl, data, 10, new Random(i+1));
				fm0.add(eval.fMeasure(0));
		        fm1.add(eval.fMeasure(1));
		        fmw.add(eval.weightedFMeasure());
			}
			inprimatu2(fm0,fm1,fmw);
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
			inprimatu(eval,clas);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void inprimatu(Evaluation eval, int clas) {
		System.out.println("F-measure: " +eval.fMeasure(clas));
		System.out.println("Precision: "+ eval.precision(clas));
		System.out.println("Recall: " +eval.recall(clas));
		System.out.println("Acc: "+eval.pctCorrect()+"\n");
		
	}
	public static void main(String[] args) {
		try {
			Instances train = ArffKargatu.instantziakIrakurri(args[0]);
			int clas = ParametroEkorketa.lortuKlaseMinoritarioa(train);
			Instances dev = ArffKargatu.instantziakIrakurri(args[1]);
			Instances traindev = Inferentzia.sortuTrainDev(train, dev);
			Inferentzia.inferentziaNB(train, dev, traindev, clas);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
