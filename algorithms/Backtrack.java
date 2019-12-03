package algorithms;

import java.util.HashMap;

import algorithms.components.Solution;

public class Backtrack extends Algorithm {
	
	Solution X;
	Solution OptX;
	int OptP;

	private void backtrack(int length, int weight) {
		Solution tempSol = new Solution(this.dataset.N);
		tempSol.copyOf(X);
		listOfSolutionsFound.add(tempSol);
		if(length == dataset.N) {
			if(computeProfit(X) > OptP) {
				OptP = computeProfit(X);
				OptX.copyOf(X);
			}
		} else {
			if(weight + items.get(length).weight <= dataset.G) {
				X.set(length,1);
				backtrack(length+1, weight+items.get(length).weight);
				X.set(length,0);
				backtrack(length+1, weight);
			} else {
				X.set(length,0);
				backtrack(length+1, weight);
			}
		}
	}
	
	@Override
	public void solve() {
		parameters = new HashMap<>();
		preRun();
		X = new Solution(dataset.N);
		OptX = new Solution(dataset.N);
		OptP = 0;
		long startTime = System.nanoTime();
		backtrack(0,0);
		long stopTime = System.nanoTime();
		printSolution(stopTime - startTime, OptX, "Backtracking");
	}

	@Override
	public void solve(String exportPath) {
		parameters = new HashMap<>();
		preRun();
		X = new Solution(dataset.N);
		OptX = new Solution(dataset.N);
		OptP = 0;
		long startTime = System.nanoTime();
		backtrack(0,0);
		long stopTime = System.nanoTime();
		printSolution(stopTime - startTime, OptX, "Backtracking");
		exportSolution(exportPath, stopTime - startTime, OptX, "Backtracking");
	}

	@Override
	public void setParameters(double[] parameters) {	}

}
