package map;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.graphics.Color;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;

import assetsHandlers.MainAssetsManager;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;

import map_generator.MapGenerator;
import map_generator.MapTile;
import map_generator.mapInfo.biomeType;

public class Map 
{
	
	public final int chunkSize = 16;
	
	public float[][] fMap;
	public int tileSize;
	public ChunkManager chunkManager;
	//private MapGenerator generator;
	private int width, height;
	private Color color; 
	private Pixmap temperaturePixmap;
	private Pixmap heightPixmap;
	private Pixmap humidityPixmap;
	private Pixmap biomesPixmap;
	private Pixmap additionalPixmap;
	private Pixmap worldPixmap;
	private MainAssetsManager manager;	
	public boolean isMapGenerated;
	public boolean isGeneratingStarted;
	//private boolean[][] isChunkGenerated;
	private Pixmap[][] chunkPixmap;
	private Texture[][] chunkTexture;
	private Pixmap readyMap;
	private Random generator;
	
	
	/**********************************
	 * DEBUG
	 */
	ArrayList<Color> kolory = new ArrayList<Color>();
	ArrayList<Integer> grupy = new ArrayList<Integer>();
	ArrayList<Integer> licznik = new ArrayList<Integer>();
	
	
	//////////////////////////////////////
	
	
	
	public Map(int width, int height, int tileSize, MainAssetsManager manager)
	{
		this.width = width;
		this.height = height;
		this.tileSize = tileSize;
		this.manager = manager;
		
		color = new Color();
		fMap = new float[height][width];
	//	generator = new MapGenerator(width, height, 0); 
		generator = new Random();
		isGeneratingStarted = false;
		isMapGenerated = false;
		
		
		
		Texture temperatureTexture = manager.get("Texture/temperature.png", Texture.class);
		if (!temperatureTexture.getTextureData().isPrepared()) {
			temperatureTexture.getTextureData().prepare();
        }
        temperaturePixmap = temperatureTexture.getTextureData().consumePixmap();
        
        Texture heightTexture = manager.get("Texture/height.png", Texture.class);
		if (!heightTexture.getTextureData().isPrepared()) {
			heightTexture.getTextureData().prepare();
        }
        heightPixmap = heightTexture.getTextureData().consumePixmap();
        
        Texture humidityTexture = manager.get("Texture/humidity.png", Texture.class);
		if (!humidityTexture.getTextureData().isPrepared()) {
			humidityTexture.getTextureData().prepare();
        }
		humidityPixmap = humidityTexture.getTextureData().consumePixmap();
		
		Texture biomesTexture = manager.get("Texture/biomes.png", Texture.class);
		if (!biomesTexture.getTextureData().isPrepared()) {
			biomesTexture.getTextureData().prepare();
        }
		biomesPixmap = biomesTexture.getTextureData().consumePixmap();
		
		Texture worldTexture = manager.get("Texture/worldTexture.png", Texture.class);
		if (!worldTexture.getTextureData().isPrepared()) {
			worldTexture.getTextureData().prepare();
        }
		worldPixmap = worldTexture.getTextureData().consumePixmap();
		
	}
	/*
	public Pixmap generateMap(Pixmap map)
	{
		map = new Pixmap(width * tileSize, height*tileSize, Format.RGBA8888);
		
		fMap = generator.generateRandomPoints(fMap);
		fMap = generator.generatePerlinNoise(fMap, 1, 0.6f, 1);	
		
		for(int i=0;i<height;i++)
			for(int j=0;j<width;j++)		
				map = setTextureTile(map, j, i, getIntegerMapTileValue(fMap[i][j], 32),1);
		
		return map;	
	}*/
	
	public Pixmap putMap(Pixmap map, float[][] fMap)
	{
		map = new Pixmap(width * tileSize, height*tileSize, Format.RGBA8888);
		
		for(int i=0;i<height;i++)
			for(int j=0;j<width;j++)	
				map = setTextureTile(map, j, i, getIntegerMapTileValue(fMap[i][j], 31),1);		
		
		return map;
	}
	
	public Pixmap debugPutMap(Pixmap map, float[][] fMap, int type)
	{
		map = new Pixmap(width * tileSize, height*tileSize, Format.RGBA8888);
		
		for(int i=0;i<height;i++)
			for(int j=0;j<width;j++)
			{
				map = setTextureTile(map, j, i, getIntegerMapTileValue(fMap[i][j], 31), type);
			}
		return map;
	}
	/*
	public Pixmap putBiomesOnMap(Pixmap map, MapTile[][] tileMap)
	{
		int val;
		int textureSize = 16;
		map = new Pixmap(width * tileSize, height*tileSize, Format.RGBA8888);
		
		for(int i=0;i<height;i++)
			for(int j=0;j<width;j++)
			{
				val = tileMap[i][j].biome.ordinal();
				
				if(val <= 1)
				{
					color.set(biomesPixmap.getPixel(val, 0));
					map.setColor(color);
					map.fillRectangle(j*tileSize, i*tileSize, tileSize, tileSize);
				}			
				else
				{
					map.drawPixmap(worldPixmap, j*textureSize, i*textureSize, 16*val, 64, textureSize, textureSize);
						
				}
				
				
			}
		return map;
	}
	*/
	
