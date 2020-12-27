package com.gameserver.model.actor;

import com.gameserver.model.holders.NpcHolder;
import com.gameserver.model.holders.SpawnHolder;

/**
 * @author Ramy Ibrahim
 */
public class Monster extends Npc
{
	public Monster(NpcHolder npcHolder, SpawnHolder spawnHolder)
	{
		super(npcHolder, spawnHolder);
		
		// TODO: AI Tasks.
		// TODO: Loot corpse.
	}
	
	@Override
	public boolean isMonster()
	{
		return true;
	}
	
	@Override
	public Monster asMonster()
	{
		return this;
	}
}
