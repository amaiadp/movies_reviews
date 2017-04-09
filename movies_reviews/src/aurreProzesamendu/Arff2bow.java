package aurreProzesamendu;


import java.util.ArrayList;

import weka.core.Instances;
import weka.core.tokenizers.WordTokenizer;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;
import weka.filters.unsupervised.instance.SparseToNonSparse;

public class Arff2bow {

	
	public static Instances bagOfWords(Instances data){
		//Metodo hau ez dugu programa nagusian erabiltzen baina bow ezberdinen datuak lortzeko erabili dugu
		System.out.println(data.relationName() + " fitxategiaren Bag of Words lortzen...");
		StringToWordVector filter = new StringToWordVector();
		filter.setDoNotOperateOnPerClassBasis(true);
		filter.setWordsToKeep(Integer.MAX_VALUE);
		filter.setOutputWordCounts(false);
		filter.setLowerCaseTokens(true);
		filter.setTFTransform(false);
		filter.setIDFTransform(false);
		filter.setMinTermFreq(1);
		try {
			filter.setInputFormat(data);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		

		Instances databow=null;
		
		try {
			databow = Filter.useFilter(data, filter);
			System.out.println("Bag of Words amaituta");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		SparseToNonSparse s2np = new SparseToNonSparse();
		Instances dataNS = null;
		try {
			s2np.setInputFormat(databow);
			dataNS = Filter.useFilter(databow, s2np);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dataNS;
		
	}
	
	public static ArrayList<Instances> bagOfWords(Instances train, Instances dev, Instances test){
		//Datuen hiru multzoak hartu eta BOW aplikatzen du
		// Irteera bezala array bat orden honetan: train, dev, test
		train.setClass(train.attribute("klasea"));
		dev.setClass(dev.attribute("klasea"));
		test.setClass(test.attribute("klasea"));
		Instances denak = new Instances(train);
		denak.addAll(dev);
		denak.addAll(test);
		ArrayList<Instances> erantzuna = new ArrayList<Instances>();
		
		
		StringToWordVector filter = new StringToWordVector();
		filter.setDoNotOperateOnPerClassBasis(true);
		filter.setWordsToKeep(Integer.MAX_VALUE);
		filter.setOutputWordCounts(false);
		filter.setLowerCaseTokens(true);
		filter.setTFTransform(false);
		filter.setIDFTransform(false);
		filter.setMinTermFreq(1);
		try {
			filter.setInputFormat(denak);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		

		Instances databow=null;
		
		try {
			databow = Filter.useFilter(denak, filter);
			System.out.println("Bag of Words amaituta");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		SparseToNonSparse s2np = new SparseToNonSparse();
		Instances dataNS = null;
		try {
			s2np.setInputFormat(databow);
			dataNS = Filter.useFilter(databow, s2np);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Instances newTrain = new Instances(dataNS, 0, train.numInstances());
		Instances newDev = new Instances(dataNS,  train.numInstances(),dev.numInstances());
		Instances newTest = new Instances(dataNS, train.numInstances()+dev.numInstances(), test.numInstances());
		
		erantzuna.add(newTrain);
		erantzuna.add(newDev);
		erantzuna.add(newTest);
		return erantzuna;
		
	}
	
	public static void main(String[] args) {
		if(args.length !=1 || args.length !=3){
			System.out.println("Pasatutako parametroak okerrak dira.");
			System.out.println("Honako agindu hau erabili.");
			System.out.println("Fitxategi bakarra bihurtzeko:");
			System.out.println("java -jar arff2bow.jar /path/to/fitx.arff ");
			System.out.println("Train dev eta test fitxategiak aldi berean bihurtzeko:");
			System.out.println("java -jar arff2bow.jar /path/to/train.arff /path/to/dev.arff /path/to/test.arff");
			System.out.println();
			System.exit(-1);
		}

		if(args.length==1){
			try {
				Instances databow = Arff2bow.bagOfWords(ArffKargatu.instantziakIrakurri(args[0]));
				ArffKargatu.arffSortu("bow.arff", databow);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}else{
			if(args.length==3){
				try {
					Instances train = ArffKargatu.instantziakIrakurri(args[0]);
					Instances dev = ArffKargatu.instantziakIrakurri(args[1]);
					Instances test = ArffKargatu.instantziakIrakurri(args[2]);
					ArrayList<Instances> denak = Arff2bow.bagOfWords(train, dev, test);
					
					Instances trainbow = denak.get(0);
					Instances devbow = denak.get(1);
					Instances testbow= denak.get(2);
					
					ArffKargatu.arffSortu("trainBOW.arff", trainbow);
					ArffKargatu.arffSortu("devBOW.arff", devbow);
					ArffKargatu.arffSortu("testBOW.arff", testbow);

					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			}
			
		}
		


	}

}
