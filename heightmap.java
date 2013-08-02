package prolix;

public class heightmap {

	public static double[][] create(int x,long seed) {
		double[][] landscape = new double[x+1][x+1];
		
		//Define corner points of the square
		int a = (int) (seed / 1000);	
		int b = (int) ((seed - (1000 * a)) / 100);
		int c = (int) ((seed - (1000 * a) - (100 * b)) / 10);
		int d = (int) (seed - (1000 * a) - (100 * b) - (10 * c));
		
		landscape[0][0] = a;
		landscape[x][0] = b;
		landscape[0][x] = c;
		landscape[x][x] = d;
		
		int[] tlBound = {0, 0};
		
		return diamondSquare(landscape,tlBound,x);
	}
	
	private static double[][] diamondSquare(double[][] landscape,int[] tlBound, int scope) {
		int x = tlBound[0];
		int y = tlBound[1];
		
		//Define center of square
		landscape[x + scope / 2][y + scope / 2] =
				(landscape[x][y] 
				+ landscape[x + scope][y] 
				+ landscape[x][y + scope] 
				+ landscape[x + scope][y + scope]) / 4;
		
		//Define the diamond points of the square
		//Top 
		landscape[x][y + scope / 2] = (landscape[x][y] + landscape[x][y + scope] + landscape[x + scope / 2][y + scope / 2]) / 3;
		//Right 
		landscape[x + scope / 2][y + scope] = (landscape[x][y + scope] + landscape[x + scope][y + scope] + landscape[x + scope / 2][y + scope / 2]) / 3;
		//Bottom 
		landscape[x + scope][y + scope / 2] = (landscape[x + scope][y + scope] + landscape[x + scope][y] + landscape[x + scope / 2][(y + scope) / 2]) / 3;
		//Left 
		landscape[x + scope / 2][y] = (landscape[x][y] + landscape[x + scope][y] + landscape[x + scope / 2][y + scope / 2]) / 3;
			
		if(scope > 2) {
			diamondSquare(landscape,new int[] {x, y},scope / 2);
			diamondSquare(landscape,new int[] {x, y + scope / 2},scope / 2);
			diamondSquare(landscape,new int[] {x + scope / 2, y},scope / 2);
			diamondSquare(landscape,new int[] {x + scope / 2, y + scope / 2},scope / 2);
		}
		
		return landscape;
	}
}