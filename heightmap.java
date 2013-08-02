package prolix;

import java.awt.*;
import java.awt.color.*;
import java.awt.image.*;

import java.io.*;

import javax.imageio.ImageIO;

import java.util.Random;


public class heightmap {

	public static double[][] create(int x,String strngSeed) {
		if(x < 2 || (x & (x - 1)) != 0)
			throw new Error("Size is not a power of two");			
		
		double[][] landscape = new double[x+1][x+1];
		
		long seed = strngSeed.hashCode();
		Random generator = new Random(seed);
		/*
		//Define corner points of the square
		int a = (int) (seed / 1000);	
		int b = (int) ((seed - (1000 * a)) / 100);
		int c = (int) ((seed - (1000 * a) - (100 * b)) / 10);
		int d = (int) (seed - (1000 * a) - (100 * b) - (10 * c));
				
		landscape[0][0] = a;
		landscape[x][0] = b;
		landscape[0][x] = c;
		landscape[x][x] = d;*/
		
		landscape[0][0] = generator.nextInt(200);
		landscape[x][0] = generator.nextInt(200);
		landscape[0][x] = generator.nextInt(200);
		landscape[x][x] = generator.nextInt(200);
				
		return diamondSquare(landscape, generator);
	}
	
	private static double[][] diamondSquare(double[][] landscape, Random gen) {
		int n = 2;
		int size = landscape.length - 1;
		int scope = size / n;
		int roughness = 200;
		
		do {			
			//Define square center
			for(int iterX = scope; iterX < size; iterX += (scope * 2)) {
				for(int iterY = scope; iterY < size; iterY += (scope * 2)) {					
					landscape[iterX][iterY] = (landscape[iterX - scope][iterY - scope] +
											   landscape[iterX - scope][iterY + scope] +
											   landscape[iterX + scope][iterY - scope] +
											   landscape[iterX + scope][iterY + scope]) / 4
											   + (gen.nextInt(roughness) - roughness / 2);
				}
		
			}
			
			//Define diamond
			for(int iterX = scope; iterX < size; iterX += (scope * 2)) {
				for(int iterY = scope; iterY < size; iterY += (scope * 2)) {
					//Top
					if(iterX - scope * 2 < 0) {
						landscape[iterX - scope][iterY] = (landscape[iterX][iterY] +
								   						   landscape[iterX - scope][iterY + scope] +
								   						   landscape[iterX - scope][iterY - scope]) / 3
								   						   + (gen.nextInt(roughness) -roughness / 2);
					} else {
						landscape[iterX - scope][iterY] = (landscape[iterX][iterY] +
														   landscape[iterX - scope][iterY + scope] +
														   landscape[iterX - scope][iterY - scope] +
														   landscape[iterX - scope * 2][iterY]) / 4
														   + (gen.nextInt(roughness) - roughness / 2);
					}
					//Right
					if(iterY + scope * 2 > size) {
						landscape[iterX][iterY + scope] = (landscape[iterX][iterY] +
													       landscape[iterX - scope][iterY + scope] +
													       landscape[iterX + scope][iterY + scope]) / 3
													       + (gen.nextInt(roughness) - roughness / 2);
					} else {
						landscape[iterX][iterY + scope] = (landscape[iterX][iterY] +
							       						   landscape[iterX - scope][iterY + scope] +
							       						   landscape[iterX + scope][iterY + scope] +
							       						   landscape[iterX][iterY + scope * 2]) / 4
							       						   + (gen.nextInt(roughness) - roughness / 2);
					}
					//Bottom
					if(iterX + scope * 2 > size) {
						landscape[iterX + scope][iterY] = (landscape[iterX][iterY] +
													   	   landscape[iterX + scope][iterY + scope] +
													   	   landscape[iterX + scope][iterY - scope]) / 3
													   	   + (gen.nextInt(roughness) - roughness / 2);
					} else {
						landscape[iterX + scope][iterY] = (landscape[iterX][iterY] +
														   landscape[iterX + scope][iterY + scope] +
														   landscape[iterX + scope][iterY - scope] +
														   landscape[iterX + scope * 2][iterY]) / 4
														   + (gen.nextInt(roughness) - roughness / 2);
					}
					//Left
					if(iterY - scope * 2 < 0) {
						landscape[iterX][iterY - scope] = (landscape[iterX][iterY] +
														   landscape[iterX - scope][iterY-scope] +
														   landscape[iterX + scope][iterY - scope]) / 3
														   + (gen.nextInt(roughness) - roughness / 2);
					} else {
						landscape[iterX][iterY - scope] = (landscape[iterX][iterY] +
								   						   landscape[iterX - scope][iterY-scope] +
								   						   landscape[iterX + scope][iterY - scope] +
								   						   landscape[iterX][iterY - scope * 2]) / 4
								   						   + (gen.nextInt(roughness) - roughness / 2);
					}
				}
			}
			n *= 2;
			scope = size / n;
			if(roughness > 1)
				roughness /= 2;
			
		} while(scope > 0);
		
		return landscape;
	}
	
	public static void preview(double[][] landscape) throws IOException {
		int sz = landscape.length;
		byte[] buffer = new byte[sz * sz];
		
		for(int i = 0; i < sz; i++) {
			for(int j = 0; j < sz; j++) {
				buffer[(i * sz) + j] = (byte) (landscape[i][j]); 
			}
		}
		
		ImageIO.write(getGrayscale(landscape.length, buffer), "PNG", new File("preview"));
	}
	
	private static BufferedImage getGrayscale(int width, byte[] buffer) {
		int height = buffer.length / width;
		ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
		int[] nBits = { 8 };
		ColorModel cm = new ComponentColorModel(cs, nBits, false, true,
				Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
		SampleModel sm = cm.createCompatibleSampleModel(width, height);
		DataBufferByte db = new DataBufferByte(buffer, width * height);
		WritableRaster raster = Raster.createWritableRaster(sm, db, null);
		BufferedImage result = new BufferedImage(cm, raster, false, null);
		
		return result;
	}
}