package me.efe.efecommunity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class UserData {
	private UUID id;
	private List<UUID> friends;
	private List<Post> posts;
	
	public UserData(OfflinePlayer p) {
		this(p.getUniqueId());
	}
	
	public UserData(UUID id) {
		this.id = id;
		this.friends = new ArrayList<UUID>();
		this.posts = new ArrayList<Post>();
		
		File file = new File("plugins/EfeCommunity/UserData/"+id.toString()+".yml");
		
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		
		for (String str : config.getStringList("friends")) friends.add(UUID.fromString(str));
		for (int num : config.getIntegerList("posts")) posts.add(Post.getFromYml(num));
	}
	
	public UUID getUniqueId() {
		return this.id;
	}
	
	public List<UUID> getFriends() {
		return this.friends;
	}
	
	public List<Post> getPosts() {
		return this.posts;
	}
	
	public void addFriend(UUID id) {
		this.friends.add(id);
		
		save();
	}
	
	public void removeFriend(UUID id) {
		if (!this.friends.contains(id)) return;
		
		this.friends.remove(id);
		
		save();
	}
	
	public void addPost(Post post) {
		this.posts.add(post);
		
		save();
	}
	
	public void removePost(Post post) {
		if (!this.posts.contains(post)) return;
		
		this.posts.remove(post);
		
		save();
	}
	
	private void save() {
		try {
			File file = new File("plugins/EfeCommunity/UserData/"+id.toString()+".yml");
			FileConfiguration config = YamlConfiguration.loadConfiguration(file);
			
			List<String> fList = new ArrayList<String>();
			for (UUID id : friends) fList.add(id.toString());
			
			config.set("friends", fList);
			
			List<Integer> pList = new ArrayList<Integer>();
			for (Post post : posts) pList.add(post.getNumber());
			
			config.set("posts", pList);
			
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}