package aurreProzesamendu;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;

public class Directory2arff {
	
	public Instances d2arff(String source){
		//source-ak barruan bi karpeta: neg eta pos
		//neg eta pos barruan testu fitxategiak
		System.out.println(source + " karpetatik instantziak lortzen...");
		File dir = new File(source);
		ArrayList<Attribute> atts = new ArrayList<>();
		ArrayList<String> klaseBalio = new ArrayList<>();
		klaseBalio.add("neg");
		klaseBalio.add("pos");
	    atts.add(new Attribute("klasea",klaseBalio));
	    atts.add(new Attribute("testua",true));
	    Instances data = new Instances("text_files_in_" + source, atts, 0);
		
		String[] karpetak = dir.list();
//		for(String kar : karpetak){ //ubuntun eta windowsen ez ditu arff berdinak sortzen
//			this.instantziakGehitu(source+File.separator+kar, kar, data);
//		}
		this.instantziakGehitu(source+File.separator+"neg", "neg", data);
		this.instantziakGehitu(source+File.separator+"pos", "pos", data);
		System.out.println("Karga amaituta. "+ data.numInstances()+ " instantzia lortu dira");
		return data;
		
	}
	

	
	
	
	private Instances instantziakGehitu(String source, String klasea, Instances data) {
		//Source karpetan testu fitxategiak egon behar dira.
		//Irteera datari sourceko fitxategiko instantzi bat gehituko zaio klase classearekin
		File karpeta = new File(source);
		int i =0;
		String[] lista = karpeta.list();
		Arrays.sort(lista);
		for(String fiIzena : lista){
			i++;
			double[] newInst = new double[2];
			if(klasea!=null){
				newInst[0] = (double)data.attribute(0).indexOfValue(klasea);
			}
		    try {
		    	File fi = new File(source+File.separator+fiIzena);
		    	InputStreamReader is;
		        is = new InputStreamReader(new FileInputStream(fi));
		        StringBuffer txtStr = new StringBuffer();
		        int c;
		        while ((c = is.read()) != -1) {
		          txtStr.append((char)c);
		        }
		        String text = txtStr.toString();
		        String newstr = text.replaceAll("-", " ");
		        String newstr2 = newstr.replaceAll("[\\x00-\\x1F\\x21-\\x2F\\x3A-\\x40\\x5B-\\x60\\x7B-\\x7F]", "");
		        String zuriGabe = newstr2.replaceAll(" +", " ");
		        newInst[1] = (double)data.attribute(1).addStringValue(zuriGabe);
		        DenseInstance ins =new DenseInstance(1.0, newInst);
		        if(klasea==null){
		        	ins.setMissing(0);
		        }
		        data.add(ins);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
		}
		
		return data;
	}


	public Instances blind(String source){
		//Source karpetan testu fitxategiak daude
		//Irteera: Instances bat non testu bakoitza instantzia bat den eta klasea ezezaguna(?)
		System.out.println(source + " karpetatik instantziak lortzen...");
		File dir = new File(source);
		ArrayList<Attribute> atts = new ArrayList<>();
		ArrayList<String> klaseBalio = new ArrayList<>();
		klaseBalio.add("neg");
		klaseBalio.add("pos");
	    atts.add(new Attribute("klasea",klaseBalio));
	    atts.add(new Attribute("testua",true));
	    Instances data = new Instances("text_files_in_" + source, atts, 0);
		
	    this.instantziakGehitu(source, null, data);
		System.out.println("Karga amaituta. "+ data.numInstances()+ " instantzia lortu dira");
		return data;
	}


	public static void main(String[] args) {
		if(args.length !=2){
			System.out.println("Pasatutako parametroak okerrak dira.");
			System.out.println("Honako agindu hau erabili.");
			System.out.println("java -jar getARFF.jar /path/to/folder <blind>");
			System.out.println("<blind>: blind bada idatzi 1, bestela 0");
			System.out.println();
			System.exit(-1);
		}
		Directory2arff d = new  Directory2arff();

		if(args[1].equals("1")){
			String[] pathZati = args[0].split(File.separator);
			Instances data = d.blind(args[0]);
			ArffKargatu.arffSortu(pathZati[pathZati.length-1], data);
		}else{
			if(args[1].equals("0")){
				String[] pathZati = args[0].split(File.separator);
				Instances data = d.d2arff(args[0]);
				ArffKargatu.arffSortu(pathZati[pathZati.length-1], data);
			}else{
				System.out.println("Pasatutako parametroak okerrak dira.");
				System.out.println("Honako agindu hau erabili.");
				System.out.println("java -jar getARFF.jar /path/to/folder <blind>");
				System.out.println("<blind>: blind bada idatzi 1, bestela 0");
				System.out.println();
				System.exit(-1);
			}
			
		}
	}

}
