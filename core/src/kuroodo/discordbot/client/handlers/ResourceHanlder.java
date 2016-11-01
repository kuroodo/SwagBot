package kuroodo.discordbot.client.handlers;

import java.util.HashMap;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class ResourceHanlder {
	private HashMap<String, Texture> textureResource;
	private HashMap<String, TextureAtlas> atlasResource;

	public ResourceHanlder() {
		textureResource = new HashMap<>();
		atlasResource = new HashMap<>();
	}

	public void loadTexture(String key, String path) {
		textureResource.put(key, new Texture(path));
	}

	public Texture getTexture(String key) {
		return textureResource.get(key);
	}

	public void loadAtlas(String key, String path) {
		atlasResource.put(key, new TextureAtlas(path));
	}

	public TextureAtlas getAtlas(String key) {
		return atlasResource.get(key);
	}

}
