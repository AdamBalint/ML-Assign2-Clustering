import java.awt.Point;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class KMeans {

	//set up distance measures
	public enum DistType {
		EUCLIDEAN, CHEBYSHEV, MANHATTAN
	}
	
	private ArrayList<Point> data;
	private int numCentroids;
	private ArrayList<Point> centroids;
	private ArrayList<ArrayList<Point>> centroidBuckets;
	private DistType distType; 
	private ArrayList<Double> dunnDist = new ArrayList<Double>();
	private String dataSet = "";
	private double averageDunn;
	
	public KMeans(){
		this(10, DistType.EUCLIDEAN);
	}
	public KMeans(DistType distType){
		this(10, distType);
	}
	
	//set up algorithm
	public KMeans(int centroidNum, DistType distType){
		centroids = new ArrayList<Point>();
		numCentroids = centroidNum;
		this.distType = distType;
		//set up buckets
		initBuckets();
	}
	
	
	//set up the initial centroids
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
	
	//add data to use
	public void addData(ArrayList<Point> data){
		this.data = data;
	}
	
	//run the algorithm
	public void run(){
		
		//runs until no update to centroids
		boolean update = true;
		while (update){
			update = false;
			initBuckets();
			for (Point p : data){
				int closestCentroid = 0;
				double closestDistance = Double.MAX_VALUE;
				//groups the data into the clusters
				for (int i = 0; i < centroids.size(); i++){
					double dist = getDistance(p, centroids.get(i));
					
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
				//recalculates the location of the centroids
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
			
			//compares new centroids to old ones
			update = compareCentroids(newCentroids);
			if (update){
				centroids = newCentroids;
				System.out.println(centroids.toString());
			}
			System.out.println("Update: " + update);
			
		}
		
		//calculates the max distance in a cluster between 2 data points
		double max = 0, min = Double.MAX_VALUE;
		for (int i = 0; i < centroidBuckets.size(); i++){
			for (int j = 0; j < centroidBuckets.get(i).size()-1; j++){
				for (int k = j+1; k < centroidBuckets.get(i).size(); k++){
					double dist = getDistance(centroidBuckets.get(i).get(j), centroidBuckets.get(i).get(k));
					if (dist > max)
						max = dist;
				}
			}
		}
		//calculates the minimum distance between 2 data points in different clusters
		for (int i = 0; i < centroidBuckets.size()-1; i++){
			for (int j = i+1; j < centroidBuckets.size(); j++){
				for (int k = 0; k < centroidBuckets.get(i).size(); k ++){
					for (int l = 0; l < centroidBuckets.get(j).size(); l++){
						double dist = getDistance(centroidBuckets.get(i).get(k), centroidBuckets.get(j).get(l));
						if (dist < min)
							min = dist;
						}
				}
			}
		}
		//calculates the Dunn distance
		dunnDist.add(min/max);
		
		//write out final classification
		writeResults();
		
	}
	
	//returns the distance based on the distance measure
	private double getDistance(Point p, Point centroid) {
		// TODO Auto-generated method stub
		
		double dist = Double.MAX_VALUE;
		switch (distType){
		case EUCLIDEAN:
			dist = Math.sqrt(Math.pow(p.getX() - centroid.getX(),2)+Math.pow(p.getY()- centroid.getY(),2));
			break;
		case CHEBYSHEV:
			dist = Math.max(Math.abs(p.getX() - centroid.getX()), Math.abs(p.getY() - centroid.getY()));
			break;
		case MANHATTAN:
			dist = Math.abs(p.getX() - centroid.getX()) + Math.abs(p.getY() - centroid.getY());
			break;
		}
		
		return dist;
	}
	
	//compares the previous and current centroid locations
	private boolean compareCentroids (ArrayList<Point> nCentroids){
		for (Point nc : nCentroids){
			if (!centroids.contains(nc))
				return true;
		}
		return false;
	}
	
	
	//writes the results of the run to 3 files (classification, centroid location and summary file)
	private void writeResults(){
		
		String prefix = dataSet + "_" + centroidBuckets.size() + "_" + distType + "_";
		String timestamp = ((Long)System.nanoTime()).toString();
		try {
			FileOutputStream fout = new FileOutputStream (new File ("Output/" + prefix + timestamp + "-Classification.txt"));
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
			
			fout = new FileOutputStream (new File ("Output/" + prefix + timestamp + "-Centroids.txt"));
			bw = new BufferedWriter(new OutputStreamWriter(fout));
			for (int i = 0; i < centroids.size(); i++){
				Point p = centroids.get(i);	
				bw.write(p.getX() + " " + p.getY() + " " + i);
				bw.newLine();
			}
			bw.flush();
			bw.close();
			
			fout = new FileOutputStream (new File ("Output/" + prefix + timestamp + "-Info.txt"));
			bw = new BufferedWriter(new OutputStreamWriter(fout));
			bw.write("Distance Type: " + distType);
			bw.newLine();
			bw.write("Centroids: " + centroidBuckets.size());
			bw.newLine();
			double avg = 0;
			for (int i = 0; i < dunnDist.size(); i++){
				//Point p = centroids.get(i);	
				bw.write(i + ":\t" + dunnDist.get(i));
				bw.newLine();
				avg += dunnDist.get(i);
			}
			averageDunn = (avg/dunnDist.size());
			bw.write("Average: \t" + averageDunn);
			bw.newLine();
			bw.flush();
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//sets up the initial clusters
	private void initBuckets(){
		centroidBuckets = new ArrayList<ArrayList<Point>>();
		for (int i = 0; i < numCentroids; i++){
			ArrayList<Point> tmp = new ArrayList<Point>();
			centroidBuckets.add(tmp);
		}		
	}
	
	//sets the data
	public void setDataSet(String s) {
		dataSet = s;
	}

	public double getAverageDunn(){
		return averageDunn;
	}
	
}
