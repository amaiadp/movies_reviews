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
import weka.core.SerializationHelper;


public class Sailkatzailea {

	

	public static void sailkatu(String pathModel, String pathTest) throws Exception{
		
		//model lortu
		Classifier cls = (Classifier) SerializationHelper.read(pathModel);
		 
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
		                           new FileWriter("test.predictions.arff"));
		 writer.write(labeled.toString());
		 writer.newLine();
		 writer.flush();
		 writer.close();
	}

}
