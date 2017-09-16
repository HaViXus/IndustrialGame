package map_generator;

import java.io.Serializable;

import map_generator.mapInfo.biomeType;
import map_generator.mapInfo.buildingType;
import map_generator.mapInfo.resourceType;
import map_generator.mapInfo.superBuildingType;

public class MapTile implements Serializable
{

	private static final long serialVersionUID = 1L;
	
	public boolean isBuilding = false;
	public boolean isSuperBuilding = false;
	public boolean occuped = false;
	public boolean tree = false;
	public boolean capitol = false;
	public int playerID = -1;
	public int population = 1000;
	public int x, y;
	public int groupID = -1;
	
	
	public biomeType biome;
	public resourceType resouce = resourceType.None;
	public buildingType building = buildingType.None;
	public superBuildingType superBuilding = superBuildingType.None;
	
	public SmallTileInfo Left;
	public SmallTileInfo Right;
	public SmallTileInfo Top;
	public SmallTileInfo Bottom;
	
	public int biomeBitmask;
	public int treeBitmask;
	
	public void UpdateBitmask()
	{
		biomeBitmask = 0;
	    treeBitmask = 0;
	     
	    if (Top.biome == biome)
	    {
	    	biomeBitmask += 1;
	    	if(biome.ordinal()>=4 && biome.ordinal()<8 && tree == true && Top.tree == true)
	    		treeBitmask += 1;
	    	
	    }
	        
	    if (Right.biome == biome)
	    {
	    	biomeBitmask += 2;
	    	if(biome.ordinal()>=4 && biome.ordinal()<8 && tree == true && Right.tree == true)
	    		treeBitmask += 2;
	    	
	    }
	        
	    if (Bottom.biome == biome)
	    {
	    	biomeBitmask += 4;
	    	if(biome.ordinal()>=4 && biome.ordinal()<8 && tree == true && Bottom.tree == true)
	    		treeBitmask += 4;
	    
	    }
	       
	    if (Left.biome == biome)
	    {
	    	biomeBitmask += 8;
	    	if(biome.ordinal()>=4 && biome.ordinal()<8 && tree == true && Left.tree == true)
	    		treeBitmask += 8;
	    	
	    }

	}
	
	public SmallTileInfo convertToSmallTileInfo()
	{
		SmallTileInfo info = new SmallTileInfo();
		info.biome = this.biome;
		info.biomeBitmask = this.biomeBitmask;
		info.treeBitmask = this.treeBitmask;
		info.tree = this.tree;
		return info;
	}

		
}
