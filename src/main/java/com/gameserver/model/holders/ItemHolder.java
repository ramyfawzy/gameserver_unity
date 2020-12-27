package com.gameserver.model.holders;

/**
 * @author Ramy Ibrahim
 */
public class ItemHolder
{
	private final ItemTemplateHolder _itemTemplate;
	private int _quantity = 1;
	private int _enchant = 0;
	
	/**
	 * @param _itemTemplate
	 */
	public ItemHolder(ItemTemplateHolder _itemTemplate)
	{
		this._itemTemplate = _itemTemplate;
	}
	
	/**
	 * @return the _quantity
	 */
	public int getQuantity()
	{
		return _quantity;
	}
	
	/**
	 * @param _quantity the _quantity to set
	 */
	public void setQuantity(int _quantity)
	{
		this._quantity = _quantity;
	}
	
	/**
	 * @return the _enchant
	 */
	public int getEnchant()
	{
		return _enchant;
	}
	
	/**
	 * @param _enchant the _enchant to set
	 */
	public void setEnchant(int _enchant)
	{
		this._enchant = _enchant;
	}
	
	/**
	 * @return the _itemTemplate
	 */
	public ItemTemplateHolder getItemTemplate()
	{
		return _itemTemplate;
	}
	
}
