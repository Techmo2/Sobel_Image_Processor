import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;

public class ImageProcessor {
	
	// Processes a RawImage with a sobel kernel
	public int[][] ProcessImage(Kernel kernel, RawImage image, int iterations) {
		int[][] output = new int[image.width][image.height];
		int padding = (kernel.size - 1) / 2; // So we don't go out of range of the image
		for (int x = padding; x < image.width - padding; x++) {
			for (int y = padding; y < image.height - padding; y++) {
				
				// Apply the kernel to a small section of the image whose size is that of the kernel
				for (int i = 0; i < iterations; i++) {
					int[][] section = image.rawSectionData(new int[] { -padding + x, padding + x }, new int[] { -padding + y, padding + y }); // Copy small section of the image
					output[x][y] = kernel.applyKernel(section);
				}
			}
		}
		return output;
	}

	// Multithreaded implementation of the ProcessImage method, currently not functional
	public int[][] ProcessImageMult(Kernel kernel, RawImage image, int iterations, int threads) {
		KernelThread.inputImage = image;
		KernelThread.iterations = iterations;

		ExecutorService executor = Executors.newFixedThreadPool(threads);
		int padding = (kernel.size - 1) / 2;
		int yChunkSize = (image.height - (padding * 2)) / threads;
		int currentYOffset = padding;
		for (int i = 0; i < threads; i++) {
			Runnable kernelWorker = new KernelThread(padding, currentYOffset, image.width - padding,
					currentYOffset + yChunkSize, kernel);
			executor.execute(kernelWorker);
		}
		executor.shutdown();
		while (!executor.isTerminated()) {
		}
		return KernelThread.outputImageData;
	}

	// Writes the image data to a .jpg file
	public void writeImage(int[][] imageData, String fileName) {
		BufferedImage outImage = new BufferedImage(imageData.length, imageData[0].length, BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < outImage.getWidth(); x++) {
			for (int y = 0; y < outImage.getHeight(); y++) {
				int rawPixelData = imageData[x][y];
				int rgb = ((rawPixelData & 0x0ff) << 16) | ((rawPixelData & 0x0ff) << 8) | (rawPixelData & 0x0ff);
				outImage.setRGB(x, y, rgb);
			}
		}
		File outFile = new File(fileName);
		try {
			ImageIO.write(outImage, "jpg", outFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Gets the average rgb value of the image data provided, used for debugging on occasion
	public int averageValue(int[][] imageData) {
		int average = 0;
		for (int x = 0; x < imageData.length; x++) {
			for (int y = 0; y < imageData[0].length; y++) {
				average += imageData[x][y];
			}
		}
		return average / (imageData.length * imageData[0].length);
	}
	
	public int[][] amplifyImage(int[][] image, float amplitude){
		int[][] out = new int[image.length][image[0].length];
		for(int x = 0; x < image.length; x++) {
			for(int y = 0; y < image[0].length; y++) {
				out[x][y] = (int)(image[x][y] * amplitude);
			}
		}
		return out;
	}
}
