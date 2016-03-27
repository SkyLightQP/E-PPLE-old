package me.efe.efequest.quest;

import java.util.List;

public class Quest {
	private int id;
	private String name;
	private int startNPC;
	private int quitNPC;
	private int lv;
	private List<Integer> needQuests;
	private List<String> askChats;
	private List<String> waitChats;
	private List<String> rewardChats;
	private List<String> thankChats;
	private List<String> goals;
	
	public Quest(Builder builder) {
		this.id = builder.id;
		this.name = builder.name;
		this.lv = builder.lv;
		this.startNPC = builder.startNPC;
		this.quitNPC = builder.quitNPC;
		this.needQuests = builder.needQuests;
		this.askChats = builder.askChats;
		this.waitChats = builder.waitChats;
		this.rewardChats = builder.rewardChats;
		this.thankChats = builder.thankChats;
		this.goals = builder.goals;
	}
	
	public int getIdentity() {
		return this.id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getStartNPC() {
		return this.startNPC;
	}
	
	public int getQuitNPC() {
		return this.quitNPC;
	}
	
	public int getLevelRequired() {
		return this.lv;
	}
	
	public List<Integer> getNeedQuests() {
		return this.needQuests;
	}
	
	public List<String> getAskChats() {
		return this.askChats;
	}
	
	public List<String> getWaitChats() {
		return this.waitChats;
	}
	
	public List<String> getRewardChats() {
		return this.rewardChats;
	}
	
	public List<String> getThankChats() {
		return this.thankChats;
	}
	
	public List<String> getGoals() {
		return this.goals;
	}
	
	public static Builder getBuilder() {
		return new Builder();
	}
	
	public static class Builder {
		private int id;
		private String name;
		private int startNPC;
		private int quitNPC;
		private int lv;
		private List<Integer> needQuests;
		private List<String> askChats;
		private List<String> waitChats;
		private List<String> rewardChats;
		private List<String> thankChats;
		private List<String> goals;
		
		public Builder setIdentity(int id, String name) {
			this.id = id;
			this.name = name;
			return this;
		}
		
		public Builder setNPC(int startNPC, int quitNPC) {
			this.startNPC = startNPC;
			this.quitNPC = quitNPC;
			return this;
		}
		
		public Builder setLevel(int lv) {
			this.lv = lv;
			return this;
		}
		
		public Builder setNeedQuests(List<Integer> needQuests) {
			this.needQuests = needQuests;
			return this;
		}
		
		public Builder setChats(List<String> askChats, List<String> waitChats, List<String> rewardChats, List<String> thankChats) {
			this.askChats = askChats;
			this.waitChats = waitChats;
			this.rewardChats = rewardChats;
			this.thankChats = thankChats;
			return this;
		}
		
		public Builder setGoals(List<String> goals) {
			this.goals = goals;
			return this;
		}
		
		public Quest build() {
			return new Quest(this);
		}
	}
}