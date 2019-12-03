import dataset.Dataset;
import algorithms.IAlgorithm;
import algorithms.Backtrack;
import algorithms.NeighborhoodSearch;
import algorithms.TabuSearch;
import algorithms.SimulatedAnnealing;
import algorithms.Genetic;

/**
 * 
 * The main class, where to import the datasets and run the algorithms
 * 
 * @author bastien
 * 
 * @see Dataset
 * @see algorithms.Algorithm
 *
 */
public class Main {

	private static Dataset randomDs8, randomDs10, randomDs50, randomDs100;
	private static String path = "~/";
	
	/**
	 * 
	 * The main method, where to import the datasets and run the algorithms
	 * 
	 * @see Dataset
	 * @see algorithms.Algorithm
	 * 
	 */
	public static void main(String[] args) {
		
		loadDS();
		
		IAlgorithm algo = new SimulatedAnnealing(5000, 300, 0.95);
		
		algo.setDataset(randomDs8);
		for(int i = 0; i < 10; i++)
			algo.solve(path);
		
		algo.setDataset(randomDs10);
		for(int i = 0; i < 10; i++)
			algo.solve(path);
		
		algo.setDataset(randomDs50);
		for(int i = 0; i < 10; i++)
			algo.solve(path);
		
		algo.setDataset(randomDs100);
		for(int i = 0; i < 10; i++)
			algo.solve(path);

	}
	
	public static void loadDS() {

		randomDs8 = new Dataset(path + "DS8.txt");
		randomDs10 = new Dataset(path + "DS10.txt");
		randomDs50 = new Dataset(path + "DS50.txt");
		randomDs100 = new Dataset(path + "DS100.txt");
		
	}
	
	public static void randomGeneration() {
		
		int numberOfItems;
		double meanWeight = 50;
		double stdDeviationWeight = 7;
		double meanValue = 100;
		double stdDeviationValue = 15;

		numberOfItems = 8;
		randomDs100 = new Dataset(numberOfItems, meanWeight, stdDeviationWeight, meanValue, stdDeviationValue);
		randomDs100.save(path + "DS8.txt");

		numberOfItems = 10;
		randomDs100 = new Dataset(numberOfItems, meanWeight, stdDeviationWeight, meanValue, stdDeviationValue);
		randomDs100.save(path + "DS10.txt");

		numberOfItems = 50;
		randomDs100 = new Dataset(numberOfItems, meanWeight, stdDeviationWeight, meanValue, stdDeviationValue);
		randomDs100.save(path + "DS50.txt");

		numberOfItems = 100;
		randomDs100 = new Dataset(numberOfItems, meanWeight, stdDeviationWeight, meanValue, stdDeviationValue);
		randomDs100.save(path + "DS100.txt");
		
	}

}
