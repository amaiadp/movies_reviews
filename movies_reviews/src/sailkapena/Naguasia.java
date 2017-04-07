package sailkapena;

public class Naguasia {

	public static void main(String[] args) {
		if(args.length !=2){
			System.out.println("Pasatutako parametroak okerrak dira.");
			System.out.println("Honako agindu hau erabili.");
			System.out.println("java -jar Classify.jar /path/to/model.model /path/to/test.arff");
			System.out.println();
			System.exit(-1);
		}
		else{
			try {
				Sailkatzailea.sailkatu(args[0], args[1]);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
