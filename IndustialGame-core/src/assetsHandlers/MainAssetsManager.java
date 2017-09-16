package assetsHandlers;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public class MainAssetsManager extends AssetManager
{
	
	public MainAssetsManager()
	{
	//	this = new AssetManager();
	}
	public void loadData()
	{
		this.load("Texture/worldTexture.png", Texture.class);
		this.load("Texture/temperature.png", Texture.class);
		this.load("Texture/height.png", Texture.class);
		this.load("Texture/humidity.png", Texture.class);
		this.load("Texture/biomes.png", Texture.class);
		this.finishLoading();
		
	}
	
}

