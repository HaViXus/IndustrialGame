package map_generator;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;
import java.util.Vector;

import Algorithms.FloodFillCross;
import Vector.Vector2i;
import map_generator.mapInfo.biomeType;

public class MapGenerator 
{

	private int width, height;
	private int seed;
	private int seaLevel = 18;
	private int islandSize;
	
	
	private Random random;
	
	public MapGenerator(int width, int height, int seed)
	{
		this.width = width;
		this.height = height;
		this.seed = seed;		
		
		random = new Random(seed);		
		
	}
	
	public float[][] generateRandomPoints(float[][] noise)
	{
		noise = getEmptyArray(width, height);
		
		for(int i=0;i<height;i++)
			for(int j=0;j<width;j++)
				noise[i][j] = random.nextFloat();	
		
		return noise;
	}
	
	private float[][] generateTerrainPoints(float[][] terrainBase, int heightLevel, int terrainType)
	{
		/*********************************************
		 * levelType:
		 * 0-Random type
		 * 
		 *********************************************/
		
	//	terrainBase = getEmptyArray(width, height);
		float delta = 0.1f;
		
		for(int i=0;i<height;i++)
			for(int j=0;j<width;j++)
			{
				if(terrainType == 0)
				{
					terrainBase[i][j] = (random.nextFloat() + heightLevel * 0.1f) + random.nextInt(2)*(-1)*random.nextFloat()*delta;
					if(terrainBase[i][j] > 1) terrainBase[i][j] = 1;
					else if(terrainBase[i][j] < 0) terrainBase[i][j] = 0;
					
				}
				
			}
				
		
		return terrainBase;
	}
	
	private float[][] generateTemperaturePoints(float[][] temperatureBase, float[][] heightMap, int temperatureLevel)
	{
		
		float delta = 0.2f;
		int equatorSize = (height/35) + (height/35)*temperatureLevel;
		int equatorMin = height/2 - equatorSize;
		int equatorMax =  height/2 + equatorSize;
		
		float heightChance = 0;
		
		for(int i=0;i<height;i++)
		{
			
			if(i<=equatorMin)			
				heightChance = i/(float)(height/2) + 0.05f * temperatureLevel;
			else if( i >= equatorMax )		
				heightChance = (height-i)/(float)(height/2) + 0.05f * temperatureLevel;

			for(int j=0;j<width;j++)
			{				
				
				if(heightChance > 0.25)
					temperatureBase[i][j] = heightChance + random.nextFloat() * delta + 0.05f * temperatureLevel;
				else
					temperatureBase[i][j] = heightChance + random.nextFloat() * (delta/2.0f);
					
				if(heightMap[i][j] > 0.4f)
					temperatureBase[i][j] *= 1.4f - heightMap[i][j];
			
				if(temperatureBase[i][j] > 1) temperatureBase[i][j] = 1;
				else if(temperatureBase[i][j] < 0) temperatureBase[i][j] = 0;
			}
		}
		
		
		return temperatureBase;
	}
	
	private float[][] generateHumadityPoints(float[][] humadityBase, float[][]heightMap, int humadityLevel, float delta)
	{
			
		Vector2i humidityPosition = new Vector2i();
		Stack<Vector2i> humidityStack = new Stack<Vector2i>();
		
		float lastHumadity = 0;	
		
		for(int i=0;i<height;i++)
		{
			for(int j=0;j<width;j++)
			{
				if(heightMap[i][j] <= seaLevel)
				{
					humidityPosition.lastHumidity = 1.0f * (1.0f - heightMap[i][j]);
					humidityPosition.x = j;
					humidityPosition.y = i;
					humidityStack.push(humidityPosition);
					
					while(!humidityStack.isEmpty())
					{
						
						humidityPosition = humidityStack.pop();
						int x = humidityPosition.x;
						int y = humidityPosition.y;
						lastHumadity = humidityPosition.lastHumidity;
						
						if( x<0 || x >= width ) continue;
						if( y<0 || y >= height ) continue;					
						
						float tmpHumidity = 0;
						float seaHumidity = 0;
						float heightFactor  = 1.0f - heightMap[y][x];
						
						
						 if(heightMap[y][x] <= seaLevel) 	
								seaHumidity = 1.0f * heightFactor;//getHumadityFromWather(heightMap[y][x]);
												
						
						tmpHumidity = (lastHumadity - (delta*(lastHumadity/1.0f)))*heightFactor;
						if(seaHumidity > tmpHumidity) tmpHumidity = seaHumidity;
																
						if(tmpHumidity < 0) tmpHumidity = 0;
						
						if(tmpHumidity > humadityBase[y][x])
						{
							
							humadityBase[y][x] = tmpHumidity;
							humidityPosition.lastHumidity = tmpHumidity;						
							
						    //DOWN							
							humidityStack.push(new Vector2i(x,y+1,tmpHumidity));							
							//TOP
							humidityStack.push(new Vector2i(x,y-1,tmpHumidity));
							///LEFT
							humidityStack.push(new Vector2i(x-1,y,tmpHumidity));							
							//RIGHT
							humidityStack.push(new Vector2i(x+1,y,tmpHumidity));
								
						}	
					}	
				}		
			}
		}
			
		return humadityBase;
	}
	
