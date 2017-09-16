package Multiplayer;

import java.io.ByteArrayInputStream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import javax.swing.JOptionPane;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import Packets.ClientGeneratingPacket;
import Packets.ClientWaitingPacket;
import Packets.ServerGeneratingPacket;
import Packets.ServerMapPacket;
import Packets.ServerWaitingPacket;
import assetsHandlers.MainAssetsManager;
import camera.GameCamera;
import client_server_interface.Server_info;
import client_server_interface.Server_info.mode;
import client_server_interface.Server_info.status;
import map.ChunkManager;
import map.Map;
import map_generator.MapTile;


public class MultiGame extends ApplicationAdapter
{

	private GameCamera camera;
	private Batch batch;

	private BitmapFont bFPS, serverDebugInfo;
	private String sFPSlog, stringServerDebugInfo;
	
	Socket socket;
	InetSocketAddress address;
	ObjectInputStream in_sock;
	Array <Sprite> PlayersSprites;
	Array <Vector2> playersPos;
	
	private int players = -1;
	private int myID;
	private long lastSendTime;
	private boolean connected;
	private boolean isReady;
	
	private boolean[] playersMapGenerated = null;
	
	Vector2 myPosition;
	double FPS;
	int Errors;
	
	Server_info.mode server_mode;
	
	Pixmap gameMap;
	Texture textureGameMap;
	Map mapHandler;
	
	boolean receivedMap, drawMap;
	MapTile[][] tileMap;
	float[][] fMap;
	float[][] fTemperature;
	float[][] fHumidity;
	int mapWidth, mapHeight;
	
	MainAssetsManager manager;
	
	
	
	
	//****************************************
	// DEBUGING
	boolean DEBUG = true;
	
	int viewMode; //0-Normal 1-Height 2-Temperature 3-Humidity
	Pixmap map;
	Pixmap temperature;
	Pixmap humidity;
	Texture textureMap;
	Texture textureTemperature;
	Texture textureHumidity;
	
	Pixmap groupPixmap;
	Texture groupTexture;
	
	// DEBUGING
	//****************************************
		
