package nnet;

/**
 * A sequential model of a neural network separated into layers:
 * 
 * LInput x LHidden_0 x ... x LHiden_N x LOutput
 * 
 * 
 * Net topology:
 * 
 * Each input neuron is connected to all hidden neurons in layer 0.
 * Each hidden neuron in layer 0 is connected to all hidden neurons in layer 1.
 * ..
 * Each hidden neuron in layer n-1 is connected to all hidden neurons in layer n.
 * Each hidden neuron in layer n is connected to all output neurons.
 * 
 * 
 * Supports training via back-propagation.
 * 
 */
public class LayeredNet extends NNet {

	public LayeredNet(int neuronCount, int[] layers) {
		this(neuronCount, layers, 0);
	}

	public LayeredNet(int neuronCount, int[] layers, float initialWeight) {
		super(neuronCount);
		this.layers = layers;
		int counter = 0;
		for (int i = 0; i < layers.length - 1; i++) {
			int layersize = layers[i];
			int nextLayersize = layers[i + 1];
			for (int j = 0; j < layersize; j++) {
				for (int k = 0; k < nextLayersize; k++) {
					connect(counter + j, counter + layersize + k, initialWeight);
				}
			}
			counter += layersize;
		}
		counter += layers[layers.length-1];
		
		if(counter!=neuronCount){
			throw new IllegalArgumentException("Number of neurons ("+neuronCount+") does not match total number in layers ("+counter+")!");
		}		
	}

	public int hiddenLayers() {
		return (layers.length - 2);
	}

	/**
	 * Returns the output vector for given input vector.
	 */
	public float[] output(float[] input) {
		return output(input, true);
	}

	/**
	 * Returns the output vector for given input vector.
	 */
	public float[] output(float[] input, boolean doSigmoidActivation) {
		activateInputNeurons(input);
		for (int i = 0; i < hiddenLayers(); i++) {
			// compute next state of neuron activations
			if (doSigmoidActivation) {
				sigmoidShift();
			} else {
				shift();
			}
		}
		int[] on = outputNeurons();
		float[] outVals = new float[on.length];
		for (int i = 0; i < on.length; i++) {
			outVals[i] = getInputActivation(on[i]);
		}
		return outVals;
	}

	
	@Override
	public int[] inputNeurons() {
		int[] in = new int[layers[0]];
		for (int i = 0; i < in.length; i++) {
			in[i] = i;
		}
		
		return in;
	}

	public int[] outputNeurons() {
		int[] out = new int[layers[layers.length - 1]];
		int neuronCount = neuronCount();
		for (int i = 0; i < out.length; i++) {
			out[i] = neuronCount - out.length + i;
		}
		
		return out;
	}

	public int[] hiddenNeurons() {
		int hdCount = 0;
		for (int i = 1; i < layers.length - 1; i++) {
			hdCount += layers[i];
		}
		int[] hid = new int[hdCount];
		for (int i = 0; i < hdCount; i++) {
			hid[i] = layers[0] + i;
		}
		
		return hid;
	}

	private int[] layers;

}