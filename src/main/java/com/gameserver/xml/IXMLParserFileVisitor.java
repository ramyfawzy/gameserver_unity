package com.gameserver.xml;

import java.io.File;

/**
 * Note: any implementation of visit needs to be thread-safe! Multiple files may
 * be visited in parallel to gather data distributed over multiple files.
 */
public interface IXMLParserFileVisitor {
	void visit(File file) throws Exception;
}
