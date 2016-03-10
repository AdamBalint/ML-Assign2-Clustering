import java.awt.Point;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class KMeans {

	public enum DistType {
		EUCLIDIAN, CHEBYSHEV, MINKOWSKI
	}
	
	private ArrayList<Point> data;
	private int numCentroids;
	private ArrayList<Point> centroids;
	private ArrayList<ArrayList<Point>> centroidBuckets;
	private DistType distType; 
	
	public KMeans(){
		this(10, DistType.EUCLIDIAN);
	}
	public KMeans(DistType distType){
		this(10, distType);
	}
	public KMeans(int centroidNum, DistType distType){
		centroids = new ArrayList<Point>();
		numCentroids = centroidNum;
		this.distType = distType;
		//set up buckets
		initBuckets();
	}
	
	public void initializeCentroids(){
		Random r = new Random();
		ArrayList<Integer> taken = new ArrayList<Integer>();
		//choose k different points from data
		while (taken.size() != numCentroids){
			int rand = r.nextInt(data.size());
			if (!taken.contains(rand))
				taken.add(rand);
		}
		//set the centroid locations
		for (int i = 0; i < taken.size(); i++){
			centroids.add(data.get(taken.get(i)));
		}
	}
	
	public void addData(ArrayList<Point> data){
		this.data = data;
	}
	
	public void run(){
		boolean update = true;
		while (update){
			update = false;
			initBuckets();
			for (Point p : data){
				int closestCentroid = 0;
				double closestDistance = Double.MAX_VALUE;
				for (int i = 0; i < centroids.size(); i++){
					double dist = Double.MAX_VALUE;
					switch (distType){
					case EUCLIDIAN:
						dist = Math.sqrt(Math.pow(p.getX() - centroids.get(i).getX(),2)+Math.pow(p.getY()- centroids.get(i).getY(),2));
						break;
					case CHEBYSHEV:
						dist = Math.max(Math.abs(p.getX() - centroids.get(i).getX()), Math.abs(p.getY() - centroids.get(i).getY()));
						break;
					case MINKOWSKI:
						break;
					}
					if (dist < closestDistance){
						closestDistance = dist;
						closestCentroid = i;
					}
				}
				centroidBuckets.get(closestCentroid).add(p);
			}
			
			ExecutorService serv;
			Future<Point> ret;
			ArrayList<Point> newCentroids = new ArrayList<Point>();
			serv = Executors.newFixedThreadPool(20);
			
			for (ArrayList<Point> bucket : centroidBuckets){
				
				try {
					ret = serv.submit(new CalculateAverage(bucket));
					Point tmp = ret.get();
					newCentroids.add(tmp);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			serv.shutdown();
			
			update = compareCentroids(newCentroids);
			if (update){
				centroids = newCentroids;
				System.out.println(centroids.toString());
			}
			System.out.println("Update: " + update);
			
		}
		
		writeResults();
		//write out final classification
	}
	
	private boolean compareCentroids (ArrayList<Point> nCentroids){
		for (Point nc : nCentroids){
			if (!centroids.contains(nc))
				return true;
		}
		return false;
	}
	
	private void writeResults(){
		String timestamp = ((Long)System.nanoTime()).toString();
		try {
			FileOutputStream fout = new FileOutputStream (new File ("Output/Classification-" + timestamp + ".txt"));
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fout));
			for (int i = 0; i < centroidBuckets.size(); i++){
				ArrayList<Point> ps = centroidBuckets.get(i);
				for (Point p : ps){
					bw.write(p.getX() + " " + p.getY() + " " + i);
					bw.newLine();
				}
			}
			bw.flush();
			bw.close();
			
			fout = new FileOutputStream (new File ("Output/Centroids-" + timestamp + ".txt"));
			bw = new BufferedWriter(new OutputStreamWriter(fout));
			for (int i = 0; i < centroids.size(); i++){
				Point p = centroids.get(i);	
				bw.write(p.getX() + " " + p.getY() + " " + i);
				bw.newLine();
			}
			bw.flush();
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	private void initBuckets(){
		centroidBuckets = new ArrayList<ArrayList<Point>>();
		for (int i = 0; i < numCentroids; i++){
			ArrayList<Point> tmp = new ArrayList<Point>();
			centroidBuckets.add(tmp);
		}		
	}
	
}
