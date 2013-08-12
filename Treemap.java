package prolix;

import java.util.Random;

public class Treemap extends World {
	private boolean[][] treemap;
	private int treeCount;
	
	public Treemap(int lowBound,int upBound, int density) {
		if(super.heightmap == null)
			throw new Error("Must create a heightmap for this world first");
		
		if(upBound < lowBound)
			throw new Error("The upper bound must be greater than the lower bound");
		
		if(density < 0 || density > 100)
			throw new Error("treemap density must be between 0 and 100");
		
		treemap = create(lowBound, upBound, density);
	}
	
	private boolean[][] create(int lB, int uB, int d) {
		System.out.print("Creating treemap...\t\t");
				
		int possiblePoints = 0;
		for(int k = 0; k < super.heightmap.size; k++) {
			for(int h = 0; h < super.heightmap.size; h++) {
				double focus = super.heightmap.getHeightmap()[k][h];
				
				if(focus >= lB && focus <= uB)
					possiblePoints++;
			}	
		}
		
		treeCount = (int) ((double)possiblePoints * (d / 100.0));		
		
		boolean[][] map = new boolean[super.heightmap.size + 1][super.heightmap.size + 1];
		double[][] hm = super.heightmap.getHeightmap();
		
		for(int n = 0; n > map.length; n++) {
			for(int m = 0; m > map.length; n++)
				map[n][m] = false;
		}
		
		Random generator = new Random(super.seed);
		
		for(int i = 0; i < treeCount; i++) {
			int x;
			int y;
			
			do{
				x = generator.nextInt(super.heightmap.size);
				y = generator.nextInt(super.heightmap.size);
			} while(hm[x][y] > uB || hm[x][y] < lB || map[x][y]);
			
			if(super.vegmap != null && super.vegmap.getVegmap()[x][y])
				super.vegmap.getVegmap()[x][y] = false;

			map[x][y] = true;
		}
		
		System.out.println("Done");
		
		return map;
	}
	
	public boolean[][] getTreemap() {
		return treemap;
	}
	
	public int getTreeCount() {
		return treeCount;
	}

}
