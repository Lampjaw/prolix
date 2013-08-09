package prolix;

import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class MapMaker {	
	BufferedImage grayscape = null;
	BufferedImage colorscape = null;
	BufferedImage contours = null;
	static double[][] hm;
	
	public MapMaker(Heightmap heightmap){
		hm = heightmap.getHeightmap();
	}
	
	public static BufferedImage createGrayscale() throws IOException{
		System.out.print("Creating grayscale map...\t");
		
		int sz = hm.length;
		byte[] buffer = new byte[sz * sz];
		
		double high = 0;
		double low = 0;
		
		for(int n = 0; n < sz; n++) {
			for(int k = 0; k < sz; k++) {
				if(hm[n][k] > high)
					high = hm[n][k];
				if(hm[n][k] < low)
					low = hm[n][k];
			}
		}
		
		double diff = (high - low) / 255.0;
		
		for(int y = 0; y < sz; y++) {
			for(int x = 0; x < sz; x++) 
				buffer[(y * sz) + x] = (byte) ((hm[y][x] + -low) / diff);
		}
		
		int height = buffer.length / hm.length;
		ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
		int[] nBits = { 8 };
		ColorModel cm = new ComponentColorModel(cs, nBits, false, true,
				Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
		SampleModel sm = cm.createCompatibleSampleModel(hm.length, height);
		DataBufferByte db = new DataBufferByte(buffer, hm.length * height);
		WritableRaster raster = Raster.createWritableRaster(sm, db, null);
		BufferedImage result = new BufferedImage(cm, raster, false, null);
		
		ImageIO.write(result, "PNG", new File("grayscale"));
		
		System.out.println("Done");
		
		return result;
	}
	
	public static BufferedImage createColorscape() throws IOException {
		System.out.print("Creating color map...\t\t");
		
		int sz = hm.length;
		byte[] buffer = new byte[sz * sz * 3];
		
		double high = 0;
		double low = 0;
		
		for(int n = 0; n < sz; n++) {
			for(int k = 0; k < sz; k++) {
				if(hm[n][k] > high)
					high = hm[n][k];
				if(hm[n][k] < low)
					low = hm[n][k];
			}
		}
		
		int[] c = {0, 0, 0};
						
		for(int y = 0; y < sz; y++) {
			for(int x = 0; x < sz; x++) {
				if(hm[y][x] < -5) 		{ c = new int[]{27, 112, 250 - 1 * (int) hm[y][x] * -1}; }	//Deep water
				else if(hm[y][x] < 0) 	{ c = new int[]{27, 224, 224}; }							//Shallow water
				else if(hm[y][x] < 5) 	{ c = new int[]{237, 237, 69}; }							//Beach
				else if(hm[y][x] < 75) 	{ c = new int[]{5, 235 - 1 * ((int) hm[y][x] - 5), 36}; }	//Plains
				else if(hm[y][x] < 110) { c = new int[]{163 + 2 * ((int) hm[y][x] - 75), 181 + 2* ((int) hm[y][x] - 75), 165 + 2 * ((int) hm[y][x] - 75)}; }	//Stone
				else 					{ c = new int[]{240, 240, 240}; }							//Snow
				
				buffer[(0 + 3  * (x + y * sz))] = (byte) c[0]; //Red channel
				buffer[(1 + 3  * (x + y * sz))] = (byte) c[1]; //Green channel
				buffer[(2 + 3  * (x + y * sz))] = (byte) c[2]; //Blue channel
			}
		}		
		
		int width = hm.length;
		int height = buffer.length / width;
		ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_LINEAR_RGB);
		int[] nBits = { 8, 8, 8 };
		ColorModel cm = new ComponentColorModel(cs, nBits, false, true,
				Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
		SampleModel sm = cm.createCompatibleSampleModel(width, height / 3);
		DataBufferByte db = new DataBufferByte(buffer, width * height);
		WritableRaster raster = Raster.createWritableRaster(sm, db, null);
		BufferedImage result = new BufferedImage(cm, raster, false, null);
		
		ImageIO.write(result, "PNG", new File("color"));
		
		System.out.println("Done");
		
		return result;
	}
	
	public static BufferedImage createContours(int scale, boolean land, boolean sea) throws IOException {
		System.out.print("Creating contour map...\t\t");
		
		int sz = hm.length;
		byte[] buffer = new byte[sz * sz];
								
		for(int y = 0; y < sz; y++) {
			for(int x = 0; x < sz; x++) {
				int focus =(int) hm[y][x];
				if(Math.abs(focus % (scale * 2))< scale) {
					if(land && focus > 0) buffer[(y * sz) + x] = (byte) 255;
					if(sea && focus < 0)  buffer[(y * sz) + x] = (byte) 255;
				} else if(Math.abs(focus % scale) < scale) {
					if(land && focus > 0) buffer[(y * sz) + x] = (byte) 0;
					if(sea && focus < 0)  buffer[(y * sz) + x] = (byte) 0;
				} else
					buffer[(y * sz) + x] = (byte) 0;				
			}
		}
		
		int height = buffer.length / hm.length;
		ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
		int[] nBits = { 8 };
		ColorModel cm = new ComponentColorModel(cs, nBits, false, true,
				Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
		SampleModel sm = cm.createCompatibleSampleModel(hm.length, height);
		DataBufferByte db = new DataBufferByte(buffer, hm.length * height);
		WritableRaster raster = Raster.createWritableRaster(sm, db, null);
		BufferedImage result = new BufferedImage(cm, raster, false, null);
		
		CannyEdgeDetector detector = new CannyEdgeDetector();
		
		detector.setLowThreshold(0.5f);
		detector.setHighThreshold(1.0f);
		
		detector.setSourceImage(result);
		detector.process();
		
		result = detector.getEdgesImage();
		
		ImageIO.write(result, "PNG", new File("contour"));
		
		System.out.println("Done");
		
		return result;
	}

	public void showGrayscale() throws IOException {
		if(grayscape == null)
			grayscape = createGrayscale();
	}

	public void showColor() throws IOException {
		if(colorscape == null)
			colorscape = createColorscape();		
	}
	
	public void showContours(int scale, boolean land, boolean sea) throws IOException {
		if(contours == null)
			contours = createContours(scale, land, sea);		
	}
}