	//Array<Integer> array;
	
	
	public void create () {
		ScreenViewport viewport = new ScreenViewport();
		batch = new SpriteBatch();
		//camera = new GameCamera(1080, 720);
		camera = new GameCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		bFPS = new BitmapFont();
		serverDebugInfo = new BitmapFont();
		manager = new MainAssetsManager();
		manager.loadData();
		
		connected = false;
		isReady = false;
		viewMode = 0;
		
		ClientWaitingPacket clientWaitingPacket;
		ServerWaitingPacket serverWaitingPacket;
		
		lastSendTime = -1;
		
		playersPos = new Array<Vector2>();
		PlayersSprites = new Array<Sprite>();
		
		camera.update();			
	
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ByteArrayInputStream bis = null;
		
		mapHandler = new Map(128, 128, 16, manager);
		drawMap = false;
		receivedMap = false;
		
		
		try 
		{   
			socket = new Socket();
			address = new InetSocketAddress("localhost", 27015);
			socket.connect(address, 10000);
			//in_sock = new ObjectInputStream(socket.getInputStream());
			in_sock = new ObjectInputStream(socket.getInputStream());	
			
			int type = in_sock.readInt();
			
			serverWaitingPacket = (ServerWaitingPacket)in_sock.readObject();
			
			if(serverWaitingPacket.PlayerID == -1) // If player don't received player ID, show information
			{
				if(serverWaitingPacket.server_status == status.FULL)
					System.out.println("Server is full");
				else if(serverWaitingPacket.server_mode != mode.WAITING_ROOM)
					System.out.println("Game stared");
				else
					System.out.println("Cannot connec to server");
			}
			else //Player is connected
			{
				connected = true;
				
				myID = serverWaitingPacket.PlayerID;
				players = serverWaitingPacket.PlayerNumber;
				//***********************************************
				//SOME CODE WITH START INSTRUCTION - TO DO !!!
				//***********************************************
				
				
				
				receiverCreate();
				senderCreate();
			}	

		} 
		catch (Exception e) {System.out.println("Client: " + e); }
		
	}
	
	
	public synchronized void render ()
	{
		
		update();
		camera.updateResolution(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		//System.out.println(myPosition);
		batch.setProjectionMatrix(camera.combined);
		camera.update();
		
		batch.begin();
		
		if(connected == false) sFPSlog = "Ping: ----";
		else
		{
			if(isReady == true)
				Gdx.gl.glClearColor(0, 1, 0, 0.1f);
			else
				Gdx.gl.glClearColor(1, 0, 0, 0.1f);
			
			
		}
		
		if(drawMap)
		{
			int chunkPixels = mapHandler.chunkSize * mapHandler.tileSize;
			
			if(!DEBUG)
			{
				
				for(int i=0;i<mapHandler.getChunkHeight();i++)
					for(int j=0;j<mapHandler.getChunkWidth();j++)
					//	batch.draw(mapHandler.getChunkTexture(j, i), j*chunkPixels, (mapHandler.getChunkHeight() - i - 1)*chunkPixels);
						batch.draw(mapHandler.chunkManager.getChunk(j, i).texture, j*chunkPixels, (mapHandler.getChunkHeight() - i - 1)*chunkPixels);
				//batch.draw(textureGameMap,0,0);
			}
				
			else
			{
				if(viewMode == 0)
					for(int i=0;i<mapHandler.getChunkHeight();i++)
						for(int j=0;j<mapHandler.getChunkWidth();j++)
						{
							//batch.draw(mapHandler.getChunkTexture(j, i), j*chunkPixels, (mapHandler.getChunkHeight() - i - 1)*chunkPixels);
							batch.draw(mapHandler.chunkManager.getChunk(j, i).texture, j*chunkPixels, (mapHandler.getChunkHeight() - i - 1)*chunkPixels);
						}
							
				//	batch.draw(textureGameMap,0,0);
				else if(viewMode == 1)
					batch.draw(textureMap,0,0);
				else if(viewMode == 2)
					batch.draw(textureTemperature,0,0);
				else if(viewMode == 3)
					batch.draw(textureHumidity,0,0);
				else 
					batch.draw(groupTexture,0,0);
			}
		}
			

		
		bFPS.draw(batch, sFPSlog + "   FPS: " + (int)(FPS), 320, 460);
		serverDebugInfo.draw(batch, "" + stringServerDebugInfo, 10, 460);
		
		batch.end();
	}
	
	
	public void update()
	{
		FPS = 1/ Gdx.graphics.getRawDeltaTime();
		control();
		
		
		
		if(receivedMap == true && drawMap == false)
		{
		
			if(mapHandler.isGeneratingStarted == false)				
				mapHandler.createMap(tileMap);
				
			
			if(mapHandler.isMapGenerated == false)
			{
				if(mapHandler.isMapGenerated())
				{
					//gameMap = mapHandler.getMap();
					//textureGameMap = new Texture(gameMap);
					
					//textureGameMap.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
					
						if(DEBUG)
					{
						map = mapHandler.debugPutMap(map, fMap,1);
						temperature = mapHandler.debugPutMap(temperature, fTemperature, 2);
						humidity = mapHandler.debugPutMap(humidity, fHumidity, 3);
						groupPixmap = mapHandler.debugGroup(tileMap);		
						
						textureMap = new Texture(map);
						textureMap.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
						
						textureTemperature = new Texture(temperature);
						textureTemperature.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
								
						textureHumidity = new Texture(humidity);
						textureHumidity.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);	
						
						groupTexture = new Texture(groupPixmap);
						groupTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
					}
					
				//	mapHandler.clearAfterGeneration();
					drawMap = true;
				}	
			}
		}	
		//	gameMap = mapHandler.putBiomesOnMap(gameMap,0, 0,128, tileMap);
			

	}
	
	
	public synchronized void receiverCreate() 
	{
			
		new Thread(new Runnable() {		
			@Override
			public void run() 
			{		
				
				ServerWaitingPacket serverWaitingPacket = null;
				ServerMapPacket mapPacket = null;	
				ServerGeneratingPacket generatingPacket = null;
				
				while(true)
				{		
					
					try 
					{	
						//To recognize what kind of packet was send from server
						//1-ServerWaitingPacket; 2-generatingPacket
						int packetType  = in_sock.readInt();
					
						
						if(packetType == 1)
						try
						{									
							serverWaitingPacket = (ServerWaitingPacket)in_sock.readObject();
							server_mode = mode.WAITING_ROOM;
						}
						catch(Exception e){}
		
						else if(packetType == 2)
						{
							try
							{										
								mapHeight = in_sock.readInt();
								mapWidth = in_sock.readInt();
								
								mapPacket = (ServerMapPacket)in_sock.readObject();
								fMap = mapPacket.map;
								fTemperature = mapPacket.temperature;
								fHumidity = mapPacket.humadity;
								tileMap = mapPacket.tileMap;
								
								
								camera.setMapSize(mapWidth*16, mapHeight*16);
								
								receivedMap = true;								
								server_mode = mode.GENERATING;
								
							}
							catch(Exception e){JOptionPane.showMessageDialog(null, e.toString());}
						}	
						
						else if(packetType == 3)
						{
							try
							{
								
								generatingPacket = (ServerGeneratingPacket)in_sock.readObject();
								
								sFPSlog = "Time: " + generatingPacket.sendTime + "   Ping: " + (int)(System.currentTimeMillis() - generatingPacket.sendTime);								
								playersMapGenerated = generatingPacket.playersReady;
								server_mode = mode.GENERATING;
								

							}
							catch(Exception e){JOptionPane.showMessageDialog(null, e.toString());}
						}
						
						if(server_mode == mode.WAITING_ROOM)
						{
							if(serverWaitingPacket.SendTime != lastSendTime)
							{
								lastSendTime = serverWaitingPacket.SendTime;
								
								//DEBUG INFO
								sFPSlog = "Time: " + serverWaitingPacket.SendTime + "   Ping: " + (int)(System.currentTimeMillis() - serverWaitingPacket.SendTime);
								stringServerDebugInfo = "Server_mode: " + serverWaitingPacket.server_mode + "\n" +
										"Server_statut: " + serverWaitingPacket.server_status + "\n" +
										"Waiting_room_statut: " + serverWaitingPacket.waiting_room_status + "\n" +
										"Players: " + serverWaitingPacket.PlayerNumber + "\\" + serverWaitingPacket.MaxPlayers;
								//DEBUG INFO
								
								myID = serverWaitingPacket.PlayerID;
								players = serverWaitingPacket.PlayerNumber;	
								serverWaitingPacket = null;
								
							}
							
						}
						else if(server_mode == mode.GENERATING)
						{
							
							
							
							
						}
					
					} 
					catch (Exception e) {e.printStackTrace();}	

					try {   //delay 5 ms to save CPU power
						Thread.currentThread();
						Thread.sleep(5);} 
					catch (InterruptedException e) {e.printStackTrace();}					
					
				}
				
			}
		}).start();
		
	}
	
