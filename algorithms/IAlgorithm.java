package algorithms;

import dataset.Dataset;

public interface IAlgorithm {

	public void setDataset(Dataset dataset);
	public void setParameters(double[] parameters);
	public void solve();
	public void solve(String exportPath);
	
}
