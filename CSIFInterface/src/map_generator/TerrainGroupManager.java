package map_generator;

import java.util.ArrayList;

public class TerrainGroupManager 
{

	private ArrayList<TerrainGroup> groups;
	
	public TerrainGroupManager() 	
	{
		groups = new ArrayList<TerrainGroup>();
	}
	
	public void putGroupToManager(TerrainGroup group)
	{
		group.groupID = groups.size();
		group.setGroupID(group.groupID);
		groups.add(group);
	}
	public TerrainGroup getGroup(int ID)
	{
		return groups.get(ID);
	}
	
}
