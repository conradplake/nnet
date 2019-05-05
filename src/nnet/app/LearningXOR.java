package nnet.app;

import java.util.LinkedList;
import java.util.List;

import nnet.MyBackpropagation;
import nnet.LayeredNet;


class Example {

	public Example(float[] input, float[] output) {
		this.input = input;
		this.output = output;
	}

	float[] input;
	float[] output;
}



public class LearningXOR {

	public static void main(String[] args) {	
		LayeredNet net = new LayeredNet(5, new int[]{2, 2, 1});
		net.randomlyInitWeights(-1f, 1f);
		train(net, getXORExamples(), 0.1f, 0.01f);
	}
	
	
	public static void train(LayeredNet net, List<Example> examples, float learnRate, float minError) {
	
		System.out.println("start training...");
		long startTime = System.currentTimeMillis();
		float totalError;
		do {
			totalError = 0;
			for (Example e : examples) {				
				float[] myOut = net.output(e.input);
				float[] errout = MyBackpropagation.backpropagate(net, e.output, myOut, learnRate);
				for (int i = 0; i < errout.length; i++) {
					totalError += (errout[i] / errout.length);
				}
			}
			System.out.println("learnRate = " + learnRate + ",  totalError = " + totalError);
//			learnRate -= (learnRate/1000); // do simulated annealing by slowly decreasing learning step size
		} while (Math.abs(totalError) > minError);		
		System.out.println("done in "+(System.currentTimeMillis()-startTime)/1000f+" sec");
			
		
		for (Example example : examples) {
			printOutput(net, example.input);
		}
		
		// show the trained net
		NNetVisualizer vis = new NNetVisualizer();
		vis.visualize(net, NNetVisualizer.LAYERED);
		vis.setVisible(true);
	}
	
	private static List<Example> getXORExamples(){
		List<Example> examples = new LinkedList<Example>();
			
		examples.add(new Example(new float[]{1, 1}, new float[]{0}));
		examples.add(new Example(new float[]{0, 0}, new float[]{0}));
		examples.add(new Example(new float[]{1, 0}, new float[]{1}));
		examples.add(new Example(new float[]{0, 1}, new float[]{1}));
		
		return examples;
	}
	
	private static void printOutput(LayeredNet net, float[] in) {
		float[] out = net.output(in);
		System.out.println("x = " + in[0] + ",  y = " + in[1]);
		for (int i = 0; i < out.length; i++) {
			System.out.print("out[" + i + "] = " + out[i]);
		}
		System.out.println();
	}

}