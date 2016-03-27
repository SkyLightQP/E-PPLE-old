package me.efe.efeserver;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;

import me.efe.efecommunity.Post;
import me.efe.efegear.EfeUtil;
import me.efe.efegear.util.VaultHooker;
import me.efe.efeisland.EfeIsland;
import me.efe.efeserver.additory.Announcer;
import me.efe.efeserver.additory.CashGUI;
import me.efe.efeserver.additory.CashShopGUI;
import me.efe.efeserver.additory.ChairManager;
import me.efe.efeserver.additory.ChatListener;
import me.efe.efeserver.additory.MainGUI;
import me.efe.efeserver.additory.OptionGUI;
import me.efe.efeserver.additory.PlayerInfoListener;
import me.efe.efeserver.additory.TipListener;
import me.efe.efeserver.additory.TradeGUI;
import me.efe.efeserver.commands.CashCommand;
import me.efe.efeserver.commands.CashShopCommand;
import me.efe.efeserver.commands.ChairCommand;
import me.efe.efeserver.commands.Commander;
import me.efe.efeserver.commands.HelpCommand;
import me.efe.efeserver.commands.MenuCommand;
import me.efe.efeserver.commands.OptionCommand;
import me.efe.efeserver.commands.PlayerInfoCommand;
import me.efe.efeserver.commands.TradeCommand;
import me.efe.efeserver.commands.WhisperCommand;
import me.efe.efeserver.mysql.SQLManager;
import me.efe.efeserver.reform.AnimalListener;
import me.efe.efeserver.reform.LoggingListener;
import me.efe.efeserver.reform.MiningListener;
import me.efe.efeserver.reform.MyBoat;
import me.efe.efeserver.reform.SleepingListener;
import me.efe.efeserver.reform.farming.FarmBuilder;
import me.efe.efeserver.reform.farming.FarmingListener;
import me.efe.efeserver.reform.farming.GrassListener;
import me.efe.efeserver.util.CashAPI;
import me.efe.efeserver.util.Scoreboarder;

import org.bukkit.Chunk;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedServerPing;
import com.comphenix.protocol.wrappers.nbt.NbtCompound;
import com.comphenix.protocol.wrappers.nbt.NbtFactory;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import de.slikey.effectlib.util.ParticleEffect;

public class EfeServer extends JavaPlugin {
	private static EfeServer instance;
	public EfeUtil util = new EfeUtil(this);
	public World world;
	public World worldIsl;
	public ItemStack steel;
	
	public MiningListener mineListener;
	public LoggingListener logListener;
	public FarmingListener farmListener;
	public FarmBuilder farmBuilder;
	public GrassListener grassListener;
	public AnimalListener animalListener;
	public SleepingListener sleepingListener;
	public MyBoat myboat;
	
	public MainGUI mainGui;
	public OptionGUI optionGui;
	public ChatListener chatListener;
	public CashGUI cashGui;
	public CashShopGUI cashShopGui;
	public TradeGUI tradeGui;
	public ChairManager chairManager;
	public TipListener tipListener;
	public Announcer announcer;
	
	public Commander commander;
	public ExtraListener extraListener;
	public PlayerInfoListener playerInfoListener;
	
	public Scoreboarder boarder;
	
	public WorldEditPlugin wep;
	public EfeIsland efeIsland;
	public VaultHooker vault;
	public SQLManager sqlManager;
	
	@Override
	public void onDisable() {
		util.logDisable();
		
		mineListener.clear();
		grassListener.clear();
		chairManager.clear();
		
		Iterator<Entity> it = world.getEntities().iterator();
		while (it.hasNext()) {
			Entity entity = it.next();
			EntityType type = entity.getType();
			
			Chunk chunk = entity.getLocation().getChunk();
			if (!chunk.isLoaded())
				chunk.load();
			
			if (type.equals(EntityType.BOAT)) entity.remove();
			if (type.equals(EntityType.DROPPED_ITEM)) entity.remove();
			if (type.equals(EntityType.EXPERIENCE_ORB)) entity.remove();
			if (entity instanceof LivingEntity && !type.equals(EntityType.ARMOR_STAND)) entity.remove();
		}
		
		it = worldIsl.getEntities().iterator();
		while (it.hasNext()) {
			Entity entity = it.next();
			EntityType type = entity.getType();
			
			Chunk chunk = entity.getLocation().getChunk();
			if (!chunk.isLoaded())
				chunk.load();
			
			if (type.equals(EntityType.BOAT)) entity.remove();
			if (type.equals(EntityType.DROPPED_ITEM)) entity.remove();
			if (type.equals(EntityType.EXPERIENCE_ORB)) entity.remove();
			if (entity.hasMetadata("biome_creeper")) entity.remove();
		}
	}
	
