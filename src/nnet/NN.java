package nnet;

/**
 * A neural network having a number of neurons and weighted inter-connections.
 * Neurons can be activated and can get activations from other incoming neurons.
 */
public interface NN {

	int neuronCount();

	void connect(int neuron1, int neuron2, float weight);

	void disconnect(int neuron1, int neuron2);

	boolean connected(int n1, int n2);

	float getWeight(int n1, int n2);

	void setWeight(int n1, int n2, float w);

	void randomlyInitWeights(float min, float max);

	void randomlyInitActivations(float min, float max);

	int[] inputNeurons();

	boolean isInputNeuron(int n);

	int[] getInputNeuronsFor(int neuron);

	int[] getOutputNeuronsFor(int neuron);

	void clearActivations();

	void activateInputNeurons(float[] inputValues);

	void activateAll(float[] inputValues);

	void activate(int neuron, float activation);

	float getActivation(int neuron);

	float[] getActivations();

	float getInputActivation(int neuron);

	float[] getInputActivations();

}