package prolix;

import java.io.IOException;

public class World {		
	protected static Heightmap heightmap = null;
	protected static Treemap treemap = null;
	protected static Vegmap vegmap = null;
	protected static MapMaker mapMaker = null;
	protected static long seed;
	
	protected World() {
	}
	
	public World(String seed) throws IOException {
		System.out.print("Creating world...\t\t");
		
		this.seed = seed.hashCode();
		mapMaker = new MapMaker();
		
		System.out.println("Done");
	}
	
	public void add(int generator, int arg1, int arg2) {
		if(generator == Prolix.PG_HEIGHTMAP)
			heightmap = new Heightmap(arg1, arg2);
		else
			throw new Error("Not a valid add");			
	}
	
	public void add(int generator, int arg1, int arg2, int arg3) {
		if(generator == Prolix.PG_TREEMAP)
			treemap = new Treemap(arg1, arg2, arg3);
		else if(generator == Prolix.PG_VEGMAP)
			vegmap = new Vegmap(arg1, arg2, arg3);
		else
			throw new Error("Not a valid add");			
	}
	
	public void renderGrayscale() throws IOException {
		mapMaker.showGrayscale(heightmap);
	}
	
	public void render2D() throws IOException {
		mapMaker.showColor(heightmap);
	}
	
	public void renderContours(int scale, boolean land, boolean sea) throws IOException {
		mapMaker.showContours(heightmap,scale, land, sea);
	}
}
