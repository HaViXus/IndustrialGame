package Packets;

import java.io.Serializable;

import client_server_interface.Server_info;



//SERVER

public class ServerWaitingPacket implements Serializable
{
	private static final long serialVersionUID = 1L;
//	private Array<Integer> array;


	public Integer PlayerID;
	public Integer PlayerNumber;
	public Integer MaxPlayers;
	public long SendTime;
	public Server_info.mode server_mode;
	public Server_info.status server_status;
	public Server_info.waiting_room_status waiting_room_status;
	
	
}
