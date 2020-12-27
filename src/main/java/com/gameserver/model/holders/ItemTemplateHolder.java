package com.gameserver.model.holders;

import com.gameserver.enums.ItemSlot;
import com.gameserver.enums.ItemType;

/**
 * @author ribrahim
 */
public class ItemTemplateHolder
{
	private int _itemId;
	private ItemSlot _itemSlot;
	private ItemType _itemType;
	private boolean _stackable;
	private boolean _tradable;
	private int _stamina;
	private int _strength;
	private int _dexterity;
	private int _intelect;
	private SkillHolder _skillHolder;
	
	public ItemTemplateHolder(int _itemId, ItemSlot _itemSlot, ItemType _itemType, boolean _stackable, boolean _tradable, int _stamina, int _strength, int _dexterity, int _intelect, SkillHolder _skillHolder)
	{
		this._itemId = _itemId;
		this._itemSlot = _itemSlot;
		this._itemType = _itemType;
		this._stackable = _stackable;
		this._tradable = _tradable;
		this._stamina = _stamina;
		this._strength = _strength;
		this._dexterity = _dexterity;
		this._intelect = _intelect;
		this._skillHolder = _skillHolder;
	}
	
	/**
	 * @return the _itemId
	 */
	public int getItemId()
	{
		return _itemId;
	}
	
	/**
	 * @param _itemId the _itemId to set
	 */
	public void setItemId(int _itemId)
	{
		this._itemId = _itemId;
	}
	
	/**
	 * @return the _itemSlot
	 */
	public ItemSlot getItemSlot()
	{
		return _itemSlot;
	}
	
	/**
	 * @param _itemSlot the _itemSlot to set
	 */
	public void setItemSlot(ItemSlot _itemSlot)
	{
		this._itemSlot = _itemSlot;
	}
	
	/**
	 * @return the _itemType
	 */
	public ItemType getItemType()
	{
		return _itemType;
	}
	
	/**
	 * @param _itemType the _itemType to set
	 */
	public void setItemType(ItemType _itemType)
	{
		this._itemType = _itemType;
	}
	
	/**
	 * @return the _stackable
	 */
	public boolean isStackable()
	{
		return _stackable;
	}
	
	/**
	 * @param _stackable the _stackable to set
	 */
	public void setStackable(boolean _stackable)
	{
		this._stackable = _stackable;
	}
	
	/**
	 * @return the _tradable
	 */
	public boolean isTradable()
	{
		return _tradable;
	}
	
	/**
	 * @param _tradable the _tradable to set
	 */
	public void setTradable(boolean _tradable)
	{
		this._tradable = _tradable;
	}
	
	/**
	 * @return the _stamina
	 */
	public int getStamina()
	{
		return _stamina;
	}
	
	/**
	 * @param _stamina the _stamina to set
	 */
	public void setStamina(int _stamina)
	{
		this._stamina = _stamina;
	}
	
	/**
	 * @return the _strength
	 */
	public int getStrength()
	{
		return _strength;
	}
	
	/**
	 * @param _strength the _strength to set
	 */
	public void setStrength(int _strength)
	{
		this._strength = _strength;
	}
	
	/**
	 * @return the _dexterity
	 */
	public int getDexterity()
	{
		return _dexterity;
	}
	
	/**
	 * @param _dexterity the _dexterity to set
	 */
	public void setDexterity(int _dexterity)
	{
		this._dexterity = _dexterity;
	}
	
	/**
	 * @return the _intelect
	 */
	public int getIntelect()
	{
		return _intelect;
	}
	
	/**
	 * @param _intelect the _intelect to set
	 */
	public void setIntelect(int _intelect)
	{
		this._intelect = _intelect;
	}
	
	/**
	 * @return the _skillHolder
	 */
	public SkillHolder getSkillHolder()
	{
		return _skillHolder;
	}
	
	/**
	 * @param _skillHolder the _skillHolder to set
	 */
	public void setSkillHolder(SkillHolder _skillHolder)
	{
		this._skillHolder = _skillHolder;
	}
	
}
