package nnet.app.mnist;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

/**
 * See The MNIST Database at: http://yann.lecun.com/exdb/mnist/
 * 
 */
public class MnistFileReader {

	/**
	 * Returns the image data as an array of color buffers or images.
	 * 
	 * File format is:
	 * 
	 * [offset] [type]          [value]          [description]
	 * 0000     32 bit integer  0x00000803(2051) magic number
	 * 0004     32 bit integer  10000            number of images
	 * 0008     32 bit integer  28               number of rows
	 * 0012     32 bit integer  28               number of columns
	 * 0016     unsigned byte   ??               pixel
	 * 0017     unsigned byte   ??               pixel
	 * ........
	 * xxxx     unsigned byte   ??               pixel
	 * 
	 * 
	 * 
	 * @throws IOException
	 */
	public static ColorBuffer[] readImages(File imagesIdx3UByte) {
		ColorBuffer[] result = null;
		
		try (Reader fileReader = new BufferedReader(new FileReader(imagesIdx3UByte));){			
			@SuppressWarnings("unused")
			int magicNumber = read4bytes(fileReader);
			// System.out.println("Magic number: "+magicNumber);
			int numberOfImages = read4bytes(fileReader);
			// System.out.println("Number of images: "+numberOfImages);
			int rows = read4bytes(fileReader);
			// System.out.println("Number of rows: "+rows);
			int cols = read4bytes(fileReader);
			// System.out.println("Number of columns: "+cols);

			result = new ColorBuffer[numberOfImages];

			for (int i = 0; i < numberOfImages; i++) {
				ColorBuffer colorBuffer = new ColorBuffer(rows, cols);
				for (int c = 0; c < cols; c++) {
					for (int r = 0; r < rows; r++) {
						int pixel = fileReader.read();

						pixel = ((255 & 0xFF) << 24) | ((pixel & 0xFF) << 16) | ((pixel & 0xFF) << 8)
								| ((pixel & 0xFF) << 0);

						colorBuffer.setPixel(r, c, pixel);
					}
				}
				result[i] = colorBuffer;
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	
	/**
	 * Returns the label data as an array of Integers.
	 * 
	 * File format is:
	 * 
	 * [offset] [type]          [value]          [description]
	 *	0000     32 bit integer  0x00000801(2049) magic number (MSB first)
	 *	0004     32 bit integer  10000            number of items
	 *	0008     unsigned byte   ??               label
	 *	0009     unsigned byte   ??               label
	 *	........
	 *	xxxx     unsigned byte   ??               label
	 * 
	 * @throws IOException 
	 * 
	 */
	public static Integer[] readLabels(File labelsIdx1UByte) {
		Integer[] result = null;
		
		try(Reader fileReader = new BufferedReader(new FileReader(labelsIdx1UByte));) {			
			@SuppressWarnings("unused")
			int magicNumber = read4bytes(fileReader);
			// System.out.println("Magic number: "+magicNumber);
			int numberOfItems = read4bytes(fileReader);
			// System.out.println("Number of items: "+numberOfItems);
			
			result = new Integer[numberOfItems];

			for (int i = 0; i < numberOfItems; i++) {
				int label = fileReader.read();
				result[i] = new Integer(label);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
	
	
	/**
	 * Reads four bytes from given reader and returns them as a 32 bit integer value.
	 * */
	private static int read4bytes(Reader fileReader) throws IOException {
		int a = fileReader.read();
		int b = fileReader.read();
		int c = fileReader.read();
		int d = fileReader.read();
		return ((a & 0xFF) << 24) | ((b & 0xFF) << 16) | ((c & 0xFF) << 8) | ((d & 0xFF) << 0);
	}
}