	public synchronized void senderCreate() 
	{
		
		
		new Thread(new Runnable() {		
			@Override
			public void run() 
			{				
				try
				{
					ClientWaitingPacket clientWaitingPacket;
					ClientGeneratingPacket clientGeneratingPacket;
					ObjectOutputStream out_sock = new ObjectOutputStream(socket.getOutputStream());	
					
					while(Errors <= 10)
					{					
						try
						{
							if(server_mode == mode.WAITING_ROOM)
							{
								
								out_sock.writeInt(1);
								out_sock.reset();
								
								clientWaitingPacket = new ClientWaitingPacket();
								if(myID != -1)
									clientWaitingPacket.PlayerID = myID;
								clientWaitingPacket.SendTime = System.currentTimeMillis();
								clientWaitingPacket.Ready = isReady;
								
								out_sock.writeObject(clientWaitingPacket);
								out_sock.flush();
								out_sock.reset();
							}
							else if(server_mode == mode.GENERATING)
							{
								
								out_sock.writeInt(2);
								out_sock.reset();							
								
								clientGeneratingPacket = new ClientGeneratingPacket();
								clientGeneratingPacket.mapReceived = receivedMap;
								clientGeneratingPacket.mapReady = drawMap;
								clientGeneratingPacket.sendTime = System.currentTimeMillis();
								
								out_sock.writeObject(clientGeneratingPacket);
								out_sock.flush();
								out_sock.reset();
								
								
						
							}

							Errors = 0;
						} catch (Exception e) {e.printStackTrace();}		
						
						try {   //delay 1 ms to save CPU power
							Thread.currentThread();
							Thread.sleep(10);} 
						catch (InterruptedException e) {Errors++;};		
					}	
				}catch (IOException e) { e.printStackTrace();};
			}
		}).start();
	}
	

	public void control() 
	{

		float deltaTime = Gdx.graphics.getDeltaTime();
		float deltaX = 0;
		float deltaY = 0;
		
		if(Gdx.input.isKeyPressed(Keys.A)) deltaX = -96;
		if(Gdx.input.isKeyPressed(Keys.D)) deltaX = 96;
		if(Gdx.input.isKeyPressed(Keys.W)) deltaY = 96;
		if(Gdx.input.isKeyPressed(Keys.S)) deltaY = -96;
		
		if(Gdx.input.isKeyPressed(Keys.Q))
			camera.zoom += 1 * Gdx.graphics.getDeltaTime();
		else if(Gdx.input.isKeyPressed(Keys.E))
			camera.zoom -= 1 * Gdx.graphics.getDeltaTime();
		
		camera.translateCamera(deltaX, deltaY, deltaTime);
		
		if(Gdx.input.isKeyJustPressed(Keys.R))
		{
			isReady = (isReady == true) ? false : true;
		}
		if(Gdx.input.isKeyJustPressed(Keys.F))
		{
			viewMode++;
			if(viewMode > 4) viewMode = 0;
		}
					
	}
	
}
