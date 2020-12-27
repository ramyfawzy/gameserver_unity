package com.gameserver.util;

/**
 * @author Ramy Ibrahim
 */
public final class Util
{
	public static void printSection(String section)
	{
		System.out.println(section);
	}
	
	public static final char[] ILLEGAL_CHARACTERS =
	{
		'/',
		'\n',
		'\r',
		'\t',
		'\0',
		'\f',
		'`',
		'?',
		'*',
		'\\',
		'<',
		'>',
		'|',
		'\"',
		'{',
		'}',
		'(',
		')'
	};
	
	public static long hexStringToLong(String hex)
	{
		return Long.parseLong(hex, 16);
	}
}
