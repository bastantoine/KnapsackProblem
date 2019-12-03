package dataset;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Scanner;

public class Dataset {
	
	public int G; 						// the maximum weight the knapsack can fit
	public int N; 						// the number of available items
	public ArrayList<Integer> g;		// the array containing the weights
	public ArrayList<Integer> v;		// the array containing the values
	
	public Dataset() {
		defaultInitilization();
	}
	
	public Dataset(int G, int N) {
		defaultInitilization();
		this.G = G;
		this.N = N;
	}
	
	public Dataset(String fileName) {
		defaultInitilization();
		read2(fileName);
	}
	
	public Dataset(int numberOfItems, double meanWeight, double stdDeviationWeight, double meanValue, double stdDeviationValue) {
		this.N = numberOfItems;
		this.g = new ArrayList<Integer>();
		this.v = new ArrayList<Integer>();
		this.randomGeneration(numberOfItems, meanWeight, stdDeviationWeight, meanValue, stdDeviationValue);
	}
	
	private void defaultInitilization() {
		G = 0;
		N = 0;
		g = new ArrayList<Integer>();
		v = new ArrayList<Integer>();
	}
		
	public void addData(int weight, int value) {
		if(this.g.size() <= this.N) {
			this.g.add(weight);
			this.v.add(value);
		}
	}
	
	@Deprecated
	public void read(String fileName) {
		String outputFile = "";
		try(FileReader fileReader = new FileReader(fileName)) {
		    int ch = fileReader.read();
		    while(ch != -1) {
		    	outputFile += String.valueOf((char)ch);
		        ch = fileReader.read();
		    }
			String[] datas = outputFile.split("-");
			this.G = Integer.valueOf(datas[0]);
			this.N = Integer.valueOf(datas[1]);
			for(int i = 2; i < datas.length; i++) {
				String[] object = datas[i].split("");
				String weight = "";
				String value = "";
				boolean afterSplit = false;
				for(String str : object) {
					if(str.equals("|"))
						afterSplit = true;
					else {
						if(afterSplit)
							value += str;
						else
							weight += str;
					}
						
				}
				this.g.add(Integer.valueOf(weight));
				this.v.add(Integer.valueOf(value));
			}
		} catch (FileNotFoundException e) {
			System.out.println("Error file not found");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Error while opening, reading or closing the file");
			e.printStackTrace();
		} catch (NumberFormatException e) {
			System.out.println("Error on the format of the datas");
			e.printStackTrace();
		}
	}
	
	public void read2(String fileName) {
		try {
			Scanner sc = new Scanner(new File(fileName));
			
			this.G = sc.nextInt();
			this.N = sc.nextInt();
			while(sc.hasNext()) {
				int weight = sc.nextInt();
				int value = sc.nextInt();
				this.g.add(weight);
				this.v.add(value);
			}
			sc.close();
		} catch (FileNotFoundException e) {
			System.out.println("Error file not found");
			e.printStackTrace();
		}
	}
	
	public void save(String fileName) {
		String output = this.G + "\n" + this.N;
		for(int i = 0; i < this.g.size(); i++) {
			output = output + "\n" + String.valueOf(this.g.get(i)) + " " + String.valueOf(this.v.get(i));
		}
		try(FileWriter fileWriter = new FileWriter(fileName)) {
		    fileWriter.write(output);
		} catch (IOException e) {
			System.out.println("Error while opening, writing on or closing the file");
			e.printStackTrace();
		}
	}

	private void randomGeneration(int numberOfItems, double meanWeight, double stdDeviationWeight, double meanValue, double stdDeviationValue) {
		Random rand = new Random();
		int weight, value;
		for(int i = 0; i < numberOfItems; i++) {
			weight = (int) (rand.nextGaussian() * Math.sqrt(stdDeviationWeight) + meanWeight);
			value = (int) (rand.nextGaussian() * Math.sqrt(stdDeviationValue) + meanValue);
			this.addData(weight, value);
		}
		int a = rand.nextInt(numberOfItems) + 1;
		this.G = (int) (meanWeight * a);
	}
	
}