	public Pixmap putBiomesOnMap(Pixmap map,int minX, int minY, int chunkSize, MapTile[][] tileMap)
	{
		int val, intColor=0;
		int textureSize = 16;
		int x, y;
		map = new Pixmap(chunkSize * tileSize, chunkSize*tileSize, Format.RGBA8888);
		
		boolean left, top, right, bottom;		
		int tmpTileCount;
		Random random = new Random();
		
		
		
		
		
		for(int i=minY;i<minY + chunkSize;i++)
			for(int j=minX;j<minX + chunkSize;j++)
			{
				
				val = tileMap[i][j].biome.ordinal();
				x=j-minX;
				y=i-minY;
				

				if(tileMap[i][j].biomeBitmask == 15)
				{
					if(val == 0)
					{					 
						map.setColor(biomesPixmap.getPixel(val, 0));
						map.fillRectangle(x*tileSize, y*tileSize, tileSize, tileSize);
					}	
					else
					{
						for(int xx=0;xx<textureSize;xx++)
							for(int yy=0;yy<textureSize;yy++)
							{
									
								intColor = worldPixmap.getPixel(16*val + xx, 16 + yy);
								map.drawPixel(tileSize*x+xx, tileSize*y+yy, intColor);
							}	
					}			
				}
				else
				{
					left = false; top = false; right = false; bottom = false;
					tmpTileCount = tileMap[i][j].biomeBitmask;
					if(tmpTileCount - 8 >= 0) { left = true; tmpTileCount -= 8; }
					if(tmpTileCount - 4 >= 0) { bottom = true; tmpTileCount -= 4; }
					if(tmpTileCount -2 >= 0) { right = true; tmpTileCount -= 2; }
					if(tmpTileCount > 0 ) top = true;
												
						
					for(int xx=0;xx<textureSize;xx++)
						for(int yy=0;yy<textureSize;yy++)
						{
								
							if(xx < 3 && left == false )
								intColor = getColorForPixmap(tileMap[i][j].biome.ordinal(), tileMap[i][j].Left.biome.ordinal(), xx, yy, textureSize, 3);
							else if(xx >= textureSize-3 && right == false )
								intColor = getColorForPixmap(tileMap[i][j].biome.ordinal(), tileMap[i][j].Right.biome.ordinal(), xx, yy, textureSize, 1);
							else if(yy < 3 && top == false )
								intColor = getColorForPixmap(tileMap[i][j].biome.ordinal(), tileMap[i][j].Top.biome.ordinal(), xx, yy, textureSize, 0);
							else if(yy >= textureSize-3 && bottom == false)
								intColor = getColorForPixmap(tileMap[i][j].biome.ordinal(), tileMap[i][j].Bottom.biome.ordinal(), xx, yy, textureSize, 2);
							else
							{
								if(val > 0)
									intColor = worldPixmap.getPixel(16*val + xx, 16 + yy);
								else
									intColor = biomesPixmap.getPixel(val, 0);
							}
																	
							map.drawPixel(tileSize*x+xx, tileSize*y+yy, intColor);
						}		
				}
					
				//create forest
				if(tileMap[i][j].biome.ordinal() >= 4 && tileMap[i][j].biome.ordinal() <= 7 && tileMap[i][j].tree == true)
				{
					int bitmask = tileMap[i][j].treeBitmask;
					map.drawPixmap(worldPixmap,  x*tileSize, y*tileSize, 16 * bitmask, 192 + 16 * (tileMap[i][j].biome.ordinal() - 4), 16, 16);
				}
				else if(tileMap[i][j].biome.ordinal() >= 12)
				{
					int bitmask = tileMap[i][j].biomeBitmask;
					if(tileMap[i][j].biome == biomeType.Desert_Mountains || tileMap[i][j].biome == biomeType.Mountains)
						map.drawPixmap(worldPixmap,  x*tileSize, y*tileSize, 16 * bitmask, 160 , 16, 16);
					else 
						map.drawPixmap(worldPixmap,  x*tileSize, y*tileSize, 16 * bitmask, 176 , 16, 16);
				}
				else if(tileMap[i][j].biome.ordinal() >= 2 && tileMap[i][j].biome != biomeType.Snow &&
						tileMap[i][j].biome != biomeType.Bitch && tileMap[i][j].capitol == false)
				{
					if(random.nextInt(100) < 20)
					{
						int objWidth = getObjectWidth(tileMap[i][j].biome);
						int objHeight = getObjectHeight(tileMap[i][j].biome);
						int objStartTextureX = getObjectStartTextureX(tileMap[i][j].biome);
						int objMaxTextureID = getObjectMaxTextureID(tileMap[i][j].biome);
						
						int maxX = tileSize - objWidth;
						int maxY = tileSize - objHeight;
						int objID = random.nextInt(objMaxTextureID);
						int startX = random.nextInt(maxX);
						int startY = random.nextInt(maxY);
						
						
						map.drawPixmap(worldPixmap,  x*tileSize + startX , y*tileSize + startY ,
								objStartTextureX + ( objID* objWidth ) , 32 + tileSize - objHeight , objWidth, objHeight);
							
						
						
						
						
					}
				}
				
				
				
				if(tileMap[i][j].capitol == true)
				{
					map.setColor(1, 0, 0.07f * tileMap[i][j].playerID, 1);
					map.fillRectangle(x*tileSize, y * tileSize, 16, 16);	
				}	
				
			}
		return map;
	}
	
	
	public void createMap(final MapTile[][] tileMap)
	{
		final int chunkSizeX = width/chunkSize;
		final int chunkSizeY = height/chunkSize;
		
		chunkManager = new ChunkManager(chunkSizeX, chunkSizeY, chunkSize);
			
	//	isChunkGenerated = new boolean[chunkSizeY][chunkSizeX];
		isGeneratingStarted = true;
		
		chunkPixmap = new Pixmap[chunkSizeY][chunkSizeX];
		chunkTexture = new Texture[chunkSizeY][chunkSizeX];
		readyMap = new Pixmap(width * tileSize, height*tileSize, Format.RGBA8888);
		
		for(int i=0;i<chunkSizeY;i++)
		{
			for(int j=0;j<chunkSizeX;j++)
			{
				final int startX = j*chunkSize;
				final int startY = i*chunkSize;
				final int chunkX = j;
				final int chunkY = i;
		//		isChunkGenerated[i][j] = false;
								
				
				Thread chunkBiomeDrawer = new Thread(new Runnable() 
				{						
					public void run() 
					{							
					//	chunkPixmap[chunkY][chunkX] = putBiomesOnMap(chunkPixmap[chunkY][chunkX], startX, startY, chunkSize, tileMap);	
						chunkManager.getChunk(chunkX, chunkY).setPixmap( putBiomesOnMap(chunkPixmap[chunkY][chunkX], startX, startY, chunkSize, tileMap) );
					//	readyMap.drawPixmap(chunkPixmap[chunkY][chunkX], startX * tileSize, startY * tileSize);
				//		isChunkGenerated[chunkY][chunkX] = true;					
					}});
				chunkBiomeDrawer.start();
				
			}
		}
		
	}
	
