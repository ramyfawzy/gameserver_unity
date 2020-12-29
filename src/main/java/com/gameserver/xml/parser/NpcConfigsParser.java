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

public class NpcConfigsParser extends XMLParser {
	private static final Logger LOGGER = LogManager.getLogger(NpcConfigsParser.class.getName());

	public NpcConfigsParser() {
	}

	public Map<Integer,NpcConfig> loadNpcConfigs() throws Exception {
		LOGGER.info("Loading: {}", Config.NPC_CONFIGS_FOLDER);
		final Map<Integer, NpcConfig> ruleMap = new ConcurrentHashMap<Integer, NpcConfig>();
		visitParallel(Config.NPC_CONFIGS_FOLDER, new IXMLParserFileVisitor() {
			@Override
			public void visit(final File file) throws Exception {
				// unfortunately simple-xml doesn't convert attributes to NpcIDs
				// so we do it here
				final NpcConfigs npcConfigs = load(NpcConfigs.class, file);
				npcConfigs.getNpcConfigs().forEach(n -> ruleMap.put(n.getNpcId(), n));
			}
		});

		return ruleMap;
	}

}
