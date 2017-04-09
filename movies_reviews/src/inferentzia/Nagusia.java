package inferentzia;

import movies_reviews.ArffKargatu;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Instances;

public class Nagusia {

	public static void main(String[] args) {
		if(args.length !=2){
			System.out.println("Pasatutako parametroak okerrak dira.");
			System.out.println("Honako agindu hau erabili.");
			System.out.println("java -jar GetModel.jar /path/to/train.arff /path/to/dev.arff");
			System.out.println();
			System.exit(-1);
		}
		
		try {
			Instances train = ArffKargatu.instantziakIrakurri(args[0]);
			train.setClass(train.attribute("klasea"));
			int classindex = ParametroEkorketa.lortuKlaseMinoritarioa(train);
			Instances dev = ArffKargatu.instantziakIrakurri(args[1]);
			dev.setClass(dev.attribute("klasea"));
			Instances traindev = Inferentzia.sortuTrainDev(train, dev);
			Inferentzia.inferentzia(train, dev,traindev, classindex);
			traindev = Inferentzia.sortuTrainDev(train, dev);
			Inferentzia.inferentziaNB(train, dev, traindev, classindex);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
	}
}
