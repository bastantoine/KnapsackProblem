package algorithms;

import java.util.HashMap;
import java.util.List;

import algorithms.components.Parameter;
import algorithms.components.Solution;

public class NeighborhoodSearch extends Algorithm {

	int iterationsMax;
	
	public NeighborhoodSearch() {
		setParameters(0);
	}
	
	public NeighborhoodSearch(int iterationsMax) {
		setParameters(iterationsMax);
	}

	@Override
	public void setParameters(double[] parameters) {
		if(parameters == null || parameters.length == 0)
			setParameters(0);
		else
			setParameters((int) parameters[0]);
	}
	
	public void setParameters(int iterationsMax) {
		this.iterationsMax = iterationsMax;
		parameters = new HashMap<>();
		parameters.put("iterationsMax", new Parameter((double) iterationsMax, "Max number of iterations"));
	}
	
	private Solution neighborhoodSearch() {
		Solution xNow = selectRandomSolution(), xBest = new Solution(this.dataset.N);
		xBest.copyOf(xNow);
		Solution xNext = new Solution(this.dataset.N);
		int bestProfit = computeProfit(xBest), numberOfIterations = 0;
		while(numberOfIterations <= this.iterationsMax) {
			List<Solution> neighborhood = getNeighborhood(xNow);
			xNext.copyOf(xNow);
			int bestProfitNeighborhood = bestProfit;
			for(Solution x : neighborhood) {
				if( (this.computeWeight(x) <= this.dataset.G) && (this.computeProfit(x) > bestProfit) & (this.computeProfit(x) > bestProfitNeighborhood) ) {
					xNext.copyOf(x);
					bestProfitNeighborhood = computeProfit(x);
				}
			}
			Solution tempSol = new Solution(this.dataset.N);
			tempSol.copyOf(xNext);
			listOfSolutionsFound.add(tempSol);
			if(xNext.equals(xNow)) // we didn't find a neighbor that improves the profit, we avoid to stay in a potentially long loop
				break;
			xNow.copyOf(xNext);
			xBest.copyOf(xNext);
			bestProfit = computeProfit(xNext);
			numberOfIterations++;
		}
		return xBest;
	}

	@Override
	public void solve() {
		// listOfSolutionsFound = new ArrayList<Solution>();
		preRun();
		long startTime = System.nanoTime();
		Solution xBest = neighborhoodSearch();
		long stopTime = System.nanoTime();
		printSolution(stopTime - startTime, xBest, "Neighborhood search");
	}

	@Override
	public void solve(String exportPath) {
		// listOfSolutionsFound = new ArrayList<Solution>();
		preRun();
		long startTime = System.nanoTime();
		Solution xBest = neighborhoodSearch();
		long stopTime = System.nanoTime();
		printSolution(stopTime - startTime, xBest, "Neighborhood search");
		exportSolution(exportPath, stopTime - startTime, xBest, "Neighborhood search");
	}
	
}
