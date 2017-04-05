package movies_reviews;

import weka.core.Instances;

public class Nagusia {

	public static void main(String[] args) {
		
		if(args.length !=3){
			System.out.println("Pasatutako parametroak okerrak dira.");
			System.out.println("Honako agindu hau erabili.");
			System.out.println("java -jar Preprocess.jar /path/to/train /path/to/dev /path/to/test_blind");
			System.out.println();
			System.exit(-1);
		}
		Directory2arff d2a = new Directory2arff();
		Instances train = d2a.d2arff(args[0]);
		Instances dev = d2a.d2arff(args[1]);
		Instances test = d2a.blind(args[2]);
		
		Instances trainbow = Arff2bow.bagOfWords(train);
		Instances devbow = Arff2bow.bagOfWords(dev);
		Instances testbow = Arff2bow.bagOfWords(test);
		
		Instances trainFSS = FssInfoGain.fssInfoGain(trainbow);
		
	}

}
