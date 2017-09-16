package Packets;

import java.io.Serializable;

import map_generator.MapTile;

public class ServerMapPacket implements Serializable
{

	private static final long serialVersionUID = 1L;
	
	public float map[][];
	public float temperature[][];
	public float humadity[][];
	public MapTile tileMap[][];
	
	
	
}
