package movies_reviews;

import weka.core.Instances;

public class Bateratzailea {

	
	public static Instances bateratu(Instances train, Instances ptest){
		Instances test = new Instances(ptest);
		System.out.println("Fitxategiak bateratzen...");
		System.out.println("Hasierako atributu koprurua: " + train.numAttributes() + " eta " + test.numAttributes());
		int i =0;
		while(i<test.numAttributes()){ //trainean ez daudenak kendu
			if(train.attribute(test.attribute(i).name())==null){
				test.deleteAttributeAt(i);
			}else{
				i++;
			}
		}
		
		for(int j=0;j<train.numAttributes();j++){
			if( test.attribute(train.attribute(j).name()) == null ){
				test.insertAttributeAt(train.attribute(j), j );
			}
		}
		System.out.println("Amaierako atributu koprurua: train " + train.numAttributes() + " eta test" + test.numAttributes());

		return test;
	}
	
	
	
	
	
	public static void main(String[] args) {
		Bateratzailea b = new Bateratzailea();
	}

}
