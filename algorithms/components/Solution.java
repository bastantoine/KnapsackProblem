package algorithms.components;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Solution {

	private List<Integer> vector;
	
	public Solution(int size) {
		vector = new ArrayList<Integer>();
		for(int i = 0; i < size; i++) {
			vector.add(0);
		}
	}
	
	public String toString() {
		String output = "";
		for(int value : this.vector)
			output += value + " ";
		return output;
	}
	
	public int get(int index) {
		return this.vector.get(index);
	}
	
	public void set(int index, int value) {
		this.vector.set(index, value);
	}
	
	public void add(int value) {
		this.vector.add(value);
	}
	
	public int size() {
		return this.vector.size();
	}
	
	public void copyOf(Solution solution) {
		Collections.copy(this.vector, solution.vector);
	}
	
	public boolean equals(Solution solution) {
		return (this.index() == solution.index());
	}
	
	public int index() {
		int index = 0;
		for(int i = 0; i < this.vector.size(); i++) {
			index += Math.pow(2, i) * this.vector.get(this.vector.size()-1-i);
		}
		return index;
	}
	
}
