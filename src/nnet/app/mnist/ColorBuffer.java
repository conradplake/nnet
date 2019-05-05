package nnet.app.mnist;

public class ColorBuffer {

	private int[] pixels;
	private int w;
	private int h;

	public ColorBuffer(int width, int height) {
		this.w = width;
		this.h = height;
		this.pixels = new int[w * h];
	}

	public int getWidth() {
		return w;
	}

	public int getHeight() {
		return h;
	}

	public int[] getBuffer() {
		return pixels;
	}

	public void setPixel(int x, int y, int argb) {
		pixels[w * y + x] = argb;
	}

	public int getPixel(int x, int y) {
		return pixels[w * y + x];
	}

	/**
	 * Returns a new color buffer scaled down by half the width and height of
	 * the original. Each new pixel thus represents 4 original pixels by
	 * averaging their values.
	 */
	public ColorBuffer mipmap() {
		ColorBuffer result = new ColorBuffer(w / 2, h / 2);

		for (int x = 0; x < result.getWidth(); x++) {
			for (int y = 0; y < result.getHeight(); y++) {

				// pixel = avg über alle 4 nachbarn im original
				int pixel_a = getPixel(x * 2, y * 2);
				int pixel_b = getPixel(x * 2 + 1, y * 2);
				int pixel_c = getPixel(x * 2, y * 2 + 1);
				int pixel_d = getPixel(x * 2 + 1, y * 2 + 1);

				int a_alpha = (pixel_a >> 24) & 0xff;
				int b_alpha = (pixel_b >> 24) & 0xff;
				int c_alpha = (pixel_c >> 24) & 0xff;
				int d_alpha = (pixel_d >> 24) & 0xff;
				int new_alpha = (a_alpha + b_alpha + c_alpha + d_alpha) / 4;

				int a_red = (pixel_a >> 16) & 0xff;
				int b_red = (pixel_b >> 16) & 0xff;
				int c_red = (pixel_c >> 16) & 0xff;
				int d_red = (pixel_d >> 16) & 0xff;
				int new_red = (a_red + b_red + c_red + d_red) / 4;

				int a_green = (pixel_a >> 8) & 0xff;
				int b_green = (pixel_b >> 8) & 0xff;
				int c_green = (pixel_c >> 8) & 0xff;
				int d_green = (pixel_d >> 8) & 0xff;
				int new_green = (a_green + b_green + c_green + d_green) / 4;

				int a_blue = (pixel_a >> 0) & 0xff;
				int b_blue = (pixel_b >> 0) & 0xff;
				int c_blue = (pixel_c >> 0) & 0xff;
				int d_blue = (pixel_d >> 0) & 0xff;
				int new_blue = (a_blue + b_blue + c_blue + d_blue) / 4;

				int pixel = ((new_alpha & 0xFF) << 24) | ((new_red & 0xFF) << 16) | ((new_green & 0xFF) << 8)
						| ((new_blue & 0xFF) << 0);

				result.setPixel(x, y, pixel);
			}
		}

		return result;
	}
}