	public Pixmap getMap()
	{
		return readyMap;
	}
	
	public Pixmap getChunk(int x, int y)
	{
		return chunkPixmap[y][x];
	}
	public Texture getChunkTexture(int x, int y)
	{
		return chunkTexture[y][x];
	}
	
	
	
	public int getChunkWidth(){return width/chunkSize;}
	public int getChunkHeight(){return height/chunkSize;}
	
	
	public boolean isMapGenerated()
	{
		boolean generated = true;
	/*	for(int i=0;i<isChunkGenerated.length;i++)
			for(int j=0;j<isChunkGenerated[0].length;j++)
				if(!isChunkGenerated[i][j]) return false;
		
		for(int i=0;i<isChunkGenerated.length;i++)
			for(int j=0;j<isChunkGenerated[0].length;j++)
			{
				chunkTexture[i][j] = new Texture(chunkPixmap[i][j]);
				chunkTexture[i][j].setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
			}*/
		
	for(int i=0;i<chunkManager.chunkHeight;i++)
		for(int j=0;j<chunkManager.chunkWidth;j++)
		{
			if(!chunkManager.getChunk(j, i).isPixmap) { if(generated == true) generated = false; }
			else if(chunkManager.getChunk(j, i).textureCreated == false )
			{
				generated = false;
				chunkManager.getChunk(j, i).createTeture();
			}
			
		}
		
		if(generated == true)
			isMapGenerated = true;
		
		return generated;		
	}
	
	public void clearAfterGeneration()
	{
		for(int i=0;i<chunkPixmap.length;i++)
			for(int j=0;j<chunkPixmap[0].length;j++)
			{
				chunkPixmap[i][j].dispose();				
			}
				
	}
	
