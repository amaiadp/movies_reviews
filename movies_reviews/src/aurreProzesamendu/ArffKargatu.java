package aurreProzesamendu;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import weka.core.Instances;
import weka.core.converters.ArffSaver;

public class ArffKargatu {

	
	public static Instances instantziakIrakurri(String path) throws Exception{
		FileReader fi = new FileReader(path);
		Instances data = new Instances(fi);	
		return data;
	}
	
	public static void arffSortu(String dest, Instances data){
		ArffSaver saver = new ArffSaver();
		saver.setInstances(data);
		try {
			saver.setFile(new File(dest));
			saver.writeBatch();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
