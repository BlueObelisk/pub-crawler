/*******************************************************************************
 * Copyright 2010 Nick Day
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package wwmm.pubcrawler.core.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.ParsingException;
import nu.xom.ValidityException;

import org.apache.log4j.Logger;

/**
 * <p>
 * Utility methods used throughout the crawler project.
 * </p>
 * 
 * @author Nick Day
 * @version 1.1
 */
public class Utils {

	private static final Logger LOG = Logger.getLogger(Utils.class);
	
	/**
	 * <p>
	 * Generates and returns a random UUID string.
	 * </p>
	 * 
	 * @return a random UUID string, e.g. 
	 * urn:uuid:fab87b8d-74c9-407b-9590-c2898ae82fff
	 */
	public static String getRandomUuidString() {
		UUID uuid = UUID.randomUUID();
		return "urn:uuid:"+uuid.toString();
	}

	/**
	 * <p>
	 * The <code>sleep</code> method is called by all methods in this class which 
	 * call a method in the superclass.  A random number between 0 and 
	 * <code>maxSleep</code> is used to determine how long a period the process 
	 * should sleep for before continuing.
	 * </p>
	 */
	public static void sleep(int maxSleep) {
		int maxTime = Integer.valueOf(maxSleep);
		try {
			Thread.sleep(((int) (maxTime * Math.random())));
		} catch (InterruptedException e) {
			LOG.debug("Sleep interrupted.");
		}
	}

    /**
	 * <p>
	 * Parses the contents of an <code>InputStream</code> into an
	 * XML document.
	 * </p>
	 * 
	 * @param in - the InputStream you want converted to XML.
	 * 
	 * @return XML document representing the contents of the 
	 * provided InputStream.
	 */
	public static Document parseXml(InputStream in) {
		return parseXml(new Builder(), in);
	}

	/**
	 * <p>
	 * Parses the contents of an <code>InputStream</code> into an
	 * XML document using a specified XML builder.
	 * </p>
	 * 
	 * @param builder - the XML builder you want the parser to use
	 * in creating the XML document.
	 * @param in - the InputStream you want converted to XML.
	 * 
	 * @return XML document representing the contents of the 
	 * provided InputStream.
	 */
	public static Document parseXml(Builder builder, InputStream in) {
		Document doc;
		try {
			doc = builder.build(in);
		} catch (ValidityException e) {
			throw new RuntimeException("Invalid XML", e);
		} catch (ParsingException e) {
			throw new RuntimeException("Could not parse XML", e);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Unsupported encoding", e);
		} catch (IOException e) {
			throw new RuntimeException("Input exception", e);
		}
		return doc;
	}

	/**
	 * <p>
	 * Parses the contents of a <code>Reader</code> into an
	 * XML document.
	 * </p>
	 * 
	 * @param reader - the Reader you want converted to XML.
	 * 
	 * @return XML document representing the contents of the 
	 * provided Reader.
	 */
	public static Document parseXml(Reader reader) {
		return Utils.parseXml(new Builder(), reader);
	}

	/**
	 * <p>
	 * Parses the contents of a <code>Reader</code> into an
	 * XML document using a specified XML builder.
	 * </p>
	 * 
	 * @param builder - the XML builder you want the parser to use
	 * in creating the XML document.
	 * @param reader - the Reader you want converted to XML.
	 * 
	 * @return XML document representing the contents of the 
	 * provided Reader.
	 */
	public static Document parseXml(Builder builder, Reader reader) {
		Document doc;
		try {
			doc = builder.build(reader);
		} catch (ValidityException e) {
			throw new RuntimeException("Invalid XML", e);
		} catch (ParsingException e) {
			throw new RuntimeException("Could not parse XML", e);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Unsupported encoding", e);
		} catch (IOException e) {
			throw new RuntimeException("Input exception", e);
		}
		return doc;
	}

	/**
	 * <p>
	 * Parses the contents of the provided <code>File</code> 
	 * into an XML document.
	 * </p>
	 * 
	 * @param file you want to be parsed.
	 * 
	 * @return XML document containing the contents of the file.
	 */
	public static Document parseXml(File file) {
		try {
			return Utils.parseXml(new FileReader(file));
		} catch (FileNotFoundException e) {
			throw new RuntimeException("Could not find file "+file.getAbsolutePath(), e);
		}
	}

}
