
public class Sobel {
	public static void main(String[] args) {
		ImageProcessor ip = new ImageProcessor();
		Kernel sobel = new SobelKernel7(SobelMode.Approximate);
		RawImage image = new RawImage("test.jpg");
		int[][] processed = ip.ProcessImage(sobel, image, 1); // 5 iterations
		ip.writeImage(processed, "out.jpg");
		ip.writeImage(image.rawImageData(), "pre.jpg");
		
		System.out.println("Average image value pre processing: " + image.averageValue());
		
		int[][] imageData = processed;
		int average = 0;
		for(int x = 0; x < imageData.length; x++) {
			for(int y = 0; y < imageData[0].length; y++) {
				average += imageData[x][y];
			}
		}
		average /= (imageData.length * imageData[0].length);
		
		System.out.println("Average image value post processing: " + average);
	}
}
