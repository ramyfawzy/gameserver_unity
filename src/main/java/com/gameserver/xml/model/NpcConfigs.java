package com.gameserver.xml.model;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import com.gameserver.xml.parser.NpcConfig;

@Root(name = "npcConfigs")
public class NpcConfigs {
	
	@ElementList(inline = true, required = false, entry = "npcConfig")
	private List<NpcConfig> npcConfigs = new ArrayList<NpcConfig>();

	public List<NpcConfig> getNpcConfigs() {
		return npcConfigs;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("NpcConfigs [npcConfigs=").append(npcConfigs).append("]");
		return builder.toString();
	}

}
