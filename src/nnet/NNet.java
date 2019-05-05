package nnet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * A simple implementation of a neural network.
 * 
 * Comprises a number of neurons, an adjacency matrix, a weight matrix, and a
 * neuron activation vector.
 * 
 * To speed things up a little, for each neuron, its input and output neurons are cached.
 * 
 * Please be careful with the number of neurons vs. quadratic space consumtion! 
 * This implementation is better suited for small networks.
 */
public class NNet implements NN {

	public NNet(int neuronCount) {
		this.neuronCount = neuronCount;
		this.adjacenceMat = new boolean[neuronCount][neuronCount];
		this.weightMat = new float[neuronCount][neuronCount];
		this.activations = new float[neuronCount];
		
		this.getInputNeuronsCache = new HashMap<Integer, List<Integer>>();
		this.getOutputNeuronCache = new HashMap<Integer, List<Integer>>();
	}

	@Override
	public int neuronCount() {
		return neuronCount;
	}

	@Override
	public void connect(int neuron1, int neuron2, float weight) {
		adjacenceMat[neuron1][neuron2] = true;
		weightMat[neuron1][neuron2] = weight;
		
		this.clearCaches();
	}

	@Override
	public void disconnect(int neuron1, int neuron2) {
		adjacenceMat[neuron1][neuron2] = false;
		weightMat[neuron1][neuron2] = 0;
		
		this.clearCaches();
	}

	@Override
	public boolean connected(int n1, int n2) {
		return adjacenceMat[n1][n2];
	}

	@Override
	public float getWeight(int n1, int n2) {
		return weightMat[n1][n2];
	}

	@Override
	public void setWeight(int n1, int n2, float w) {
		if (connected(n1, n2)) {
			weightMat[n1][n2] = w;
		}
	}

	@Override
	public void randomlyInitWeights(float min, float max) {
		float interval = max - min;
		for (int i = 0; i < neuronCount; i++) {
			for (int j = 0; j < neuronCount; j++) {
				if (adjacenceMat[i][j]) {
					weightMat[i][j] = min + (float) Math.random() * interval;
				}
			}
		}
	}

	@Override
	public void randomlyInitActivations(float min, float max) {
		float interval = max - min;
		for (int i = 0; i < neuronCount; i++) {
			activate(i, min + (float) Math.random() * interval);
		}
	}