	private float[][] generateSmoothNoise(float[][] baseNoise, int octave)
	{
		
		float[][] smoothNoise = getEmptyArray(width, height);
		
		int samplePeriod = 1<<octave;
		float sampleFrequency = 1.0f/samplePeriod;
		
		for(int i=0;i<height;i++)
		{
			int sample_i0 = (i / samplePeriod) * samplePeriod;
		    int sample_i1 = (sample_i0 + samplePeriod) % height;
		    float horizontal_blend = (i - sample_i0) * sampleFrequency;
		    
		    for (int j = 0; j < width; j++)
		      {
		         //calculate the vertical sampling indices
		         int sample_j0 = (j / samplePeriod) * samplePeriod;
		         int sample_j1 = (sample_j0 + samplePeriod) % width; 
		         float vertical_blend = (j - sample_j0) * sampleFrequency;
		 
		         //blend the top two corners
		         float top = CosineInterpolate(baseNoise[sample_i0][sample_j0],
		            baseNoise[sample_i1][sample_j0], horizontal_blend);
		 
		         //blend the bottom two corners
		         float bottom = CosineInterpolate(baseNoise[sample_i0][sample_j1],
		            baseNoise[sample_i1][sample_j1], horizontal_blend);
		 
		         //final blend
		         smoothNoise[i][j] = CosineInterpolate(top, bottom, vertical_blend);
		      }
		}
		
		
		return smoothNoise;
		
	}
	
	private float CosineInterpolate(float y1,float y2,float mu)
	{
		float mu2 =(float)(1-Math.cos(mu*Math.PI))/2;	
		return(y1*(1-mu2)+y2*mu2);
	}
	
	public float[][] generatePerlinNoise(float baseNoise[][], int octaveCount, float per, float amp)
	{
		
		float[][][] smoothNoise = new float[octaveCount][][];
		
		float persistance = per;
		
		for(int i=0;i<octaveCount;i++)	
			smoothNoise[i] = generateSmoothNoise(baseNoise, i);
		
		float[][] perlinNoise = getEmptyArray(width, height);
		float amplitude = amp;
		float totalAmplitude = 0.0f;
		
		
		for(int octave = octaveCount - 1; octave >=0; octave--)
		{
			amplitude *= persistance;
		    totalAmplitude += amplitude;
		    
		    for(int i=0; i<height; i++)
		    	for(int j=0; j<width; j++)
		    		perlinNoise[i][j]+=smoothNoise[octave][i][j] * amplitude;
		}
		
		for (int i = 0; i < height; i++)		   
		      for (int j = 0; j < width; j++)   
		         perlinNoise[i][j] /= totalAmplitude;
		      
		 return perlinNoise;
		
	}
	
