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
package wwmm.pubcrawler.core;

/**
 * <p>
 * The <code>CrawlerRuntimeException</code> is meant to be used for
 * generic runtime exceptions that occur during crawler execution.
 * </p>
 * 
 * @author Nick Day
 * @version 1.1
 * 
 */
public class CrawlerRuntimeException extends RuntimeException {

	private static final long serialVersionUID = -8993316955991175892L;

	public CrawlerRuntimeException() {
		super();
	}

	public CrawlerRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public CrawlerRuntimeException(String message) {
		super(message);
	}

	public CrawlerRuntimeException(Throwable cause) {
		super(cause);
	}
	
}
