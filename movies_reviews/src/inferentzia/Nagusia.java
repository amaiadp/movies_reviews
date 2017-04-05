package inferentzia;

import movies_reviews.ArffKargatu;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Instances;

public class Nagusia {

	public static void main(String[] args) {
		Inferentzia inf = new Inferentzia();
		try {
			Instances train = ArffKargatu.instantziakIrakurri(args[0]);
			Instances dev = ArffKargatu.instantziakIrakurri(args[1]);
			inf.inferentzia(train, dev, args[2]);
			Instances traindev = inf.sortuTrainDev(train, dev);
			NaiveBayes nb = new NaiveBayes();
			nb.buildClassifier(traindev);
			inf.ebaluatu(nb, traindev);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
	}
}