	@Override
	public void onEnable() {
		instance = this;
		
		util.logEnable();
		
		this.mineListener = new MiningListener(this);
		this.logListener = new LoggingListener(this);
		this.farmListener = new FarmingListener(this);
		this.farmBuilder = new FarmBuilder(this);
		this.grassListener = new GrassListener(this);
		this.animalListener = new AnimalListener(this);
		this.sleepingListener = new SleepingListener(this);
		this.myboat = new MyBoat(this);
		
		this.mainGui = new MainGUI(this);
		this.optionGui = new OptionGUI(this);
		this.chatListener = new ChatListener(this);
		this.cashGui = new CashGUI(this);
		this.cashShopGui = new CashShopGUI(this);
		this.tradeGui = new TradeGUI(this);
		this.chairManager = new ChairManager(this);
		this.tipListener = new TipListener(this);
		this.announcer = new Announcer(this);
		
		this.commander = new Commander(this);
		this.extraListener = new ExtraListener(this);
		this.playerInfoListener = new PlayerInfoListener(this);
		
		this.boarder = new Scoreboarder(this);
		
		util.register(mineListener);
		util.register(logListener);
		util.register(farmListener);
		util.register(farmBuilder);
		util.register(grassListener);
		util.register(animalListener);
		util.register(sleepingListener);
		util.register(myboat);
		
		util.register(mainGui);
		util.register(optionGui);
		util.register(chatListener);
		util.register(cashGui);
		util.register(cashShopGui);
		util.register(tradeGui);
		util.register(tipListener);
		
		util.register(commander);
		util.register(extraListener);
		util.register(playerInfoListener);
		
		util.register(boarder);
		
		this.getCommand("메뉴").setExecutor(new MenuCommand(this));
		this.getCommand("명령어").setExecutor(new HelpCommand(this));
		this.getCommand("귓속말").setExecutor(new WhisperCommand(this));
		this.getCommand("설정").setExecutor(new OptionCommand(this));
		this.getCommand("정보").setExecutor(new PlayerInfoCommand(this));
		this.getCommand("코인").setExecutor(new CashCommand(this));
		this.getCommand("코인샵").setExecutor(new CashShopCommand(this));
		this.getCommand("거래").setExecutor(new TradeCommand(this));
		this.getCommand("앉기").setExecutor(new ChairCommand(this));
		
		wep = (WorldEditPlugin) getServer().getPluginManager().getPlugin("WorldEdit");
		efeIsland = (EfeIsland) getServer().getPluginManager().getPlugin("EfeIsland");
		
		vault = new VaultHooker();
		vault.setupEconomy();
		
		world = getServer().getWorld("world");
		worldIsl = efeIsland.world;
		
		worldIsl.setTime(world.getTime());
		
		sqlManager = new SQLManager();
		sqlManager.connect();
		CashAPI.update();
		
		ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(this, PacketType.Status.Server.OUT_SERVER_INFO) {
			@Override
			public void onPacketSending(PacketEvent e) {
				WrappedServerPing ping = e.getPacket().getServerPings().read(0);
				
				ping.setMotD("§c§lE-PPLE §4|§6 Open" + "\n"
						+ "§b>> Welcome To New-Style Minecraft!");
			}
		});
		
		ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(this, PacketType.Play.Server.SET_SLOT, PacketType.Play.Server.WINDOW_ITEMS) {
			
			@Override
			public void onPacketSending(PacketEvent e) {
				if (e.getPacketType() == PacketType.Play.Server.SET_SLOT) {
					addGlow(e.getPacket().getItemModifier().read(0));
				} else {
					addGlow(e.getPacket().getItemArrayModifier().read(0));
				}
			}
		});
		
		this.steel = util.createRawItem("§r강철", new ItemStack(Material.IRON_INGOT), new String[]{});
		
		removeRecipes();
		addRecipes();
		
		getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			@Override
			public void run() {
				getServer().dispatchCommand(getServer().getConsoleSender(), "timings on");
			}
		}, 60L);
	}
	
	private void addGlow(ItemStack... items) {
		for (ItemStack item : items) {
			if (item == null) continue;
			
			if (item.getEnchantmentLevel(Enchantment.SILK_TOUCH) == 100) {
				NbtCompound compound = (NbtCompound) NbtFactory.fromItemTag(item);
				compound.put(NbtFactory.ofList("ench"));
			}
		}
	}
	
	public static EfeServer getInstance() {
		return instance;
	}
	
	public void removeRecipes() {
		Iterator<Recipe> it = getServer().recipeIterator();
		
		while (it.hasNext()) {
			Recipe recipe = it.next();
			Material type = recipe.getResult().getType();
			
			if (type.equals(Material.ANVIL) || 
					type.equals(Material.ENCHANTMENT_TABLE) || 
					type.equals(Material.ENDER_CHEST) || 
					type.equals(Material.BREWING_STAND_ITEM) || 
					type.equals(Material.EMPTY_MAP) || 
					type.equals(Material.MAP) || 
					type.equals(Material.BOAT) || 
					type.equals(Material.COMPASS) || 
					type.equals(Material.IRON_HELMET) || 
					type.equals(Material.IRON_CHESTPLATE) || 
					type.equals(Material.IRON_LEGGINGS) || 
					type.equals(Material.IRON_BOOTS) || 
					type.equals(Material.IRON_PICKAXE) || 
					type.equals(Material.IRON_AXE) || 
					type.equals(Material.IRON_SPADE) || 
					type.equals(Material.IRON_SWORD) || 
					type.equals(Material.IRON_HOE)) {
				it.remove();
			}
		}
	}
	
	public void addRecipes() {
		ShapedRecipe recipeHelmet = new ShapedRecipe(new ItemStack(Material.CHAINMAIL_HELMET))
		.shape("iii", "i i")
		.setIngredient('i', Material.IRON_INGOT);
		ShapedRecipe recipeChestplate = new ShapedRecipe(new ItemStack(Material.CHAINMAIL_CHESTPLATE))
		.shape("i i", "iii", "iii")
		.setIngredient('i', Material.IRON_INGOT);
		ShapedRecipe recipeLeggings = new ShapedRecipe(new ItemStack(Material.CHAINMAIL_LEGGINGS))
		.shape("iii", "i i", "i i")
		.setIngredient('i', Material.IRON_INGOT);
		ShapedRecipe recipeBoots = new ShapedRecipe(new ItemStack(Material.CHAINMAIL_BOOTS))
		.shape("i i", "i i")
		.setIngredient('i', Material.IRON_INGOT);
		ShapedRecipe recipePickaxe = new ShapedRecipe(new ItemStack(Material.IRON_PICKAXE, 1, (short) 76))
		.shape("iii", " w ", " w ")
		.setIngredient('i', Material.IRON_INGOT)
		.setIngredient('w', Material.STICK);
		ShapedRecipe recipeAxe = new ShapedRecipe(new ItemStack(Material.IRON_AXE, 1, (short) 76))
		.shape("iii", " w ", " w ")
		.setIngredient('i', Material.IRON_INGOT)
		.setIngredient('w', Material.STICK);
		ShapedRecipe recipeSpade = new ShapedRecipe(new ItemStack(Material.IRON_SPADE, 1, (short) 76))
		.shape("iii", " w ", " w ")
		.setIngredient('i', Material.IRON_INGOT)
		.setIngredient('w', Material.STICK);
		ShapedRecipe recipeSword = new ShapedRecipe(new ItemStack(Material.IRON_SWORD, 1, (short) 76))
		.shape("iii", " w ", " w ")
		.setIngredient('i', Material.IRON_INGOT)
		.setIngredient('w', Material.STICK);
		ShapedRecipe recipeHoe = new ShapedRecipe(new ItemStack(Material.IRON_HOE, 1, (short) 76))
		.shape("iii", " w ", " w ")
		.setIngredient('i', Material.IRON_INGOT)
		.setIngredient('w', Material.STICK);
		
		getServer().addRecipe(recipeHelmet);
		getServer().addRecipe(recipeChestplate);
		getServer().addRecipe(recipeLeggings);
		getServer().addRecipe(recipeBoots);
		getServer().addRecipe(recipePickaxe);
		getServer().addRecipe(recipeAxe);
		getServer().addRecipe(recipeSpade);
		getServer().addRecipe(recipeSword);
		getServer().addRecipe(recipeHoe);
		
		ShapedRecipe recipeIronHelmet = new ShapedRecipe(new ItemStack(Material.IRON_HELMET))
		.shape("iii", "i i")
		.setIngredient('i', steel.getData());
		ShapedRecipe recipeIronChestplate = new ShapedRecipe(new ItemStack(Material.IRON_CHESTPLATE))
		.shape("i i", "iii", "iii")
		.setIngredient('i', steel.getData());
		ShapedRecipe recipeIronLeggings = new ShapedRecipe(new ItemStack(Material.IRON_LEGGINGS))
		.shape("iii", "i i", "i i")
		.setIngredient('i', steel.getData());
		ShapedRecipe recipeIronBoots = new ShapedRecipe(new ItemStack(Material.IRON_BOOTS))
		.shape("i i", "i i")
		.setIngredient('i', steel.getData());
		ShapedRecipe recipeIronPickaxe = new ShapedRecipe(new ItemStack(Material.IRON_PICKAXE))
		.shape("iii", " w ", " w ")
		.setIngredient('i', Material.IRON_INGOT)
		.setIngredient('w', Material.STICK);
		ShapedRecipe recipeIronAxe = new ShapedRecipe(new ItemStack(Material.IRON_AXE))
		.shape("iii", " w ", " w ")
		.setIngredient('i', Material.IRON_INGOT)
		.setIngredient('w', Material.STICK);
		ShapedRecipe recipeIronSpade = new ShapedRecipe(new ItemStack(Material.IRON_SPADE))
		.shape("iii", " w ", " w ")
		.setIngredient('i', Material.IRON_INGOT)
		.setIngredient('w', Material.STICK);
		ShapedRecipe recipeIronSword = new ShapedRecipe(new ItemStack(Material.IRON_SWORD))
		.shape("iii", " w ", " w ")
		.setIngredient('i', Material.IRON_INGOT)
		.setIngredient('w', Material.STICK);
		ShapedRecipe recipeIronHoe = new ShapedRecipe(new ItemStack(Material.IRON_HOE))
		.shape("iii", " w ", " w ")
		.setIngredient('i', Material.IRON_INGOT)
		.setIngredient('w', Material.STICK);
		
		getServer().addRecipe(recipeIronHelmet);
		getServer().addRecipe(recipeIronChestplate);
		getServer().addRecipe(recipeIronLeggings);
		getServer().addRecipe(recipeIronBoots);
		getServer().addRecipe(recipeIronPickaxe);
		getServer().addRecipe(recipeIronAxe);
		getServer().addRecipe(recipeIronSpade);
		getServer().addRecipe(recipeIronSword);
		getServer().addRecipe(recipeIronHoe);
		
		FurnaceRecipe recipeSteel = new FurnaceRecipe(steel.clone(), new ItemStack(Material.IRON_INGOT).getData());
		
		getServer().addRecipe(recipeSteel);
	}
	
	public ProtectedRegion getIslRegion(Player p) {
		return this.efeIsland.getIsleRegion(p);
	}
	
	public ProtectedRegion getRegion(ApplicableRegionSet set, String id) {
		for (ProtectedRegion region : set) {
			if (region.getId().equals(id)) {
				return region;
			}
		}
		
		return null;
	}
	
	public void firework(Location loc) {
		Random rand = util.random;
		Firework firework = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
		FireworkMeta meta = firework.getFireworkMeta();
		Type type = Type.values()[rand.nextInt(Type.values().length)];
		Color color = Color.fromBGR(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
		Color fade = Color.fromBGR(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
		if (rand.nextBoolean()) fade = color;
		FireworkEffect effect = FireworkEffect.builder()
				.flicker(rand.nextBoolean())
				.trail(rand.nextBoolean())
				.with(type)
				.withColor(color)
				.withFade(fade)
				.build();
		meta.addEffect(effect);
		meta.setPower(rand.nextInt(2) + 1);
		firework.setFireworkMeta(meta);
	}
	
	@Override
	public boolean onCommand(CommandSender s, Command c, String l, String[] a) {
		if (l.equalsIgnoreCase("서버")) {
			if (!s.isOp()) return false;
			
			try {
				commander.serverCommand((Player) s, a);
			} catch (Exception e) {
				s.sendMessage("Exception Occured: "+e.getMessage());
			}
		} else if (l.equalsIgnoreCase("cash")) {
			if (!s.isOp()) return false;
			
			try {
				if (a.length == 0) {
					s.sendMessage("§a▒§r /cash info <target>");
					s.sendMessage("§a▒§r /cash deposit <target> <amount> <reason>");
					s.sendMessage("§a▒§r /cash withdraw <target> <amount> <reason>");
					s.sendMessage("§a▒§r /cash donate <target> <amount>");
					s.sendMessage("§a▒§r /cash donate <target> fail <reason>");
				} else if (a[0].equalsIgnoreCase("info")) {
					OfflinePlayer target = util.getOfflinePlayer(a[1]);
					
					if (target == null) {
						s.sendMessage("§c▒§r "+a[1]+"님은 존재하지 않습니다.");
						return false;
					}
					
					int amount = CashAPI.getBalance(target);
					
					s.sendMessage("§a▒§r "+target.getName()+"님의 잔액: "+amount);
				} else if (a[0].equalsIgnoreCase("deposit")) {
					OfflinePlayer target = util.getOfflinePlayer(a[1]);
					
					if (target == null) {
						s.sendMessage("§c▒§r "+a[1]+"님은 존재하지 않습니다.");
						return false;
					}
					
					int amount = Integer.parseInt(a[2]);
					
					if (amount <= 0) {
						s.sendMessage("§c▒§r "+a[2]+"원은 입력할 수 없습니다.");
						return false;
					}
					
					if (a.length < 4) {
						s.sendMessage("§c▒§r 이유를 적어주세요.");
						return false;
					}
					
					String reason = util.getFinalArg(a, 4);
					
					CashAPI.deposit(target, amount);
					CashAPI.log("Reason: "+reason);
					
					s.sendMessage("§a▒§r 코인이 "+amount+"원 지급되었습니다. 이유: "+reason);
					s.sendMessage("§a▒§r "+target.getName()+"님의 잔액: "+CashAPI.getBalance(target));
					
					if (target.isOnline())
						target.getPlayer().sendMessage("§a▒§r 관리자에 의해 코인이 충전되었습니다: §e"+amount+"원§r, 이유: "+reason);
				} else if (a[0].equalsIgnoreCase("withdraw")) {
					OfflinePlayer target = util.getOfflinePlayer(a[1]);
					
					if (target == null) {
						s.sendMessage("§c▒§r "+a[1]+"님은 존재하지 않습니다.");
						return false;
					}
					
					int amount = Integer.parseInt(a[2]);
					
					if (amount <= 0) {
						s.sendMessage("§c▒§r "+a[2]+"원은 입력할 수 없습니다.");
						return false;
					}
					
					if (a.length < 4) {
						s.sendMessage("§c▒§r 이유를 적어주세요.");
						return false;
					}
					
					String reason = util.getFinalArg(a, 4);
					
					CashAPI.withdraw(target, amount);
					CashAPI.log("Reason: "+reason);
					
					s.sendMessage("§a▒§r 코인이 "+amount+"원 지출되었습니다. 이유: "+reason);
					s.sendMessage("§a▒§r "+target.getName()+"님의 잔액: "+CashAPI.getBalance(target));
					
					if (target.isOnline())
						target.getPlayer().sendMessage("§a▒§r 관리자에 의해 코인이 지출되었습니다: §e-"+amount+"원§r, 이유: "+reason);
				} else if (a[0].equalsIgnoreCase("donate")) {
					OfflinePlayer target = util.getOfflinePlayer(a[1]);
					SimpleDateFormat format = new SimpleDateFormat("yyyy년 MM월 dd일 hh시 mm분");
					
					if (target == null) {
						s.sendMessage("§c▒§r "+a[1]+"님은 존재하지 않습니다.");
						return false;
					}
					
					if (a[2].equalsIgnoreCase("fail")) {
						String reason = util.getFinalArg(a, 4);
						
						Post post = Post.getBuilder()
								.setSender("§cE-PPLE")
								.setMessage("후원 처리에 실패했습니다.", "사유: "+reason+"|질문이 있으시다면 관리자에게 문의해주세요.|처리 일시: "+format.format(new Date()))
								.build();
						Post.sendPost(target, post);
						
						s.sendMessage("§a▒§r 후원 처리 실패 우편을 전송했습니다.");
						return true;
					}
					
					int amount = Integer.parseInt(a[2]);
					
					if (amount <= 0) {
						s.sendMessage("§c▒§r "+a[2]+"원은 입력할 수 없습니다.");
						return false;
					}
					
					CashAPI.deposit(target, amount);
					
					Post post = Post.getBuilder()
							.setSender("§cE-PPLE")
							.setMessage("코인이 지급되었습니다.", "후원해주셔서 감사합니다!|"+amount+"코인이 지급되었습니다.|처리 일시: "+format.format(new Date()))
							.build();
					Post.sendPost(target, post);
					
					s.sendMessage("§a▒§r 후원으로 코인이 "+amount+"원 지급되었습니다.");
					s.sendMessage("§a▒§r "+target.getName()+"님의 잔액: "+CashAPI.getBalance(target));
					
					if (target.isOnline()) {
						target.getPlayer().sendMessage("§a▒§r 후원으로 코인이 충전되었습니다: §e"+amount+"원");
					}
				}
				
				
			} catch (Exception e) {
				s.sendMessage("Exception Occured: "+e.getMessage());
			}
		} else if (l.equalsIgnoreCase("animaltrap")) {
			BlockCommandSender sender = (BlockCommandSender) s;
			Location loc = sender.getBlock().getLocation().add(0, 4, 0);
			
			for (Entity entity : loc.getWorld().getNearbyEntities(loc, 1.0D, 1.0D, 1.0D)) {
				ItemStack item = null;
				
				if (entity.getType() == EntityType.CHICKEN) {
					item = new ItemStack(Material.MONSTER_EGG, 1, (short) 93);
				} else if (entity.getType() == EntityType.RABBIT) {
					item = new ItemStack(Material.MONSTER_EGG, 1, (short) 101);
				} else if (entity.getType() == EntityType.COW) {
					item = new ItemStack(Material.MONSTER_EGG, 1, (short) 92);
				} else if (entity.getType() == EntityType.PIG) {
					item = new ItemStack(Material.MONSTER_EGG, 1, (short) 90);
				} else if (entity.getType() == EntityType.SHEEP) {
					item = new ItemStack(Material.MONSTER_EGG, 1, (short) 91);
				} else if (entity.getType() == EntityType.MUSHROOM_COW) {
					item = new ItemStack(Material.MONSTER_EGG, 1, (short) 96);
				}
				
				if (item != null) {
					ParticleEffect.EXPLOSION_NORMAL.display(0.0F, 0.0F, 0.0F, 0.1F, 50, entity.getLocation().add(0, 0.5, 0), 32);
					
					OfflinePlayer owner = this.animalListener.getOwner((LivingEntity) entity);
					
					if (owner != null && owner.isOnline() && owner.getPlayer().getLocation().distance(entity.getLocation()) < 15) {
						if (owner.getPlayer().getInventory().firstEmpty() == -1)
							loc.getWorld().dropItemNaturally(owner.getPlayer().getLocation(), item);
						else
							owner.getPlayer().getInventory().addItem(item);
					}
					
					entity.remove();
				}
			}
		} else if (l.equalsIgnoreCase("rebootannounce") && s.isOp()) {
			getServer().broadcastMessage("§a▒§r §7============================================");
			getServer().broadcastMessage("§a▒§r ");
			getServer().broadcastMessage("§a▒§r 서버 업데이트를 위한 리붓을 진행합니다.");
			getServer().broadcastMessage("§a▒§r 약 3분 후 재접속을 부탁드립니다. 감사합니다.");
			getServer().broadcastMessage("§a▒§r ");
			getServer().broadcastMessage("§a▒§r §7============================================");
		}
		return false;
	}
}