import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;

public class ImageProcessor {
	public ImageProcessor() {
		
	}
	
	public int[][] ProcessImage(Kernel kernel, RawImage image, int iterations) {
		int[][] output = new int[image.width][image.height];
		int padding = (kernel.size - 1) / 2;
		for(int x = padding; x < image.width - padding; x++) {
			for(int y = padding; y < image.height - padding; y++){
				int dx = (kernel.size - 1) / 2;
				int dy = (kernel.size - 1) / 2;
				int[][] section = image.rawSectionData(new int[] {-dx + x, dx + x}, new int[] {-dy + y, dy + y});
				//System.out.println("Average section pre: " + averageValue(section));
				for(int i = 0; i < iterations; i++)
					section = image.rawSectionData(new int[] {-dx + x, dx + x}, new int[] {-dy + y, dy + y});
					output[x][y] = kernel.applyKernel(section);
				//System.out.println("Average section pre: " + averageValue(output) + "\n");
			}
		}
		return output;
	}
	
	public int[][] ProcessImageMult(Kernel kernel, RawImage image, int iterations, int threads){
		KernelThread.inputImage = image;
		KernelThread.iterations = iterations;
		
		ExecutorService executor = Executors.newFixedThreadPool(threads);
		int padding = (kernel.size - 1) / 2;
		int yChunkSize = (image.height - (padding * 2)) / threads;
		int currentYOffset = padding;
		for(int i = 0; i < threads; i++) {
			Runnable kernelWorker = new KernelThread(padding, currentYOffset, image.width - padding, currentYOffset + yChunkSize, kernel);
			executor.execute(kernelWorker);
		}
		executor.shutdown();
		while(!executor.isTerminated()) {}
		return KernelThread.outputImageData;
	}
	
	public void writeImage(int[][] imageData, String fileName) {
		BufferedImage outImage = new BufferedImage(imageData.length, imageData[0].length, BufferedImage.TYPE_INT_RGB);
		for(int x = 0; x < outImage.getWidth(); x++) {
			for(int y = 0; y < outImage.getHeight(); y++) {
				int rawPixelData = imageData[x][y];
				int rgb = ((rawPixelData&0x0ff)<<16)|((rawPixelData&0x0ff)<<8)|(rawPixelData&0x0ff);			
				outImage.setRGB(x,  y,  rgb);
			}
		}
		File outFile = new File(fileName);
		try {
			ImageIO.write(outImage, "jpg", outFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private int[] concat(int[][] data) {
		int[] outData = new int[data.length * data[0].length];
		for(int x = 0; x < data.length; x++) {
			for(int y = 0; y < data.length; y++) {
				outData[x * y] = data[x][y];
			}
		}
		return outData;
	}
	
	public int averageValue(int[][] imageData) {
		int average = 0;
		for(int x = 0; x < imageData.length; x++) {
			for(int y = 0; y < imageData[0].length; y++) {
				average += imageData[x][y];
			}
		}
		return average / (imageData.length * imageData[0].length);
	}
	
	private byte[] toByteArray(int[] data) {
		byte[] outData = new byte[data.length];
		for(int n = 0; n < data.length; n++) {
			outData[n] = (byte)data[n];
		}
		return outData;
	}
}
