package movies_reviews;

import weka.core.Instances;

public class Bateratzailea {

	
	public void bateratu(Instances train, Instances dev, Instances test){
		
		Instances guztiak = new Instances(train);
		guztiak.addAll(dev);
		guztiak.addAll(test);
		
		Instances guztiakBow = Arff2bow.bagOfWords(guztiak);
		Instances guztiakIG = FssInfoGain.fssInfoGain(guztiakBow, null);
		
		Instances newTrain = new Instances(guztiakIG, 0, train.numInstances());
		Instances newDev = new Instances(guztiakIG, train.numInstances(), dev.numInstances());
		Instances newTest = new Instances(guztiakIG, train.numInstances()+ dev.numInstances(), test.numInstances());

		ArffKargatu.arffSortu("TRAIN.aff", newTrain);
		ArffKargatu.arffSortu("DEV.aff", newDev);
		ArffKargatu.arffSortu("TEST.aff", newTest);
	}
	
	
	
	
	
	public static void main(String[] args) {
		Bateratzailea b = new Bateratzailea();
		b.bateratu(ArffKargatu.instantziakIrakurri("train.arff"), ArffKargatu.instantziakIrakurri("dev.arff"), ArffKargatu.instantziakIrakurri("test_blind.arff"));
	}

}
