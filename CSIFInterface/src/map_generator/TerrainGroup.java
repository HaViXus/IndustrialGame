package map_generator;

import java.util.ArrayList;

public class TerrainGroup 
{

	private ArrayList<MapTile> group;
	public boolean isIsland;
	public int size;
	public int groupID;
	
	public TerrainGroup()
	{
		group = new ArrayList<MapTile>();
	}
	public TerrainGroup(int size, int islandSize)
	{
		this();
		setSize(size, islandSize);
		
	}
	public void setSize(int size, int islandSize)
	{
		if(size <= islandSize) isIsland = true;
		else isIsland = false;
		this.size = size;
	}
	public void setGroup(ArrayList<MapTile> group){ this.group = group; }
	public ArrayList<MapTile> getGroup(){ return group; }
	public MapTile getMapTile(int x, int y)
	{
		for(int i=0;i<group.size();i++)
			if(group.get(i).x == x && group.get(i).y == y)
				return group.get(i);
		return null;
	}
	public void setGroupID(int ID)
	{
		for(int i=0;i<group.size();i++)
			group.get(i).groupID = ID;
	}
	
	
	
}
