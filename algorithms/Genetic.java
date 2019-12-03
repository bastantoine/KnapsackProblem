package algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import algorithms.components.Solution;

public class Genetic extends Algorithm {
	
	private int sizePopulation = 6;
	private int maxIterations = 10;
	private double mutationProbability = 0.1;

	private Solution genetic() {
		System.out.println("Algorithm lauched");
		List<Solution> population = new ArrayList<>();
		for(int i = 0; i < sizePopulation; i++) {
			population.add(selectRandomSolution());
		}
		Solution best = getBestSolutionOfList(population);
		listOfSolutionsFound.add(best);
		int iterations = 0;
		while(iterations < maxIterations) {
			iterations++;
			System.out.println("Iteration " + iterations);
			population.sort((sol0, sol1) -> {
				if(score(sol0) == score(sol1))
					return 0;
				return (score(sol0) - score(sol1)) > 0 ? 1 : -1;
			});
			population = fitnessProportionateSelection(population);
			List<Solution> newPopulation = new ArrayList<>();
			for(int i = 0; i < population.size(); i++) {
				Solution parent1, parent2;
				parent1 = population.get(i);
				if(i == population.size()-1)
					parent2 = population.get(0);
				else
					parent2 = population.get(i+1);
				List<Solution> newOffsprings = recombination(parent1, parent2);
				Solution child1 = mutation(newOffsprings.get(0));
				Solution child2 = mutation(newOffsprings.get(1));
				newPopulation.add(child1);
				newPopulation.add(child2);
				best = getBestSolutionOfList(newPopulation);
				listOfSolutionsFound.add(best);
			}
			population = newPopulation;
		}
		best = getBestSolutionOfList(population);
		listOfSolutionsFound.add(best);
		return best;
	}
	
	private List<Solution> fitnessProportionateSelection(List<Solution> population) { // see https://en.wikipedia.org/wiki/Fitness_proportionate_selection
		double sumFitness = 0;
		for(Solution sol : population) {
			sumFitness += score(sol);
		}
		List<Double> listFitness = new ArrayList<>();
		double previousPropability = 0.0;
		for(Solution sol : population) {
			double probability = previousPropability + (score(sol)/sumFitness);
			listFitness.add(probability);
			previousPropability = probability;
		}
		List<Solution> proportionateNextPopulation = new ArrayList<>();
		for(Solution sol : population) {
			double probability = Math.random();
			int index = 0;
			boolean added = false;
			while(index < listFitness.size()) {
				if(probability < listFitness.get(index)) {
					proportionateNextPopulation.add(population.get(index));
					added = true;
					break;
				}
				index++;
			}
			if(!added)
				proportionateNextPopulation.add(population.get(listFitness.size()));
		}
		return proportionateNextPopulation;
	}
	
	private Solution mutation(Solution solution) {
		Solution mutatedSolution = new Solution(solution.size());
		for(int i = 0; i < solution.size(); i++) {
			if(Math.random() < mutationProbability)
				mutatedSolution.set(i, (solution.get(i) == 1 ? 0 : 1));
			else
				mutatedSolution.set(i, solution.get(i));
		}
		return mutatedSolution;
	}
	
	private List<Solution> recombination(Solution sol1, Solution sol2) {
		Random rand = new Random();
		int crossoverIndex = rand.nextInt(sol1.size());
		Solution newSol1 = new Solution(sol1.size()); // left of sol 1 & right of sol 2
		Solution newSol2 = new Solution(sol1.size()); // left of sol 2 & right of sol 1
		for(int i = 0; i < sol1.size(); i++) {
			if(i <= crossoverIndex) {
				newSol1.set(i, sol1.get(i));
				newSol2.set(i, sol2.get(i));
			} else {
				newSol1.set(i, sol2.get(i));
				newSol2.set(i, sol1.get(i));
			}
		}
		List<Solution> couple = new ArrayList<>();
		couple.add(newSol1);
		couple.add(newSol2);
		return couple;
	}
	
	private Solution getBestSolutionOfList(List<Solution> listOfSolutions) {
		Solution best = listOfSolutions.get(0);
		for(Solution solution : listOfSolutions) {
			if(computeWeight(solution) <= this.dataset.G && score(solution) >= score(best))
				best = solution;
		}
		return best;
	}	

	@Override
	public void solve() {
		listOfSolutionsFound = new ArrayList<>();
		long startTime = System.nanoTime();
		Solution xBest = genetic();
		long stopTime = System.nanoTime();
		printSolution(stopTime - startTime, xBest, "Genetic algorithm");
	}

	@Override
	public void solve(String exportPath) {
		listOfSolutionsFound = new ArrayList<>();
		long startTime = System.nanoTime();
		Solution xBest = genetic();
		long stopTime = System.nanoTime();
		exportSolution(exportPath, stopTime - startTime, xBest, "Genetic algorithm");
	}

	@Override
	public void setParameters(double[] parameters) {}
	
}
