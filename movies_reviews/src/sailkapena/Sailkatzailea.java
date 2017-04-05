package sailkapena;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;

import weka.classifiers.Classifier;
import weka.core.Instances;


public class Sailkatzailea {

	

	public void sailkatu(String pathModel, String pathTest) throws Exception{
		
		//model lortu
		 ObjectInputStream ois = new ObjectInputStream(
                 new FileInputStream(pathModel));
		 Classifier cls = (Classifier) ois.readObject();
		 ois.close();
		 
		 // load unlabeled data
		 Instances unlabeled = new Instances(
		                         new BufferedReader(
		                           new FileReader(pathTest)));
		 
		 // set class attribute
		 unlabeled.setClassIndex(unlabeled.numAttributes() - 1);
		 
		 // create copy
		 Instances labeled = new Instances(unlabeled);
		 
		 // label instances
		 for (int i = 0; i < unlabeled.numInstances(); i++) {
		   double clsLabel = cls.classifyInstance(unlabeled.instance(i));
		   labeled.instance(i).setClassValue(clsLabel);
		 }
		 // save labeled data
		 BufferedWriter writer = new BufferedWriter(
		                           new FileWriter(""));
		 writer.write(labeled.toString());
		 writer.newLine();
		 writer.flush();
		 writer.close();
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
