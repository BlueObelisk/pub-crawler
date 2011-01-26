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

import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Serializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

/**
 * @author Sam Adams
 */
public class HtmlUtil {

    public static String writeAscii(Document html) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            AsciiSerializer ser = new AsciiSerializer(out);
            ser.write(html);
            return out.toString("ASCII");
        } catch (IOException e) {
            throw new RuntimeException("ASCII encoding not supported", e);
        }
    }


    private static class AsciiSerializer extends Serializer {

        private AsciiSerializer(OutputStream out) throws UnsupportedEncodingException {
            super(out, "ASCII");
        }

        @Override
        protected void writeXMLDeclaration() throws IOException {
            // ignore
        }

        @Override
        protected void writeNamespaceDeclarations(Element element) throws IOException {
            // ignore
        }
        
    }

}
