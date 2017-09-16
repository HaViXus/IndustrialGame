package map_generator;

import java.io.Serializable;

import map_generator.mapInfo.biomeType;

public class SmallTileInfo implements Serializable
{

	private static final long serialVersionUID = 1L;
	public biomeType biome;
	public int biomeBitmask;
	public int treeBitmask;
	public boolean tree;
	
}
