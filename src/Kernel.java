
public abstract class Kernel {
	
	protected int size;
	
	public Kernel(int size) {
		this.size = size;
	}
	
	public abstract int applyKernel(int[][] sectionData);
	
}
