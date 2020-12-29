package com.gameserver.xml.parser;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name = "npcConfig")
public class NpcConfig {
	
	@Attribute
	private int npcId;
	@Attribute(required = false)
	private String type;
	@Attribute(required = false)
	private int level;
	@Attribute(required = false)
	private int sex;
	@Attribute(required = false)
	private int hp;
	@Attribute(required = false)
	private int stamina;
	@Attribute(required = false)
	private int strength;
	@Attribute(required = false)
	private int dexterity;
	@Attribute(required = false)
	private int intelect;
	public int getNpcId() {
		return npcId;
	}
	public void setNpcId(int npcId) {
		this.npcId = npcId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public int getHp() {
		return hp;
	}
	public void setHp(int hp) {
		this.hp = hp;
	}
	public int getStamina() {
		return stamina;
	}
	public void setStamina(int stamina) {
		this.stamina = stamina;
	}
	public int getStrength() {
		return strength;
	}
	public void setStrength(int strength) {
		this.strength = strength;
	}
	public int getDexterity() {
		return dexterity;
	}
	public void setDexterity(int dexterity) {
		this.dexterity = dexterity;
	}
	public int getIntelect() {
		return intelect;
	}
	public void setIntelect(int intelect) {
		this.intelect = intelect;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("NpcConfig [npcId=").append(npcId).append(", type=").append(type).append(", level=")
				.append(level).append(", sex=").append(sex).append(", hp=").append(hp).append(", stamina=")
				.append(stamina).append(", strength=").append(strength).append(", dexterity=").append(dexterity)
				.append(", intelect=").append(intelect).append("]");
		return builder.toString();
	}

}
