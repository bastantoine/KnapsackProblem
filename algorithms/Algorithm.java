package algorithms;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import algorithms.components.Item;
import algorithms.components.Solution;
import algorithms.components.Parameter;
import dataset.Dataset;

public abstract class Algorithm implements IAlgorithm {

	protected Dataset dataset;
	protected List<Item> items;
	protected List<Solution> listOfSolutionsFound;
	protected Map<String,Parameter> parameters;
	
	public void setDataset(Dataset dataset) {
		this.dataset = dataset;
		this.items = new ArrayList<Item>();
		computeItemsFromDataset();
	}

	private void computeItemsFromDataset() {
		for (int i = 0; i < this.dataset.g.size(); i++) {
			items.add(new Item(this.dataset.g.get(i), this.dataset.v.get(i)));
		}
	}

	protected int computeProfit(Solution solution) {
		int profit = 0;
		for (int i = 0; i < solution.size(); i++)
			profit += solution.get(i) * items.get(i).value;
		return profit;
	}
	
	protected int computeWeight(Solution solution) {
		int weight = 0;
		for (int i = 0; i < solution.size(); i++)
			weight += solution.get(i) * items.get(i).weight;
		return weight;
	}

	protected double score(Solution solution) {
		return ((double) this.computeProfit(solution));
	}

	protected void addFoundSolution(Solution solution) {
		Solution solCopy = new Solution(this.dataset.N);
		solCopy.copyOf(solution);
		this.listOfSolutionsFound.add(solCopy);
	}

	protected Solution selectRandomSolution() {
		Solution solution = new Solution(this.dataset.N);
		List<Integer> filledPositions = new ArrayList<>();
		boolean isEnoughFilled = false;
		boolean weightOK = true;
		Random rand = new Random();
		while (weightOK & !isEnoughFilled) { // we set to 1 half of the solution while constantly making sure that it's
											 // not overweighted
			while (true) {
				int index = rand.nextInt(this.dataset.N);
				if (this.computeWeight(solution) + this.dataset.g.get(index) <= this.dataset.G
						&& !filledPositions.contains(index)) {
					solution.set(index, 1);
					filledPositions.add(index);
					break;
				} else if (this.computeWeight(solution) + this.dataset.g.get(index) > this.dataset.G) {
					weightOK = false;
					break;
				}
			}
			if (!weightOK)
				break;
			weightOK = weightOK & (this.computeWeight(solution) <= this.dataset.G);
			isEnoughFilled = filledPositions.size() == this.dataset.N / 3;
		}
		return solution;
	}

	protected List<Solution> getNeighborhood(Solution solution) {
		List<Solution> neighborhood = new ArrayList<Solution>();
		for (int i = 0; i < this.dataset.N; i++) {
			Solution tempSol = new Solution(this.dataset.N);
			tempSol.copyOf(solution);
			if (tempSol.get(i) == 1)
				tempSol.set(i, 0);
			else
				tempSol.set(i, 1);
			neighborhood.add(tempSol);
		}
		return neighborhood;
	}
	
	public boolean listOfSolutionsContainsSolution(List<Solution> listOfSolutions, Solution solution) {
		for(Solution sol : listOfSolutions) {
			if(sol.equals(solution))
				return true;
		}
		return false;
	}
	
	protected void preRun() {
		listOfSolutionsFound = new ArrayList<Solution>();
	}
	
	public void solve() {
		System.out.println("The solve method has not been implemented yet");
	}
	
	public void solve(String exportPath) {
		System.out.println("The solve method has not been implemented yet");
	}

	protected void printSolution(long time, Solution solution, String algorithm) {
		String params = "";
		for(Parameter parameter : parameters.values()) {
			params += parameter.getDisplayName() + ": " + parameter.getValue() + "\n";
		}
		System.out.println("---------------------------");
		System.out.println("Knapsack problem\nSolution of the problem with:");
		System.out.println("G = " + this.dataset.G);
		System.out.println("N = " + this.dataset.N);
		System.out.println("Algorithm used: " + algorithm);
		System.out.print(params);
		System.out.println("Solution found: " + solution);
		System.out.println("Weight = " + computeWeight(solution));
		System.out.println("Value = " + computeProfit(solution));
		System.out.println("Time = " + String.valueOf(time / 1000000.0) + " ms");
	}

	protected void exportSolution(String path, long time, Solution solution, String algorithm) {
		String params = "";
		for(Parameter parameter : parameters.values()) {
			params += parameter.getDisplayName() + ": " + parameter.getValue() + "\n";
		}
		String outputStats = "G = " + this.dataset.G + "\nN = " + this.dataset.N + "\nAlgorithm used: " + algorithm + "\n" + params + 
				"\nSolution found: " + solution + "\nWeight = " + computeWeight(solution) + "\nValue = "
				+ computeProfit(solution) + "\nTime = " + String.valueOf(time / 1000000.0) + " ms";

		String outputSolutions = "Solution # - Weight - Value - Score";
		for (int i = 0; i < listOfSolutionsFound.size(); i++) {
			outputSolutions += "\n" + String.valueOf(i) + " - " + computeWeight(listOfSolutionsFound.get(i)) + " - "
					+ computeProfit(listOfSolutionsFound.get(i)) + " - " + score(listOfSolutionsFound.get(i));
		}

		Calendar now = Calendar.getInstance();
		String folderName = String.valueOf(now.get(Calendar.YEAR)) + "-" + String.valueOf(now.get(Calendar.MONTH)+1) + "-" + String.valueOf(now.get(Calendar.DAY_OF_MONTH)) + "-" + String.valueOf(now.get(Calendar.HOUR_OF_DAY)) + ":"+ String.valueOf(now.get(Calendar.MINUTE)) + ":" + String.valueOf(now.get(Calendar.SECOND)) + ":" + String.valueOf(now.get(Calendar.MILLISECOND)) + "-" + this.dataset.N;

		Path outputFolder = Paths.get(path, "outputs", folderName);
		try {
			Files.createDirectories(outputFolder);
			
			FileWriter fw;
			
			fw = new FileWriter(Paths.get(outputFolder.toString(), "log.txt").toString());
			fw.write(outputStats.toCharArray());
			fw.close();
			
			fw = new FileWriter(Paths.get(outputFolder.toString(), "datas.csv").toString());
			fw.write(outputSolutions.toCharArray());
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
