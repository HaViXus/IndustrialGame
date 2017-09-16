package Packets;

import java.io.Serializable;

public class ClientGeneratingPacket implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	public boolean mapReady;
	public boolean mapReceived;
	public long sendTime;
	
}
