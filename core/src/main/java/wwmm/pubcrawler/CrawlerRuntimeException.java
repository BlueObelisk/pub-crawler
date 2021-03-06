/*
 * Copyright 2010-2011 Nick Day, Sam Adams
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
 */
package wwmm.pubcrawler;

import java.net.URI;

/**
 * <p>
 * The <code>CrawlerRuntimeException</code> is meant to be used for
 * generic runtime exceptions that occur during crawler execution.
 * </p>
 * 
 * @author Nick Day
 * @author Sam Adams
 * @version 1.2
 * 
 */
public class CrawlerRuntimeException extends RuntimeException {

    private final URI url;

	public CrawlerRuntimeException(final String message, final Throwable cause, final URI url) {
		super(message, cause);
        this.url = url;
    }

	public CrawlerRuntimeException(final String message, final URI url) {
		this(message, null, url);
	}

    public URI getUrl() {
        return url;
    }
}
