package map;

public class ChunkManager 
{

	private Chunk[][] chunks;
	public int chunkWidth, chunkHeight;
	public int chunkSize;
	
	public ChunkManager()
	{
		chunkWidth = 0;
		chunkHeight = 0;
		chunkSize = 0;
	}
	public ChunkManager(int chunkWidth, int chunkHeight, int chunkSize)
	{
		this.chunkHeight = chunkHeight;
		this.chunkWidth = chunkWidth;
		this.chunkSize = chunkSize;
		
		chunks = new Chunk[chunkHeight][chunkWidth];
		
		for(int i=0;i<chunkHeight;i++)
			for(int j=0;j<chunkWidth;j++)		
				chunks[i][j] = new Chunk(j, i);			
	}
	
	public void setUpManager(int chunkWidth, int chunkHeight, int chunkSize)
	{
		this.chunkHeight = chunkHeight;
		this.chunkWidth = chunkWidth;
		this.chunkSize = chunkSize;
		
		chunks = new Chunk[chunkHeight][chunkWidth];
		
		for(int i=0;i<chunkHeight;i++)
			for(int j=0;j<chunkWidth;j++)		
				chunks[i][j] = new Chunk(j, i);
	}
	
	public Chunk getChunk(int x, int y){return chunks[y][x];}
	
}
