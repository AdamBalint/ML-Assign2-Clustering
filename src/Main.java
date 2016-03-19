import java.awt.Point;
import java.util.ArrayList;

public class Main {

	public Main (){
		FileParser fp = new FileParser("Input/s1.txt");
		fp.readFile();
		KMeans km = new KMeans(15, KMeans.DistType.CHEBYSHEV);
		km.addData(fp.getData());
		km.initializeCentroids();
		km.run();
	}
	
	
	public static void main(String[] args) {
		// TArrayList<E>generated method stub
			new Main();
		
	/*	ArrayList<Point> p = new ArrayList<Point>();
		Point p2 = new Point (2,2);
		Point p3 = new Point (2,2);
		p.add(p2);
		
		System.out.println("Contains: " + p.contains(p3));*/
	}

}
