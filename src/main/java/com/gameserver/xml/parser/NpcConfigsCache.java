package com.gameserver.xml.parser;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.gameserver.Config;
import com.gameserver.xml.IXMLParserFileVisitor;
import com.gameserver.xml.XMLParser;
import com.gameserver.xml.model.NpcConfigs;
import com.google.inject.Singleton;


@Singleton
public class NpcConfigsCache extends XMLParser {
	private static final Logger LOGGER = LogManager.getLogger(NpcConfigsCache.class.getName());
	
	private final Map<Integer, NpcConfig> npcConfigsMap = new ConcurrentHashMap<>();

	public void loadNpcConfigs() throws Exception {
		LOGGER.info("Loading: {}", Config.NPC_CONFIGS_FOLDER);
		visitParallel(Config.NPC_CONFIGS_FOLDER, new IXMLParserFileVisitor() {
			@Override
			public void visit(final File file) throws Exception {
				final NpcConfigs npcConfigs = load(NpcConfigs.class, file);
				npcConfigs.getNpcConfigs().forEach(n -> npcConfigsMap.put(n.getNpcId(), n));
			}
		});
	}

	public Map<Integer, NpcConfig> getNpcConfigsMap() {
		return npcConfigsMap;
	}

}