	private Pixmap setTextureTile(Pixmap map, int x, int y, int val, int debugType)
	{
	/*	if(val < 2)
			color.set(0.0f,0.2f,0.8f,1);
		else if(val < 4)
			color.set(0.85f,0.8f,0.0f,1);
		else 
			color.set(0.2f,0.8f,0.0f,1);*/
		if(debugType == 1)
			color.set(heightPixmap.getPixel(val, 0));
		else if(debugType == 2)
			color.set(temperaturePixmap.getPixel(val, 0));
		else 
			color.set(humidityPixmap.getPixel(val, 0));
		
		map.setColor(color);
		map.fillRectangle(x*tileSize, y*tileSize, tileSize, tileSize);
		
		
		return map;
	}
	
	private int getColorForPixmap(int srcVal, int neighboorVal, int x, int y,int textureSize, int direction) //0-top 1-right 2-bottom 3-left
	{
		int color = 0;
		int chance = 0, los;
		boolean firstPixel = false;
		
		if(direction == 0 && y<3)
		{
			chance = 50 / (1<<y);	
			if(y==0) firstPixel = true;
		}
		else if(direction == 1 && x >= textureSize - 3) 
		{
			chance = 50 / (1<<(textureSize - x));
			if(x == textureSize - 1) firstPixel = true;
		}
		else if(direction == 2 && y >= textureSize - 3)
		{
			chance = 50 / (1<<(textureSize - y));
			if(y == textureSize - 1) firstPixel = true;
		}
		else if(direction == 3 && x<3)
		{
			chance = 50 / (1<<x);
			if(x==0) firstPixel = true;
		}
		
		los = generator.nextInt(100);
		
		if(neighboorVal > 0 && srcVal != 0)
		{
			if(los < chance)
				color = worldPixmap.getPixel(16*neighboorVal + x, 16 + y);
			else
				color = worldPixmap.getPixel(16*srcVal + x, 16 + y);
		}
		else if(neighboorVal == 0)
		{
			int dank = Integer.parseInt("00000000001000000010000000100000", 2);
			
			color = (worldPixmap.getPixel(16*srcVal + x, 16 + y) - dank);
			
			chance *= 4;
			if(los < chance)		
				color =(worldPixmap.getPixel(16*srcVal + x, 16 + y) - dank);
			else 
				color = worldPixmap.getPixel(16*srcVal + x, 16 + y);
		}
		else
		{		
			int dank = Integer.parseInt("00000000001000000010000000100000", 2);
			
			if(firstPixel)		
				chance = (chance * 5)/3;
			else chance *= 3;
			
			if(los < chance)
				color = (worldPixmap.getPixel(16*neighboorVal + x, 16 + y) - dank);
			else 
				color = biomesPixmap.getPixel(0, 0);
			
		}
		
		return color;
	}
	
	private int getIntegerMapTileValue(float tileValue, int maxValue)
	{
		return (int) (tileValue * maxValue);
	}
	
	private int getObjectWidth(biomeType biome)
	{
		if(biome == biomeType.Savanna)
			return 10;
		else return 5;
		
	}
	private int getObjectHeight(biomeType biome)
	{
		if(biome == biomeType.Savanna || biome == biomeType.Desert)
			return 5;
		else return 8;
	}
	private int getObjectStartTextureX(biomeType biome)
	{
		if(biome == biomeType.Savanna)
			return 0;
		else if(biome == biomeType.Desert)
			return 64;
		else if(biome == biomeType.Tajga || biome == biomeType.Tundra)
			return 144;
		else return 96;
	}
	private int getObjectMaxTextureID(biomeType biome)
	{
		if(biome == biomeType.Savanna || biome == biomeType.Desert)
			return 6;
		else if(biome == biomeType.Tajga || biome == biomeType.Tundra)
			return 8;
		else return 7;
	}
	
	public Pixmap debugGroup( MapTile[][] tileMap)
	{
		Random random = new Random();
		Pixmap map = new Pixmap(width * tileSize, height*tileSize, Format.RGBA8888);
////////////////////DEBUG
		for(int i=0;i<tileMap.length;i++)
			for(int j=0;j<tileMap[0].length;j++)
			{
				if(tileMap[i][j].groupID != -1)
				{
					boolean znaleziono = false;
					
					for(int k=0;k<grupy.size() && znaleziono == false;k++)
						if(tileMap[i][j].groupID == grupy.get(k))
							{
								znaleziono = true;
								map.setColor(kolory.get(k));	
								licznik.set(k, licznik.get(k)+1);
								
							}
					
					if(znaleziono == false)
					{
						grupy.add(tileMap[i][j].groupID);
						kolory.add(new Color(random.nextFloat(), random.nextFloat(), random.nextFloat(), 1));		
						licznik.add(1);
							
					}
					
					map.fillRectangle(j*tileSize, i * tileSize, 16, 16);
				}
			}
	
		return map;
	}
	
}
