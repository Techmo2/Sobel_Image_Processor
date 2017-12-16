
public class KernelThread implements Runnable {

	public static RawImage inputImage;
	public static int[][] outputImageData;
	public static int iterations;

	private int startX, startY, endX, endY;
	private Kernel kernel;

	public KernelThread(int startX, int startY, int endX, int endY, Kernel kernel) {
		this.startX = startX;
		this.startY = startY;
		this.endX = endX;
		this.endY = endY;
		this.kernel = kernel;
	}

	@Override
	public void run() {
		int dx = (kernel.size - 1) / 2;
		int dy = (kernel.size - 1) / 2;
		for (int x = startX; x < endX; x++) {
			for (int y = startY; y < endY; y++) {
				int[][] section = inputImage.rawSectionData(new int[] { -dx + x, dx + x }, new int[] { -dy + y, dy + y });
				outputImageData[x][y] = kernel.applyKernel(section);
				
				/*for (int i = 0; i < iterations - 1; i++) {
					section = inputImage.rawSectionData(new int[] { -dx + x, dx + x }, new int[] { -dy + y, dy + y });
					outputImageData[x][y] = kernel.applyKernel(section);
				}*/
			}
		}
	}
}
