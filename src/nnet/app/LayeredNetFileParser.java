package nnet.app;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import nnet.LayeredNet;

public class LayeredNetFileParser {

	public static LayeredNet loadNNet(String filename){		
		LayeredNet net = null;
		try(BufferedReader in = new BufferedReader(new FileReader(filename));) {			
			int cin = Integer.parseInt(in.readLine());
			int chn = Integer.parseInt(in.readLine());
			int con = Integer.parseInt(in.readLine());
			int nc = cin + chn + con;
			
			if (chn != 0)
				net = new LayeredNet(nc, new int[] { cin, chn, con });
			else
				net = new LayeredNet(nc, new int[] { cin, con });

			for (int i = 0; i < nc; i++) {
				for (int j = 0; j < nc; j++) {
					float w = Float.parseFloat(in.readLine());
					net.setWeight(i, j, w);
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	
		return net;
	}

	public static void saveNNet(LayeredNet net, String filename) {		
		try (BufferedWriter out = new BufferedWriter(new FileWriter(filename))){
			out.write("" + net.inputNeurons().length);
			out.newLine();
			out.write("" + net.hiddenNeurons().length);
			out.newLine();
			out.write("" + net.outputNeurons().length);
			out.newLine();
			int nc = net.neuronCount();
			for (int i = 0; i < nc; i++) {
				for (int j = 0; j < nc; j++) {
					out.write("" + net.getWeight(i, j));
					out.newLine();
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
}
