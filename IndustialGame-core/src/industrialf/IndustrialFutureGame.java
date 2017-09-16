package industrialf;

import com.badlogic.gdx.ApplicationAdapter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import Multiplayer.MultiGame;
import camera.GameCamera;
import map.Map;

public class IndustrialFutureGame extends ApplicationAdapter {
	SpriteBatch batch;
	//OrthographicCamera camera;
	float x, y;
	double time;
	BitmapFont font;
	MultiGame multiplayer;
	Map gameMap;
	Pixmap mapPixmap;
	Texture mapTexture;
	GameCamera camera;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		
		font = new BitmapFont();

		
		camera = new GameCamera(320, 240, 512*16, 512*16);
		camera.position.set(160, 120,0);
		camera.update();
		
		
		multiplayer = new MultiGame();
		multiplayer.create();
		
		//camera = new OrthographicCamera(640, 480);
		//multiGame = new MultiGame();
		//multiGame.create();
		
		
		//camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
		//camera.update();
		
		
	}

	@Override
	public void render () 
	{
		batch.setProjectionMatrix(camera.combined);
		
		camera.update();
	//	Gdx.gl.glClearColor(1, 0, 0, 0.1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if(Gdx.input.isKeyPressed(Keys.Q))
			camera.zoom += 1 * Gdx.graphics.getDeltaTime();
		else if(Gdx.input.isKeyPressed(Keys.E))
			camera.zoom -= 1 * Gdx.graphics.getDeltaTime();
	
	//	control();
		
		batch.begin();
		//batch.draw(mapTexture,0,0);
		multiplayer.render();
		batch.end();
	}
	
	public void control()
	{
		float deltaTime = Gdx.graphics.getDeltaTime();
		float deltaX=0, deltaY=0;
		
		if(Gdx.input.isKeyPressed(Keys.W))
			deltaY = 32;
		else if(Gdx.input.isKeyPressed(Keys.S))
			deltaY = -32;
		if(Gdx.input.isKeyPressed(Keys.A))
			deltaX = -32;
		else if(Gdx.input.isKeyPressed(Keys.D))
			deltaX = 32;
		
		camera.translateCamera(deltaX, deltaY, deltaTime);
		
	}
	
	
}


