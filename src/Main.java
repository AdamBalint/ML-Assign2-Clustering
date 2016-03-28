import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class Main {

	public Main (){
		//FileParser fp = new FileParser("Input/s1.txt");
		//FileParser fp = new FileParser("Input/s2.txt");
		//FileParser fp = new FileParser("Input/s3.txt");
		FileParser fp = new FileParser("Input/s4.txt");
		
		
		fp.readFile();
		
		ArrayList<Double> averageRun = new ArrayList<Double>();
		
		KMeans.DistType dtype = KMeans.DistType.MANHATTAN;
		//KMeans.DistType dtype = KMeans.DistType.EUCLIDEAN;
		//KMeans.DistType dtype = KMeans.DistType.CHEBYSHEV;
		int kval = 10;
		//String dataSet = "s1";
		//String dataSet = "s2";
		//String dataSet = "s3";
		String dataSet = "s4";
		int runTimes = 20;
		
		for (int i = 0; i < runTimes; i++){
			KMeans km = new KMeans(kval, dtype);
			km.addData(fp.getData());
			km.setDataSet(dataSet);
			km.initializeCentroids();
			km.run();
			averageRun.add(km.getAverageDunn());
		}
		
		PrintWriter writer;
		try {
			writer = new PrintWriter("Run_Average_" + dataSet + "_"+ kval + "_" +dtype + ".txt", "UTF-8");
			writer.println("Total Average:\t" + average(averageRun));
			for (int i = 0; i < averageRun.size(); i++){
				writer.println("Run " + i + ":\t" + averageRun.get(i));
			}
			
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	private double average(ArrayList<Double> in){
		double tot = 0;
		for (int i = 0; i < in.size(); i++){
			tot += in.get(i);
		}
		
		return tot / in.size();
	}
	
	public static void main(String[] args) {
			new Main();
	}
}