	public void createBiomes(MapTile[][] map, float[][] fMap, float[][] temperature, float[][] humidity)
	{
		
		biomeType biomeTable[][] = 
			{
					//COLDEST        //COLDER          //COLD            //NORMAL           //HOT              	   //HOTTER          	  HOTTEST           	 //APOCALIPSE
					{biomeType.Snow, biomeType.Snow,   biomeType.Snow,   biomeType.Plain,   biomeType.Plain, 	   biomeType.Savanna, 	  biomeType.Desert,  	 biomeType.Desert}, 	 //DRYEST
					{biomeType.Snow, biomeType.Snow,   biomeType.Tundra, biomeType.Plain,   biomeType.Plain,   	   biomeType.Savanna, 	  biomeType.Desert,  	 biomeType.Desert}, 	 //DRYER
					{biomeType.Snow, biomeType.Tundra, biomeType.Tajga,  biomeType.Plain,   biomeType.Plain,  	   biomeType.Savanna,     biomeType.Desert,      biomeType.Desert}, 	 //DRY
					{biomeType.Snow, biomeType.Tundra, biomeType.Tajga,  biomeType.Forest,  biomeType.Forest, 	   biomeType.Thicket,     biomeType.Desert,      biomeType.Desert}, 	 //NORMAL
					{biomeType.Snow, biomeType.Tundra, biomeType.Tajga,  biomeType.Forest,  biomeType.Forest, 	   biomeType.Rain_Forest, biomeType.Savanna,     biomeType.Desert}, 	 //WET
					{biomeType.Snow, biomeType.Tundra, biomeType.Tajga,  biomeType.Thicket, biomeType.Thicket,	   biomeType.Rain_Forest, biomeType.Rain_Forest, biomeType.Savanna}, 	 //WETTER
					{biomeType.Snow, biomeType.Tajga,  biomeType.Tajga,  biomeType.Thicket, biomeType.Rain_Forest, biomeType.Rain_Forest, biomeType.Rain_Forest, biomeType.Savanna}, 	 //WETTEST
					{biomeType.Snow, biomeType.Tajga,  biomeType.Tajga,  biomeType.Thicket, biomeType.Rain_Forest, biomeType.Rain_Forest, biomeType.Rain_Forest, biomeType.Rain_Forest}  //WBA
			};
			
		int temperatureScale;
		int humidityScale;
		
		for(int i=0;i<height;i++)
			for(int j=0;j<width;j++)
			{
				temperatureScale = (int)((temperature[i][j] * 31)/4);
				humidityScale = (int)((humidity[i][j] * 31)/4);			
				map[i][j] = new MapTile();
				map[i][j].x = j;
				map[i][j].y = i;
				
				map[i][j].biome = biomeTable[humidityScale][temperatureScale];
				if(map[i][j].biome.ordinal() >=4 && map[i][j].biome.ordinal() <8)
				{
					if(random.nextInt(100) >= 2)
						map[i][j].tree = true;
				}
				
				if(fMap[i][j] * 31 >= 23)
				{
					if(random.nextInt() >= 20)
					{
						if(map[i][j].biome == biomeType.Snow)
							map[i][j].biome = biomeType.Snow_Mountains;
						else if(map[i][j].biome == biomeType.Desert)
							map[i][j].biome = biomeType.Desert_Mountains;
						else 
							map[i][j].biome = biomeType.Mountains;
					}	
				}
				
				else if(fMap[i][j] * 31 <= seaLevel )
				{
					if(map[i][j].biome == biomeType.Snow)
						map[i][j].biome = biomeType.Frezee_Water;
					else
						map[i][j].biome = biomeType.Water;
				}
					
				else if(fMap[i][j] * 31 <= seaLevel + 1 && map[i][j].biome != biomeType.Snow)
				{
					map[i][j].biome = biomeType.Bitch;					
				}
					
				
			}
		
	}
	
	public float[][] generateMap(MapTile[][] map, float[][] fMap, float[][] temperature, float[][] humidity,
			int mapWidth, int mapHeight, int heightLevel, int seaLevel, int temperatureLevel, int humadityLevel, 
			int terrainvariety, int islandSize, int players)
	{
		
		this.width = mapWidth;
		this.height = mapHeight;
		this.islandSize = islandSize;
		
		fMap = generateTerrainPoints(fMap, 0, 0);
		fMap = generatePerlinNoise(fMap, 6, 0.5f, 0.5f);

		temperature = generateTemperaturePoints(temperature, fMap, temperatureLevel);
		temperature = generatePerlinNoise(temperature, 6, 0.5f, 1f);
		
		humidity = generateHumadityPoints(humidity, fMap, humadityLevel, 0.05f);		
		
		createBiomes(map, fMap, temperature, humidity);
		createTileNeighbors(map);
		
		generateCapitals(fMap, map, players);
		
		return fMap;
	}
	
