package com.gameserver.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.gameserver.enums.SkillType;
import com.gameserver.managers.IDatabaseManager;
import com.gameserver.model.holders.SkillHolder;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author Ramy Ibrahim
 */

@Singleton
public class SkillData implements ISkillData
{
	private static final Logger LOGGER = LogManager.getLogger(SkillData.class.getName());
	
	private static final String RESTORE_SKILLS = "SELECT * FROM skills";
	
	private static final Map<Long, SkillHolder> _skills = new HashMap<>();
	
	@Inject
	IDatabaseManager databaseManager;
	
	@Override
	public void init()
	{
		_skills.clear();
		
		try (Connection con = databaseManager.getConnection();
			PreparedStatement ps = con.prepareStatement(RESTORE_SKILLS))
		{
			try (ResultSet rset = ps.executeQuery())
			{
				while (rset.next())
				{
					final int skillId = rset.getInt("skill_id");
					final int skillLevel = rset.getInt("level");
					_skills.put(getSkillHashCode(skillId, skillLevel), new SkillHolder(skillId, skillLevel, Enum.valueOf(SkillType.class, rset.getString("type")), rset.getInt("reuse"), rset.getInt("range"), rset.getInt("param_1"), rset.getInt("param_2")));
				}
			}
		}
		catch (Exception e)
		{
			LOGGER.error(e.getMessage());
		}
		
		LOGGER.info("SkillData: Loaded " + _skills.size() + " skills.");
	}
	
	private static long getSkillHashCode(int skillId, int skillLevel)
	{
		return (skillId * 100000) + skillLevel;
	}
	
	public static SkillHolder getSkillHolder(int skillId, int skillLevel)
	{
		return _skills.get(getSkillHashCode(skillId, skillLevel));
	}
}
