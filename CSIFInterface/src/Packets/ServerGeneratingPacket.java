package Packets;

import java.io.Serializable;

public class ServerGeneratingPacket implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	public long sendTime;
	public boolean[] playersReady;
	
}
