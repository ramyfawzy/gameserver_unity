package com.gameserver.model.actor;

import com.gameserver.model.holders.NpcHolder;
import com.gameserver.model.holders.SpawnHolder;

/**
 * @author Ramy Ibrahim
 */
public class Npc extends Creature
{
	private final NpcHolder _npcHolder;
	private final SpawnHolder _spawnHolder;
	
	public Npc(NpcHolder npcHolder, SpawnHolder spawnHolder)
	{
		_npcHolder = npcHolder;
		_spawnHolder = spawnHolder;
		
		setCurrentHp(npcHolder.getHp());
		// TODO: Implement Creature stats holder.
		setLocation(spawnHolder.getLocation());
	}
	
	public NpcHolder getNpcHolder()
	{
		return _npcHolder;
	}
	
	public SpawnHolder getSpawnHolder()
	{
		return _spawnHolder;
	}
	
	@Override
	public boolean isNpc()
	{
		return true;
	}
	
	@Override
	public Npc asNpc()
	{
		return this;
	}
	
	@Override
	public String toString()
	{
		return "NPC [" + _npcHolder.getNpcId() + "]";
	}
}
