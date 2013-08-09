package prolix;

import java.util.Random;

public class Heightmap extends World {	
	private double[][] heightmap;
	public static int size;
	
	public Heightmap(int size, int type) {
		this.size = size;
		heightmap = create(size, type);
	}
	
	public double[][] getHeightmap() {
		return heightmap;
	}

	private double[][] create(int x, int type) {		
		if(x < 2 || (x & (x - 1)) != 0)
			throw new Error("Size is not a power of two");	
		
		System.out.print("Creating heightmap...\t\t");
		
		Random generator = new Random(super.seed);
		
		double[][] landscape = new double[x+1][x+1];
		
		for(int n = 0; n < landscape.length; n++) {
			for(int k = 0; k < landscape.length; k++) {
				landscape[n][k] = -999;
			}
		}
		
		landscape[0][0] = generator.nextInt(300) - 150;
		landscape[x][0] = generator.nextInt(300) - 150;
		landscape[0][x] = generator.nextInt(300) - 150;
		landscape[x][x] = generator.nextInt(300) - 150;
				
		System.out.println("Done");
		
		return diamondSquare(landscape, generator);
	}
	
	private static double[][] diamondSquare(double[][] landscape, Random gen) {
		int n = 2;
		int size = landscape.length - 1;
		int scope = size / n;
		int r = 200;
		
		do {			
			//Define square center
			for(int iterX = scope; iterX < size; iterX += (scope * 2)) {
				for(int iterY = scope; iterY < size; iterY += (scope * 2)) {					
					if(landscape[iterX][iterY] == -999) {
						landscape[iterX][iterY] = (landscape[iterX - scope][iterY - scope] +
											   landscape[iterX - scope][iterY + scope] +
											   landscape[iterX + scope][iterY - scope] +
											   landscape[iterX + scope][iterY + scope]) / 4
											   + offset(gen, r);
					}
				}
			}
			
			//Define diamond
			for(int iterX = scope; iterX < size; iterX += (scope * 2)) {
				for(int iterY = scope; iterY < size; iterY += (scope * 2)) {
					//Top
					if(iterX - scope * 2 < 0 && landscape[iterX - scope][iterY] == -999) {
						landscape[iterX - scope][iterY] = (landscape[iterX][iterY] +
								   						   landscape[iterX - scope][iterY + scope] +
								   						   landscape[iterX - scope][iterY - scope]) / 3
								   						   + offset(gen, r);
					} else if(landscape[iterX - scope][iterY] == -999) {
						landscape[iterX - scope][iterY] = (landscape[iterX][iterY] +
														   landscape[iterX - scope][iterY + scope] +
														   landscape[iterX - scope][iterY - scope] +
														   landscape[iterX - scope * 2][iterY]) / 4
														   + offset(gen, r);
					}
					//Right
					if(iterY + scope * 2 > size && landscape[iterX][iterY + scope] == -999) {
						landscape[iterX][iterY + scope] = (landscape[iterX][iterY] +
													       landscape[iterX - scope][iterY + scope] +
													       landscape[iterX + scope][iterY + scope]) / 3
													       + offset(gen, r);
					} else if(landscape[iterX][iterY + scope] == -999) {
						landscape[iterX][iterY + scope] = (landscape[iterX][iterY] +
							       						   landscape[iterX - scope][iterY + scope] +
							       						   landscape[iterX + scope][iterY + scope] +
							       						   landscape[iterX][iterY + scope * 2]) / 4
							       						   + offset(gen, r);
					}
					//Bottom
					if(iterX + scope * 2 > size && landscape[iterX + scope][iterY] == -999) {
						landscape[iterX + scope][iterY] = (landscape[iterX][iterY] +
													   	   landscape[iterX + scope][iterY + scope] +
													   	   landscape[iterX + scope][iterY - scope]) / 3
													   	   + offset(gen, r);
					} else if(landscape[iterX + scope][iterY] == -999) {
						landscape[iterX + scope][iterY] = (landscape[iterX][iterY] +
														   landscape[iterX + scope][iterY + scope] +
														   landscape[iterX + scope][iterY - scope] +
														   landscape[iterX + scope * 2][iterY]) / 4
														   + offset(gen, r);
					}
					//Left
					if(iterY - scope * 2 < 0 && landscape[iterX][iterY - scope] == -999) {
						landscape[iterX][iterY - scope] = (landscape[iterX][iterY] +
														   landscape[iterX - scope][iterY-scope] +
														   landscape[iterX + scope][iterY - scope]) / 3
														   + offset(gen, r);
					} else if(landscape[iterX][iterY - scope] == -999) {
						landscape[iterX][iterY - scope] = (landscape[iterX][iterY] +
								   						   landscape[iterX - scope][iterY-scope] +
								   						   landscape[iterX + scope][iterY - scope] +
								   						   landscape[iterX][iterY - scope * 2]) / 4
								   						   + offset(gen, r);
					}
				}
			}
			n *= 2;
			scope = size / n;
			if(r > 1)
				r /= 2;
			
		} while(scope > 0);
		
		return landscape;
	}
	
	public static double offset(Random gen, int r) {
		return (gen.nextDouble() * 2 * r) - r;
	}
}