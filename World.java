package prolix;

import java.io.IOException;

public class World {		
	Heightmap heightmap;
	MapMaker mapMaker;
	
	public World(int size, String seed, int type) throws IOException {		
		heightmap = new Heightmap(size, seed, type);
		mapMaker = new MapMaker(heightmap);
	}
	
	public void renderGrayscale() throws IOException {
		mapMaker.showGrayscale();
	}
	
	public void render2D() throws IOException {
		mapMaker.showColor();
	}
	
	public void renderContours(int scale, boolean land, boolean sea) throws IOException {
		mapMaker.showContours(scale, land, sea);
	}
}
