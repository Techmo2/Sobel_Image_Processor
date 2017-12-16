import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class RawImage {
	private int[][] imageData;
	public int width, height;
	
	public RawImage(String fileName) {
		try {
		loadImage(fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		width = imageData.length;
		height = imageData[0].length;
	}
	
	public RawImage(int[][] data) {
		imageData = data;
		width = imageData.length;
		height = imageData[0].length;
	}
	
	public int[][] rawImageData(){
		return imageData;
	}
	
	// Returns the data from a section of the image
	public int[][] rawSectionData(int[] xRange, int[] yRange){
		int[][] sectionData = new int[xRange[1] - xRange[0] + 1][yRange[1] - yRange[0] + 1];
		
		for(int x = xRange[0]; x <= xRange[1]; x++) {
			for(int y = yRange[0]; y <= yRange[1]; y++) {
				sectionData[x - xRange[0]][y - yRange[0]] = imageData[x][y];
			}
		}
		
		return sectionData;
	}
	
	public void setSectionData(int startX, int startY, int[][] data) {
		for(int x = startX; x < startX + data.length; x++) {
			for(int y = startY; y < startY + data[0].length; y++) {
				if(!(x > imageData.length || y > imageData[0].length)) {
					if(!(x < 0 || y < 0)) {
						imageData[x][y] = data[x - startX][y - startY];
					}
				}
			}
		}
	}
	
	public int averageValue() {
		int average = 0;
		for(int x = 0; x < imageData.length; x++) {
			for(int y = 0; y < imageData[0].length; y++) {
				average += imageData[x][y];
			}
		}
		return average / (imageData.length * imageData[0].length);
	}
	
	private void loadImage(String fileName) throws Exception {
		BufferedImage img = ImageIO.read(new File(fileName));
		width = img.getWidth();
		height = img.getHeight();
		imageData = new int[width][height];
		
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				int pixel = img.getRGB(x, y);
				int r = (pixel >> 16) & 0xff;
				int g = (pixel >> 8) & 0xff;
				int b = (pixel) & 0xff;
				imageData[x][y] = (r + g + b) / 3;
			}
		}
	}
}

