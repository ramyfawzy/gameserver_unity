package com.gameserver.xml;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.List;


import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.convert.Registry;
import org.simpleframework.xml.convert.RegistryStrategy;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.strategy.Strategy;
import org.simpleframework.xml.stream.OutputNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

public class XMLParser {
	private static final Logger logger = LoggerFactory.getLogger(XMLParser.class);

	public void write(final Object o, final File file) throws Exception {
		final Serializer serializer = new Persister();
		serializer.write(o, file);
	}

	public void write(final Object o, final OutputStream stream) throws Exception {
		final Serializer serializer = new Persister();
		serializer.write(o, stream);
	}

	public void write(final Object o, final Writer writer) throws Exception {
		final Serializer serializer = new Persister();
		serializer.write(o, writer);
	}

	public void write(final Object o, final OutputNode node) throws Exception {
		final Serializer serializer = new Persister();
		serializer.write(o, node);
	}

	public <T> T load(final Class<T> clazz, final InputStream stream) throws Exception {
		final Serializer serializer = getSerializer();
		return serializer.read(clazz, stream);
	}

	public <T> T load(final Class<T> clazz, final String xml) throws Exception {
		final Serializer serializer = getSerializer();
		return serializer.read(clazz, xml);
	}

	public <T> T load(final Class<T> clazz, final File file) throws Exception {
		final Serializer serializer = getSerializer();
		return serializer.read(clazz, file);
	}

	private Serializer getSerializer() throws Exception {
		final Registry registry = new Registry();
//		registry.bind(AmsContainer.class, SeafightAmsContainerConverter.class);
		final Strategy strategy = new RegistryStrategy(registry);
		final Serializer serializer = new Persister(strategy);
		return serializer;
	}

	public <T> T load(final Class<T> clazz, final Reader reader) throws Exception {
		final Serializer serializer = getSerializer();
		return serializer.read(clazz, reader);
	}

	/**
	 * make sure your XMLParserFileVisitor is thread safe when using this speedy
	 * variant
	 */
	protected void visitParallel(final String path, final IXMLParserFileVisitor visitor) {
		var folderUri = getClass().getClassLoader().getResource(path);
		final File folder = new File(folderUri.getPath());
//		final File folder = new File(path);
		if (folder.isDirectory()) {
			final List<File> listFiles = Lists.newArrayList(folder.listFiles());
			listFiles.parallelStream().forEach(file -> {
				if (file.getName().endsWith(".xml")) {
					visitFile(visitor, file);
				}
			});
		} else {
			visitFile(visitor, folder);
		}
	}

	/**
	 * use this if there is a small amount of files and/or you don't want to
	 * deal with concurrency in your ParserFileVisitor implementation
	 */
	protected void visitSingleThreaded(final String path, final IXMLParserFileVisitor visitor) {
		final File folder = new File(path);
		if (folder.isDirectory()) {
			final File[] listFiles = folder.listFiles();
			if (listFiles == null) {
				logger.debug("Path {} is not valid", path);
				return;
			}
			for (final File file : listFiles) {
				if (!file.getName().endsWith(".xml")) {
					continue;
				}
				visitFile(visitor, file);
			}
		} else {
			visitFile(visitor, folder);
		}
	}

	private void visitFile(final IXMLParserFileVisitor visitor, final File file) {
		try {
			visitor.visit(file);
		} catch (final Exception e) {
			logger.error("failed to load " + file, e);
			throw new RuntimeException("failed to load " + file, e);
		}
	}
}
