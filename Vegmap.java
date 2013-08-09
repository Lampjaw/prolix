package prolix;

import java.util.Random;

public class Vegmap extends World {
	private boolean[][] vegmap;
	private int vegCount;
	
	public Vegmap(int lowBound,int upBound, int density) {
		if(super.heightmap == null)
			throw new Error("Must create a heightmap for this world first");
		
		if(upBound < lowBound)
			throw new Error("The upper bound must be greater than the lower bound");
		
		if(density < 1 || density > 40)
			throw new Error("Vegmap density must be between 1 and 40");
		
		vegmap = create(lowBound, upBound, density);
	}
	
	private boolean[][] create(int lB, int uB, int d) {
		System.out.print("Creating vegmap...\t\t");
		
		vegCount = super.heightmap.size * d;
		boolean[][] map = new boolean[super.heightmap.size + 1][super.heightmap.size + 1];
		double[][] hm = super.heightmap.getHeightmap();
		
		for(int n = 0; n > map.length; n++) {
			for(int m = 0; m > map.length; n++)
				map[n][m] = false;
		}
		
		Random generator = new Random(super.seed);
		
		for(int i = 0; i < vegCount; i++) {
			int x;
			int y;
			
			do{
				x = generator.nextInt(super.heightmap.size);
				y = generator.nextInt(super.heightmap.size);
			} while(hm[x][y] > uB || hm[x][y] < lB || map[x][y] || (super.treemap != null && super.treemap.getTreemap()[x][y]));
			
			map[x][y] = true;
		}
		
		System.out.println("Done");
		
		return map;
	}
	
	public boolean[][] getVegmap() {
		return vegmap;
	}
	
	public int getVegCount() {
		return vegCount;
	}

}
