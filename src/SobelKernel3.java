
public class SobelKernel3 extends Kernel{
	
	private int mode;
	
	private int[][] horizontalKernel = {
			{1, 2, 1},
			{0, 0, 0},
			{-1, -2, -1}
	};
	
	private int[][] verticalKernel = {
			{-1, 0, 1},
			{-2, 0, 2},
			{-1, -2, 1}
	};

	public SobelKernel3(int mode) {
		super(3); // 3x3 kernel
		this.mode = mode;
	}

	// The supplied image data must be 3x3
	@Override
	public int applyKernel(int[][] sectionData) {
		if(sectionData.length == size && sectionData[0].length == size) {
			if(mode == KernelMode.Approximate) {
				return applyApproximate(sectionData);
			}
			else if(mode == KernelMode.Exact) {
				return applyExact(sectionData);
			}
		}
		else {
			System.out.println("Section data too large: " + sectionData.length + "x" + sectionData[0].length);
		}
		return 10;
	}
	
	private int applyVertical(int[][] sectionData) {
		int weight = 0;
		for(int x = 0; x < size; x++) {
			for(int y = 0; y < size; y++) {
				weight += verticalKernel[x][y] * sectionData[x][y];
			}
		}
		return weight / (size*size);
	}
	
	private int applyHorizontal(int[][] sectionData) {
		int weight = 0;
		for(int x = 0; x < size; x++) {
			for(int y = 0; y < size; y++) {
				weight += horizontalKernel[x][y] * sectionData[x][y];
			}
		}
		return weight / (size*size);
	}
	
	private int applyApproximate(int[][] sectionData){
		int verticalWeight = applyVertical(sectionData);
		int horizontalWeight = applyHorizontal(sectionData);
		return Math.abs(verticalWeight) + Math.abs(horizontalWeight);
	}
	
	private int applyExact(int[][] sectionData){
		int verticalWeight = applyVertical(sectionData);
		int horizontalWeight = applyHorizontal(sectionData);
		return (int) Math.sqrt(verticalWeight * verticalWeight + horizontalWeight * horizontalWeight);
	}
}