	@Override
	public int[] inputNeurons() {
		ArrayList<Integer> inputNeurons = new ArrayList<Integer>();
		for (int i = 0; i < neuronCount; i++) {
			if (isInputNeuron(i)) {
				inputNeurons.add(new Integer(i));
			}
		}
		
		int[] ret = new int[inputNeurons.size()];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = (inputNeurons.get(i)).intValue();
		}
		return ret;
	}

	@Override
	public boolean isInputNeuron(int n) {
		boolean result = true;
		for (int i = 0; i < neuronCount; i++) {
			if (adjacenceMat[i][n] == true){
				result = false;
				break;
			}				
		}
		return result;
	}

	@Override
	public int[] getInputNeuronsFor(int neuron) {
		
		if(getInputNeuronsCache.containsKey(neuron)){								
			List<Integer> inputNeurons = getInputNeuronsCache.get(neuron);
			return toArray(inputNeurons);
		}
		
		List<Integer> inputNeurons = new ArrayList<Integer>();
		for (int i = 0; i < neuronCount; i++) {
			if (connected(i, neuron)){
				inputNeurons.add(new Integer(i));
			}
		}
		getInputNeuronsCache.put(neuron, inputNeurons);
		
		return toArray(inputNeurons);
	}
	
		
	@Override
	public int[] getOutputNeuronsFor(int neuron) {
		
		if(getOutputNeuronCache.containsKey(neuron)){
			List<Integer> outputNeurons = getOutputNeuronCache.get(neuron);
			return toArray(outputNeurons);
		}
		
		List<Integer> outputNeurons = new ArrayList<Integer>();
		for (int i = 0; i < neuronCount; i++) {
			if (connected(neuron, i)){
				outputNeurons.add(new Integer(i));
			}
		}
		getOutputNeuronCache.put(neuron, outputNeurons);
		
		return toArray(outputNeurons);
	}

	@Override
	public void clearActivations() {
		for (int i = 0; i < neuronCount; i++) {
			activations[i] = 0;
		}
	}

	@Override
	public void activateInputNeurons(float[] inputValues) {
		int[] inputNeurons = inputNeurons();
		for (int i = 0; i < inputNeurons.length; i++) {
			activate(inputNeurons[i], inputValues[i]);
		}
	}

	public void activateAll(float[] inputValues) {
		for (int i = 0; i < neuronCount; i++) {
			activate(i, inputValues[i]);
		}
	}

	@Override
	public void activate(int neuron, float activation) {
		activations[neuron] = activation;
	}

	@Override
	public float getActivation(int neuron) {
		return activations[neuron];
	}

	@Override
	public float[] getActivations() {
		float[] act = new float[neuronCount];
		for (int i = 0; i < neuronCount; i++) {
			act[i] = getActivation(i);
		}
		return act;
	}


	@Override
	public float getInputActivation(int neuron) {
		float sum = 0;
		for (int j = 0; j < neuronCount; j++) {
			if (adjacenceMat[j][neuron]) {
				sum += weightMat[j][neuron] * getActivation(j);
			}
		}
		return sum;
	}

	/**
	 * Returns the weighted sums of activations going into each neuron.
	 * 
	 * This array represents the neural net's next state.
	 */
	@Override
	public float[] getInputActivations() {
		float[] as = new float[neuronCount];
		for (int i = 0; i < neuronCount; i++) {
			as[i] = getInputActivation(i);
		}

		int[] inputNeurons = inputNeurons();
		for (int i = 0; i < inputNeurons.length; i++) {
			as[inputNeurons[i]] = getActivation(inputNeurons[i]);
		}

		return as;
	}

	public void sigmoidShift() {
		float[] a = getInputActivations();
		for (int i = 0; i < neuronCount; i++) {
			activate(i, sigmoid(a[i]));
		}
	}

	public void tanhShift() {
		float[] a = getInputActivations();
		for (int i = 0; i < neuronCount; i++) {
			activate(i, tanh(a[i]));
		}
	}

	public void shift() {
		float[] a = getInputActivations();
		for (int i = 0; i < neuronCount; i++) {
			activate(i, a[i]);
		}
	}

	public NN copy() {
		NNet copy = new NNet(neuronCount);
		for (int i = 0; i < neuronCount; i++) {
			for (int j = 0; j < neuronCount; j++) {
				copy.adjacenceMat[i][j] = adjacenceMat[i][j];
				copy.weightMat[i][j] = weightMat[i][j];
			}
		}
		return copy;
	}

	public static float sigmoid(float val) {
		return (float) (1 / (1 + Math.pow(2.718, -val)));
	}

	public static float tanh(float x) {
		double powBig = Math.pow(2.718, x);
		double powSml = Math.pow(2.718, -x);
		return (float) ((powBig - powSml) / (powBig + powSml));
	}
	
	public void clearCaches(){	
		getInputNeuronsCache.clear();
		getOutputNeuronCache.clear();
	}
	
	private int[] toArray(List<Integer> listOfIntegers){
		int[] result = new int[listOfIntegers.size()];
		for (int i = 0; i < result.length; i++) {
			result[i] = listOfIntegers.get(i).intValue();
		}			
		return result;
	}
		
	private Map<Integer, List<Integer>> getInputNeuronsCache;
	private Map<Integer, List<Integer>> getOutputNeuronCache;
	
	private boolean[][] adjacenceMat;
	private float[][] weightMat;
	private float[] activations;
	private int neuronCount;
}
