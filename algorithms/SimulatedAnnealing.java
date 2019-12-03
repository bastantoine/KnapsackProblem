package algorithms;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

import algorithms.components.Parameter;
import algorithms.components.Solution;

public class SimulatedAnnealing extends Algorithm {

	double initialTemperature, coolingRatio, tempLimit;

	public SimulatedAnnealing() {
		setParameters(0, 0, 0);
	}

	public SimulatedAnnealing(double initialTemperature, double tempLimit, double coolingRatio) {
		setParameters(initialTemperature, tempLimit, coolingRatio);
	}

	@Override
	public void setParameters(double[] parameters) {
		if (parameters == null || parameters.length == 0)
			setParameters(0, 0, 0);
		else if (parameters.length == 1)
			setParameters(parameters[0], 0, 0);
		else if (parameters.length == 2)
			setParameters(parameters[0], parameters[1], 0);
		else
			setParameters(parameters[0], parameters[1], parameters[2]);
	}

	public void setParameters(double initialTemperature, double tempLimit, double coolingRatio) {
		this.initialTemperature = initialTemperature;
		this.tempLimit = tempLimit;
		this.coolingRatio = coolingRatio;
		parameters = new HashMap<>();
		parameters.put("initialTemperature", new Parameter(initialTemperature, "Initial temperature"));
		parameters.put("tempLimit", new Parameter(tempLimit, "Temperature limit"));
		parameters.put("coolingRatio", new Parameter(coolingRatio, "Cooling ratio"));
	}

	public double temperature(double currentTemp) {
		return currentTemp * coolingRatio;
	}

	private Solution simulatedAnnealing() {
		double currentTemp = this.initialTemperature;
		Solution optimum = selectRandomSolution();
		Random rand = new Random();
		while (currentTemp > tempLimit) {
			this.addFoundSolution(optimum);
			List<Solution> neighbourhood = getNeighborhood(optimum);
			Solution solution = null;
			boolean nextSolutionSelected = false;
			int numberOfTriedSolutions = 0;
			while (!nextSolutionSelected && numberOfTriedSolutions < neighbourhood.size()) { // to make sure that the randomly selected solution is acceptable
				numberOfTriedSolutions++;
				int index = rand.nextInt(neighbourhood.size());
				solution = neighbourhood.get(index);
				if (computeWeight(solution) <= this.dataset.G)
					nextSolutionSelected = true;
			}
			if (solution == null) { // in case there's no acceptable solutions in the neighborhood
				break;
			}
			double delta = score(solution) - score(optimum);
			if (delta <= 0)
				optimum = solution;
			else {
				if (rand.nextDouble() <= Math.exp((-1) * delta / currentTemp))
					optimum = solution;
			}
			currentTemp = temperature(currentTemp);
		}
		return optimum;
	}

	@Override
	public void solve() {
		// listOfSolutionsFound = new ArrayList<Solution>();
		preRun();
		long startTime = System.nanoTime();
		Solution xBest = simulatedAnnealing();
		long stopTime = System.nanoTime();
		printSolution(stopTime - startTime, xBest, "Simulated annealing");
	}

	@Override
	public void solve(String exportPath) {
		// listOfSolutionsFound = new ArrayList<Solution>();
		preRun();
		long startTime = System.nanoTime();
		Solution xBest = simulatedAnnealing();
		long stopTime = System.nanoTime();
		printSolution(stopTime - startTime, xBest, "Simulated annealing");
		exportSolution(exportPath, stopTime - startTime, xBest, "Simulated annealing");
	}

}
