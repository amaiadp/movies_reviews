package inferentzia;

import movies_reviews.ArffKargatu;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Instances;

public class Nagusia {

	public static void main(String[] args) {
		if(args.length !=3){
			System.out.println("Pasatutako parametroak okerrak dira.");
			System.out.println("Honako agindu hau erabili.");
			System.out.println("java -jar Inferentzia.jar /path/to/train.arff /path/to/dev.arff /path/to/model");
			System.out.println();
			System.exit(-1);
		}
		
		try {
			Instances train = ArffKargatu.instantziakIrakurri(args[0]);
			int classindex = ParametroEkorketa.lortuKlaseMinoritarioa(train);
			Instances dev = ArffKargatu.instantziakIrakurri(args[1]);
			Inferentzia.inferentzia(train, dev, args[2],classindex);
			Inferentzia.inferentziaNB(train, dev, args[2], classindex);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
	}
}
