
public class Sobel {
	private static final int Combined = 1;
	private static final int SmallKernel = 2;
	private static final int LargeKernel = 3;
	
	private static final int demonstrationType = Combined;
	
	public static void main(String[] args) {
		ImageProcessor ip = new ImageProcessor();
		Kernel sobel7 = new SobelKernel7(KernelMode.Exact);
		Kernel sobel3 = new SobelKernel3(KernelMode.Exact);
		
		long startTime = System.currentTimeMillis();
		RawImage image = new RawImage("test.jpg"); // Load the base image file
		float runTime = (System.currentTimeMillis() - startTime) / 1000.0f;
		System.out.println("Image loaded in " + runTime + " seconds.");
		
		startTime = System.currentTimeMillis();
		int[][] processed;
		
		if(demonstrationType == Combined) {
			System.out.println("Running combined kernels.");
			processed = ip.ProcessImage(sobel3, image, 1); // Process with 3x3
			processed = ip.ProcessImage(sobel7, new RawImage(processed), 1); // Then process with 7x7
			processed = ip.amplifyImage(processed, 2.0f);
		}
		else if(demonstrationType == SmallKernel) {
			System.out.println("Running small kernel.");
			processed = ip.ProcessImage(sobel3, image, 5); // Process with 3x3, 5 times
			processed = ip.amplifyImage(processed, 1.5f); // The image gets a little dark
		}
		else if(demonstrationType == LargeKernel) {
			System.out.println("Running large kernel.");
			processed = ip.ProcessImage(sobel7, image, 1); // Then process with 7x7
		}
		else {
			System.out.println("I don't know what you want me to do.");
			System.exit(-1);
		}
		
		runTime = (System.currentTimeMillis() - startTime) / 1000.0f;
		System.out.println("Sobel finished in " + runTime + " seconds.");
		ip.writeImage(processed, "out.jpg"); // The final processed image
		ip.writeImage(image.rawImageData(), "pre.jpg"); // The image after it is loaded, but before it's processed
	}
}
