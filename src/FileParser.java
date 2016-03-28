import java.awt.Point;
import java.io.*;
import java.util.ArrayList;

//parses the file
public class FileParser {

	private String f;
	private ArrayList<Point> data;
	
	
	public FileParser(String file){
		f = file;
	}
	
	public void setFile(String file){
		f = file;
	}
	
	//reads in the data
	public void readFile(String file){
		data = new ArrayList<Point>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(file)));
			String in = br.readLine();
			while (in != null){
				in = in.trim().replaceAll(" +", " ");
				String[] splitin = in.split(" ");
				data.add(new Point(Integer.parseInt(splitin[0]), Integer.parseInt(splitin[1])));
				in = br.readLine();
			}
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void readFile(){
		readFile(f);
	}
	
	public ArrayList<Point> getData(){
		return data;
	}
	
}
