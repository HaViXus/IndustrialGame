package Packets;

import java.io.Serializable;

//CLIENT

public class ClientWaitingPacket implements Serializable
{
	private static final long serialVersionUID = 1L;
//	private Array<Integer> array;

	public Integer PlayerID;
	public long SendTime;
	public boolean Ready;
	
	

	
}