	public void generateCapitals(float[][] fMap, MapTile[][] map, int players)
	{
		int attempts = 0, maxAttempts=10;
		boolean created = false;	
		int minDistance = getMinDistance();
		int x[] = new int[players]; 
		int y[] = new int[players];
		FloodFillCross floodFillCross = new FloodFillCross();
		
		while(created == false)
		{
			
			boolean correct = true;
			for(int i=0;i<players;i++)
			{
				boolean playerCapitolCreated = false;
				while(playerCapitolCreated == false && attempts < 10)
				{
					correct = true;
					boolean correctPosition = false;
					
					while(correctPosition == false)
					{
						x[i] = random.nextInt(width);
						y[i] = (int)(height*0.13f) + random.nextInt(height-(int)(0.26f*height));
						boolean terrainError = false;
						
						if(x[i] >= width-1 || y[i] >= height-1)
							terrainError = true;
						else
						{
							for(int xx=0;xx<2 && terrainError == false;xx++)
								for(int yy=0;yy<2 && terrainError == false;yy++)
									if(fMap[y[i] + yy][x[i] + xx] * 31 > seaLevel && fMap[y[i] + yy][x[i] + xx] * 31 < 23 
											&& floodFillCross.isCapitolOnCorrectIsland(map, x[i], y[i], 20))
										correctPosition = true;	
									else terrainError = true;
						}
						
					}			
					
					for(int j=i-1;j>=0;j--)
					{
						
						if(!(distance(x[i], x[j], y[i], y[j]) >= minDistance))
						{
							attempts++;
							correct = false;
							break;					
						}
					}
					
					if(correct == true)
					{
						playerCapitolCreated = true;						
						if(i==players-1)
							created = true;
						attempts = 0;
					}			
				
				}
				if(attempts > maxAttempts)
					break;
				
			}
		}
		
		for(int i=0;i<players;i++)
		{
			map[y[i]][x[i]].capitol = true;
			map[y[i]][x[i]].occuped = true;
			map[y[i]][x[i]].playerID = i;
		}
		
	}
	
	private int distance(int x1, int x2, int y1, int y2)
	{
		int distance = (int) (Math.sqrt(Math.pow(x1-x2,2) + Math.pow(y1-y2, 2)));
		return distance;
	}
	
	private int getMinDistance()
	{
	
		if(width == 128) return 10;
		else return 10;
	}
	
	private void createTileNeighbors(MapTile[][] map)
	{
			
		for(int y=0;y<height;y++)
			for(int x=0;x<width;x++)
			{
				map[y][x].Left = map[y][ Math.floorMod(x - 1, width)].convertToSmallTileInfo();			
				map[y][x].Right = map[y][ Math.floorMod(x + 1, width)].convertToSmallTileInfo();				
			    map[y][x].Top = map[Math.floorMod(y - 1, height)][x].convertToSmallTileInfo();	
				map[y][x].Bottom = map[Math.floorMod(y + 1, height)][x].convertToSmallTileInfo();	
				
				map[y][x].UpdateBitmask();
			}
//TO DO
		
	}
	
	public void createTerrainGroups(MapTile[][] map)
	{
		TerrainGroupManager manager = new TerrainGroupManager();
		ArrayList<TerrainGroup> groups; 
		FloodFillCross floodFillCross = new FloodFillCross();
		groups = floodFillCross.getTerrainGroups(map, islandSize);
		for(int i=0; i<groups.size(); i++)
			manager.putGroupToManager(groups.get(i));
		
		
			
		
	}
	
	private float[][] getEmptyArray(int width, int height)
	{
		float[][] array = new float[height][width];
		
		for(int i=0;i<height;i++)
			for(int j=0;j<width;j++)
				array[i][j]=0;
				
		return array;
	}
	
	public int getWidth(){ return width; }
	public int getHeight(){ return height; }
	
	
}
