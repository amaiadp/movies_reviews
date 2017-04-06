package movies_reviews;

import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;
import weka.filters.unsupervised.attribute.Reorder;

public class Bateratzailea {

	
//	public static Instances bateratu(Instances train, Instances ptest){
//		train.setClass(train.attribute("klasea"));
//		ptest.setClass(ptest.attribute("klasea"));
//
//		Instances test = new Instances(ptest);
//		test.setClass(test.attribute("klasea"));
//
//		System.out.println("Fitxategiak bateratzen...");
//		System.out.println("Hasierako atributu koprurua: train " + train.numAttributes() + " eta test " + test.numAttributes());
//		int i =0;
//		while(i<test.numAttributes()){ //trainean ez daudenak kendu
//			if(train.attribute(test.attribute(i).name())==null){
//				test.deleteAttributeAt(i);
//			}else{
//				i++;
//			}
//		}
//		
//		for(int j=0;j<train.numAttributes();j++){
//			if( test.attribute(train.attribute(j).name()) == null ){
//				test.insertAttributeAt(train.attribute(j), j );
//			}
//		}
//		int[] atributuak = new int[train.numAttributes()];
//		for (int a = 0;a<test.numAttributes();a++) {
//			for(int b=0;b<train.numAttributes();b++){
//				if(train.attribute(b).name().equals(test.attribute(a).name())){
//						atributuak[a] = b;
//				}
//			}
//		}
//		Reorder ro = new Reorder();
//		Instances newtest = null;
//		try {
//			ro.setAttributeIndicesArray(atributuak);
//			ro.setInputFormat(test);
//			newtest = Filter.useFilter(test, ro);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		System.out.println("Amaierako atributu koprurua: train " + train.numAttributes() + " eta test " + newtest.numAttributes());
//
//		return newtest;
//	}
	
	public static Instances bateratu(Instances train, Instances ptest){
		Remove remove = new Remove();
		remove.setAttributeIndicesArray(FssInfoGain.selectedAttributes); 
		remove.setInvertSelection(true); 

		try {
			remove.setInputFormat(ptest);
			return Filter.useFilter(ptest, remove);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	
	
	public static void main(String[] args) {
		Bateratzailea b = new Bateratzailea();
	}

}
