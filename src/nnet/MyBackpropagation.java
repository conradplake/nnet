package nnet;

public class MyBackpropagation {

	/**
	 * My take on the back-propagation learning algorithm adjusting neuron
	 * connection weighs in order to minimize the output error (difference
	 * between target output and actual output). Figured out that it converges
	 * quite slowly on the MNIST data set but still finds good solution (94%
	 * acc.) after a while (~250 epochs each on a random 50% selection out of
	 * all 60,000 training samples; each sample down-scaled from 28x28 to 14x14
	 * pixels).
	 */
	public static float[] backpropagate(LayeredNet net, float[] target_output, float[] actual_output, float learnRate) {

		float[] err = error(net, target_output, actual_output);

		int[] out = net.outputNeurons();
		float[] errout = new float[out.length];

		for (int i = 0; i < out.length; i++) {
			// errout[i] = Math.abs( err[out[i]] );
			errout[i] = err[out[i]] * err[out[i]];

			int[] in = net.getInputNeuronsFor(out[i]);
			for (int j = 0; j < in.length; j++) {
				net.setWeight(in[j], out[i],
						net.getWeight(in[j], out[i]) + net.getActivation(in[j]) * err[out[i]] * learnRate);
			}
		}

		int[] hidden = net.hiddenNeurons();
		for (int i = 0; i < hidden.length; i++) {
			int[] in = net.getInputNeuronsFor(hidden[i]);
			for (int j = 0; j < in.length; j++) {
				net.setWeight(in[j], hidden[i],
						net.getWeight(in[j], hidden[i]) + net.getActivation(in[j]) * err[hidden[i]] * learnRate);
			}
		}

		return errout;
	}

	/**
	 * Returns the error vector for given input, target, and guess vector.
	 * First, error is computed as delta between target and guess values at each
	 * output neuron. Next, error values are propagated through the net up until
	 * input neurons using weighted sums.
	 * 
	 * Finally, the error for each neuron is returned as a vector.
	 */
	public static float[] error(LayeredNet net, float[] target_output, float[] output) {

		float[] err = new float[net.neuronCount()];

		int[] outputNeurons = net.outputNeurons();
		for (int i = 0; i < outputNeurons.length; i++) {
			err[outputNeurons[i]] = target_output[i] - output[i];
		}

		int[] hiddenNeurons = net.hiddenNeurons();
		for (int i = hiddenNeurons.length - 1; i >= 0; i--) {
			outputNeurons = net.getOutputNeuronsFor(hiddenNeurons[i]);
			for (int j = 0; j < outputNeurons.length; j++) {
				err[hiddenNeurons[i]] += (err[outputNeurons[j]] * net.getWeight(hiddenNeurons[i], outputNeurons[j]));
			}
		}

		int[] inputNeurons = net.inputNeurons();
		for (int i = 0; i < inputNeurons.length; i++) {
			outputNeurons = net.getOutputNeuronsFor(inputNeurons[i]);
			for (int j = 0; j < outputNeurons.length; j++) {
				err[inputNeurons[i]] += (err[outputNeurons[j]] * net.getWeight(inputNeurons[i], outputNeurons[j]));
			}
		}

		return err;
	}

}
