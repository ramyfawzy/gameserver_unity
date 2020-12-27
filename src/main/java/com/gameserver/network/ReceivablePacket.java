package com.gameserver.network;

import java.io.ByteArrayInputStream;

/**
 * @author Ramy Ibrahim
 */
public class ReceivablePacket
{
	private final ByteArrayInputStream _bais;
	
	public ReceivablePacket(byte[] bytes)
	{
		_bais = new ByteArrayInputStream(bytes);
	}
	
	public String readString()
	{
		String result = "";
		try
		{
			result = new String(readBytes(readShort()), "UTF-8");
		}
		catch (Exception e)
		{
		}
		return result;
	}
	
	public byte[] readBytes(int length)
	{
		final byte[] result = new byte[length];
		for (int i = 0; i < length; i++)
		{
			result[i] = (byte) _bais.read();
		}
		return result;
	}
	
	public int readByte()
	{
		return _bais.read();
	}
	
	public int readShort()
	{
		return (_bais.read() & 0xff) | ((_bais.read() << 8) & 0xff00);
	}
	
	public int readInt()
	{
		int result = _bais.read() & 0xff;
		result |= (_bais.read() << 8) & 0xff00;
		result |= (_bais.read() << 0x10) & 0xff0000;
		result |= (_bais.read() << 0x18) & 0xff000000;
		return result;
	}
	
	public long readLong()
	{
		long result = _bais.read() & 0xff;
		result |= (_bais.read() & 0xffL) << 8L;
		result |= (_bais.read() & 0xffL) << 16L;
		result |= (_bais.read() & 0xffL) << 24L;
		result |= (_bais.read() & 0xffL) << 32L;
		result |= (_bais.read() & 0xffL) << 40L;
		result |= (_bais.read() & 0xffL) << 48L;
		result |= (_bais.read() & 0xffL) << 56L;
		return result;
	}
	
	public float readFloat()
	{
		int result = _bais.read() & 0xff;
		result |= (_bais.read() << 8) & 0xff00;
		result |= (_bais.read() << 0x10) & 0xff0000;
		result |= (_bais.read() << 0x18) & 0xff000000;
		return Float.intBitsToFloat(result);
	}
	
	public double readDouble()
	{
		long result = _bais.read() & 0xff;
		result |= (_bais.read() & 0xffL) << 8L;
		result |= (_bais.read() & 0xffL) << 16L;
		result |= (_bais.read() & 0xffL) << 24L;
		result |= (_bais.read() & 0xffL) << 32L;
		result |= (_bais.read() & 0xffL) << 40L;
		result |= (_bais.read() & 0xffL) << 48L;
		result |= (_bais.read() & 0xffL) << 56L;
		return Double.longBitsToDouble(result);
	}
}
