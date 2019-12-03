package algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import algorithms.components.Parameter;
import algorithms.components.Solution;

public class TabuSearch extends Algorithm {

	class TabuStructure {

		class Pair<K, V> {

			K key;
			V value;

			public Pair(K left, V right) {
				this.key = left;
				this.value = right;
			}

		}

		int tenure;
		List<Pair<Integer, Integer>> structure; // the key is the index of the solution and the value is the tenure of this solution
		TabuSearch ts = TabuSearch.this;

		public TabuStructure(int tenure) {
			this.tenure = tenure;
			this.structure = new ArrayList<>();
		}
		
		private boolean contains(Solution solution) {
			for(Pair<Integer,Integer> pair : this.structure) {
				if(pair.key == solution.index())
					return true;
			}
			return false;
		}

		public void add(Solution solution) {
			int indexSolution = solution.index();
			if(!contains(solution))
				structure.add(new Pair<Integer, Integer>(indexSolution, tenure));
			else { // for the aspiration criteria
				for(Pair<Integer,Integer> pair : structure) {
					if(pair.key == solution.index())
						pair.value = tenure;
				}
			}
		}

		public void decreaseAll() {
			List<Pair<Integer, Integer>> structureBuffer = new ArrayList<>();
			for (Pair<Integer, Integer> pair : structure) {
				if (pair.value > 1) {
					pair.value--;
					structureBuffer.add(pair);
				}
			}
			structure = structureBuffer;
		}

		private Solution convertIndexToSolution(int number) {
			String index = Integer.toBinaryString(number);
			Solution sol = new Solution(ts.dataset.N);
			if(index.length() < ts.dataset.N) { // in case the binary number does not fill the entire vector, we put zeros at the beginning
				for(int i = 0; i < ts.dataset.N - index.length(); i++)
					sol.add(0);
			}
			for (char character : index.toCharArray())
				sol.add(Integer.valueOf(character));
			return sol;
		}

		public boolean isTabu(Solution sol) {
			for (Pair<Integer, Integer> pair : this.structure) {
				if (pair.key == sol.index()) // if the solution is tabu, it means that it is in the tabu structure,
											 // so its enough to see if there's a pair whose key is the same as the
											 // index of the solution
					return true;
			}
			return false;
		}
		
		public String toString() {
			String output = "";
			for(Pair<Integer,Integer> pair : structure)
				output += this.convertIndexToSolution(pair.key) + ": " + pair.value;
			return output;
		}

	}

	int tabuTenure;
	int iterationsMax;
	TabuStructure ts;

	public TabuSearch() {
		setParameters(0, 0);
	}

	public TabuSearch(int tabuTenure, int iterationsMax) {
		setParameters(tabuTenure, iterationsMax);
	}

	@Override
	public void setParameters(double[] parameters) {
		if(parameters == null || parameters.length == 0)
			setParameters(0, 0);
		else if(parameters.length == 1)
			setParameters((int) parameters[0], 0);
		else
			setParameters((int) parameters[0], (int) parameters[1]);
	}

	private void setParameters(int tabuTenure, int iterationsMax) {
		this.tabuTenure = tabuTenure;
		this.iterationsMax = iterationsMax;
		parameters = new HashMap<>();
		parameters.put("tabuTenure", new Parameter((double) tabuTenure, "Tabu tenure"));
		parameters.put("iterationsMax", new Parameter((double) iterationsMax, "Max number of iterations"));
	}

	private Solution getBestSolutionInNeighboorhood(Solution currentBest, Solution solution) {
		
		List<Solution> neighborhood = getNeighborhood(solution); 
		SortedMap<Double, Solution> admissibleSolutionsNonTabu = new TreeMap<>();
		SortedMap<Double, Solution> admissibleSolutionsTabu = new TreeMap<>();
		for(Solution sol : neighborhood) {
			if(computeWeight(sol) <= this.dataset.G && score(sol) - score(currentBest) > 0) // the solution is admissible, i.e. it's a solution not overweighted and that leads to a better score than the current best solution
				if(!ts.isTabu(sol))
					admissibleSolutionsNonTabu.put(new Double(this.score(sol)), sol);
				else
					admissibleSolutionsTabu.put(new Double(this.score(sol)), sol);
		}
		if(admissibleSolutionsNonTabu.size() == 0 && admissibleSolutionsTabu.size() == 0) // no solution in the neighborhood was admissible
			return currentBest;
		// Aspiration criteria : if the best tabu solution has a better score than the best non tabu solution, then we return the best tabu solution, otherwise we return the best non tabu solution
		if(admissibleSolutionsTabu.size() == 0) // all the admissible solutions are non tabu
			return admissibleSolutionsNonTabu.get(admissibleSolutionsNonTabu.lastKey());
		Solution bestNonTabu = admissibleSolutionsNonTabu.get(admissibleSolutionsNonTabu.lastKey());
		Solution bestTabu = admissibleSolutionsTabu.get(admissibleSolutionsTabu.lastKey());
		return (score(bestNonTabu) > score(bestTabu)) ? bestNonTabu : bestTabu;
	}
	
	private Solution tabuSearch() {
		ts = new TabuStructure(tabuTenure);
		Solution xBest = selectRandomSolution();
		int numberOfIterations = 0;
		while (numberOfIterations <= this.iterationsMax) {
			ts.decreaseAll();
			this.addFoundSolution(xBest);
			Solution xNow = getBestSolutionInNeighboorhood(xBest, xBest);
			xBest.copyOf(xNow);
			ts.add(xBest);
			numberOfIterations++;
		}
		return xBest;
	}

	@Override
	public void solve() {
		// listOfSolutionsFound = new ArrayList<Solution>();
		preRun();
		long startTime = System.nanoTime();
		Solution xBest = tabuSearch();
		long stopTime = System.nanoTime();
		printSolution(stopTime - startTime, xBest, "Tabu search");
	}

	@Override
	public void solve(String exportPath) {
		// listOfSolutionsFound = new ArrayList<Solution>();
		preRun();
		long startTime = System.nanoTime();
		Solution xBest = tabuSearch();
		long stopTime = System.nanoTime();
		printSolution(stopTime - startTime, xBest, "Tabu search");
		exportSolution(exportPath, stopTime - startTime, xBest, "Tabu search");
	}

}
