import java.awt.Point;
import java.util.ArrayList;
import java.util.concurrent.Callable;

public class CalculateAverage implements Callable<Point>{

	private ArrayList<Point> data;
	
	public CalculateAverage(ArrayList<Point> bucket){
		data = bucket;
	}
	
	
	//calculates the average x and y of the cluster
	@Override
	public Point call() throws Exception {
		// TODO Auto-generated method stub
		int sumx = 0, sumy = 0;
		
		for (Point p : data){
			sumx += p.getX();
			sumy += p.getY();
		}
		int num = data.size();
		
		return new Point(sumx/num, sumy/num);
	}

	
	
}
