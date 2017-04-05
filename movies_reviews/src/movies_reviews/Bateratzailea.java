package movies_reviews;

import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Reorder;

public class Bateratzailea {

	
	public static Instances bateratu(Instances train, Instances ptest){
		Instances test = new Instances(ptest);
		System.out.println("Fitxategiak bateratzen...");
		System.out.println("Hasierako atributu koprurua: train " + train.numAttributes() + " eta test " + test.numAttributes());
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
				test.insertAttributeAt(train.attribute(j), test.numInstances() );
			}
		}
		Reorder ro = new Reorder();
		Instances newtest = null;
		try {
			ro.setInputFormat(train);
			newtest = Filter.useFilter(test, ro);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Amaierako atributu koprurua: train " + train.numAttributes() + " eta test " + newtest.numAttributes());

		return newtest;
	}
	
	
	
	
	
	public static void main(String[] args) {
		Bateratzailea b = new Bateratzailea();
	}

}
