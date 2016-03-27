package me.efe.efecommunity;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

public class Post {
	private int number;
	private String sender;
	private String title;
	private String text;
	private List<ItemStack> items;
	
	public Post(int number, String sender, String title, String text, List<ItemStack> items) {
		this.number = number;
		this.sender = sender;
		this.title = title;
		this.text = text;
		this.items = items;
	}
	
	public int getNumber() {
		return this.number;
	}
	
	public String getSender() {
		return this.sender;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public String getText() {
		return this.text;
	}
	
	public List<ItemStack> getItems() {
		return this.items;
	}
	
	public void writeYml() {
		try {
			File file = new File("plugins/EfeCommunity/Posts/"+number+".yml");
			
			if (!file.exists()) file.createNewFile();
			
			FileConfiguration config = YamlConfiguration.loadConfiguration(file);
			
			config.set("sender", sender);
			config.set("title", title);
			config.set("text", text);
			if (items != null && !items.isEmpty()) config.set("items", items);
			
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void delete() {
		File file = new File("plugins/EfeCommunity/Posts/"+number+".yml");
		file.delete();
	}
	
	public static Builder getBuilder() {
		return new Builder();
	}
	
	public static Post getFromYml(int number) {
		File file = new File("plugins/EfeCommunity/Posts/"+number+".yml");
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		
		String sender = config.getString("sender");
		String title = config.getString("title");
		String text = config.getString("text");
		@SuppressWarnings("unchecked")
		List<ItemStack> items = (List<ItemStack>) config.getList("items");
		
		return new Post(number, sender, title, text, items);
	}
	
	public static int generateNumber() {
		try {
			File file = new File("plugins/EfeCommunity/numbers.yml");
			
			if (!file.exists()) file.createNewFile();
			
			FileConfiguration config = YamlConfiguration.loadConfiguration(file);
			
			if (config.contains("number")) config.set("number", config.getInt("number") + 1);
			else config.set("number", 0);
			
			config.save(file);
			
			return config.getInt("number");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public static void sendPost(OfflinePlayer p, Post post) {
		UserData data = new UserData(p);
		
		post.writeYml();
		data.addPost(post);
	}
	
	public static class Builder {
		private int number;
		private String sender;
		private String title;
		private String text;
		private List<ItemStack> items;
		
		public Builder() {
			this.number = Post.generateNumber();
		}
		
		public Builder setSender(String sender) {
			this.sender = sender;
			
			return this;
		}
		
		public Builder setMessage(String title, String text) {
			this.title = title;
			this.text = text;

			return this;
		}
		
		public Builder setItems(ItemStack[] array) {
			this.items = Arrays.asList(array);

			return this;
		}
		
		public Builder setItems(List<ItemStack> list) {
			this.items = list;

			return this;
		}
		
		public Post build() {
			return new Post(number, sender, title, text, items);
		}
	}
}