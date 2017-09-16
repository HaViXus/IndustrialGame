package Algorithms;

import java.util.ArrayList;

import Vector.Vector2i;
import map_generator.MapTile;
import map_generator.TerrainGroup;

public class FloodFillCross 
{
	private Queue<Vector2i> queue;
	
	public FloodFillCross()
	{
		queue = new Queue<Vector2i>();
	}

	public int mapTerrainCounter(MapTile[][] map, int startX, int startY)
	{
		int counter = 0;
		Vector2i tmp = new Vector2i(startX, startY);
		boolean[][] checked = new boolean[map.length][map[0].length];
		queue.push(tmp);
		
		while(!queue.isEmpty())
		{
			tmp = queue.pull();
			
			if(tmp.x < 0 || tmp.x >= map[0].length) continue;
			if(tmp.y <0 || tmp.y >= map.length) continue;
			
			if(map[tmp.y][tmp.x].biome.ordinal() >= 2 && checked[tmp.y][tmp.x] == false)
			{
				checked[tmp.y][tmp.x] = true;
				counter++;
				//DOWN							
				queue.push(new Vector2i(tmp.x,tmp.y+1));							
				//TOP
				queue.push(new Vector2i(tmp.x,tmp.y-1));
				///LEFT
				queue.push(new Vector2i(tmp.x-1,tmp.y));							
				//RIGHT
				queue.push(new Vector2i(tmp.x+1,tmp.y));
			}
		}
		return counter;
	}
	
	public boolean isCapitolOnCorrectIsland(MapTile[][] map, int startX, int startY, int minTile)
	{
		int counter = 0;
		Vector2i tmp = new Vector2i(startX, startY);
		boolean[][] checked = new boolean[map.length][map[0].length];
		queue.push(tmp);
		
		while(!queue.isEmpty())
		{
			tmp = queue.pull();
			
			if(tmp.x < 0 || tmp.x >= map[0].length) continue;
			if(tmp.y <0 || tmp.y >= map.length) continue;
			if(map[tmp.y][tmp.x].biome.ordinal() >= 12) continue;
			
			if(map[tmp.y][tmp.x].biome.ordinal() >= 2 && checked[tmp.y][tmp.x] == false)
			{
				checked[tmp.y][tmp.x] = true;
				counter++;
				//DOWN							
				queue.push(new Vector2i(tmp.x,tmp.y+1));							
				//TOP
				queue.push(new Vector2i(tmp.x,tmp.y-1));
				///LEFT
				queue.push(new Vector2i(tmp.x-1,tmp.y));							
				//RIGHT
				queue.push(new Vector2i(tmp.x+1,tmp.y));
			}
			
			if(counter >= minTile)	
				return true;
			
		}
		return false;
	}
	
	public ArrayList<TerrainGroup> getTerrainGroups(MapTile[][] map, int islandSize)
	{
		boolean[][] checked = new boolean[map.length][map[0].length];
		ArrayList<TerrainGroup> groups = new ArrayList<TerrainGroup>();
		
		for(int i=0;i<map.length;i++)
			for(int j=0;j<map[0].length;j++)
				if(checked[i][j] == false && map[i][j].biome.ordinal()>=2)
				{
					TerrainGroup tmp = new TerrainGroup();
					tmp.setGroup(getTerrainGroup(map, j, i, checked)); 			
					tmp.setSize(tmp.getGroup().size(), islandSize);
					groups.add(tmp);
				}
					
		return groups;
	}
	
	private ArrayList<MapTile> getTerrainGroup(MapTile[][] map, int startX, int startY, boolean[][] checked)
	{
		ArrayList<MapTile> group = new ArrayList<MapTile>();
		Vector2i tmp = new Vector2i(startX, startY);
		queue.push(tmp);
		int counter = 1;
		while(!queue.isEmpty())
		{
			tmp = queue.pull();
			
			if(tmp.x < 0 || tmp.x >= map[0].length) continue;
			if(tmp.y <0 || tmp.y >= map.length) continue;
			if(map[tmp.y][tmp.x].biome.ordinal() >= 12) continue;

			if(checked[tmp.y][tmp.x] == false)
			{
				checked[tmp.y][tmp.x] = true;	
				if(map[tmp.y][tmp.x].biome.ordinal() >= 2 )
				{
					group.add(map[tmp.y][tmp.x]);
					counter++;
					//TOP
					queue.push(new Vector2i(tmp.x,tmp.y-1));
					//TOP-RIGHT
					queue.push(new Vector2i(tmp.x+1,tmp.y-1));
					//RIGHT
					queue.push(new Vector2i(tmp.x+1,tmp.y));
					//RIGHT-DOWN
					queue.push(new Vector2i(tmp.x+1,tmp.y+1));
					//DOWN							
					queue.push(new Vector2i(tmp.x,tmp.y+1));	
					//DOWN-LEFT							
					queue.push(new Vector2i(tmp.x-1,tmp.y+1));	
					///LEFT
					queue.push(new Vector2i(tmp.x-1,tmp.y));	
					///LEFT-UP
					queue.push(new Vector2i(tmp.x-1,tmp.y-1));
				}			
			}
		}
		
		System.out.println("GROUP SIZE:" + counter);
		return group;
	}
	
	
}
