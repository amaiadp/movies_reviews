package aurreProzesamendu;

import java.util.ArrayList;

import weka.core.Instances;

public class Nagusia {

	public static void main(String[] args) {
		
		if(args.length !=3 || args.length!=4){
			System.out.println("Pasatutako parametroak okerrak dira.");
			System.out.println("Honako agindu hau erabili.");
			System.out.println("java -jar Preprocess.jar /path/to/train /path/to/dev /path/to/test_blind [FSS aukera]");
			System.out.println("FSS aukera: ez bada ezer adierazten InfoGain, beste edozein baliorako TF-IDF");
			System.out.println("Irteerako emaitzak jar-a dagoen tokian sortuko dira.");
			System.exit(-1);
		}
		Directory2arff d2a = new Directory2arff();
		Instances train = d2a.d2arff(args[0]);
		Instances dev = d2a.d2arff(args[1]);
		Instances test = d2a.blind(args[2]);
		
		if(args.length==3){
			ArrayList<Instances> denak = Arff2bow.bagOfWords(train, dev, test);
			
			
			Instances trainbow = denak.get(0);
			Instances devbow = denak.get(1);
			Instances testbow= denak.get(2);
		
		
			Instances trainFSS = FssInfoGain.fssInfoGain(trainbow);
			
			Instances devbat = Bateratzailea.bateratu(trainFSS, devbow);
			Instances testbat = Bateratzailea.bateratu(trainFSS, testbow);
		
			ArffKargatu.arffSortu("train.arff", trainFSS);
			ArffKargatu.arffSortu("dev.arff", devbat);
			ArffKargatu.arffSortu("test.arff", testbat);
		}else{
			
			ArrayList<Instances> denak = FssInfoGain.TFIDF(train, dev, test);
			
			
			Instances trainbow = denak.get(0);
			Instances devbow = denak.get(1);
			Instances testbow= denak.get(2);
		
			ArffKargatu.arffSortu("train.arff", trainbow);
			ArffKargatu.arffSortu("dev.arff", devbow);
			ArffKargatu.arffSortu("test.arff", testbow);
		}
	}

}
