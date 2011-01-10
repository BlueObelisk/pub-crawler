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

package wwmm.pubcrawler.crawlers.acs;

import nu.xom.*;
import org.xml.sax.AttributeList;
import wwmm.pubcrawler.utils.XPathUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author Sam Adams
 */
public class AcsTools {

    public static Element getTitleHtml(Node node) {
        Element element = (Element) XPathUtils.getNode(node, ".//x:h1[@class='articleTitle']");
        Element copy = new Element("h1", "http://www.w3.org/1999/xhtml");
        for (int i = 0; i < element.getChildCount(); i++) {
            copy.appendChild(element.getChild(i).copy());
        }
        return copy;
    }

    public static void copyChildren(ParentNode source, ParentNode target) {
        for (int i = 0; i < source.getChildCount(); i++) {
            target.appendChild(source.getChild(i).copy());
        }
    }

    public static String toHtml(Element element) {
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            Serializer ser = new Serializer(bytes, "UTF-8") {
                @Override
                protected void writeXMLDeclaration() {
                    // skip XML declaration
                }
            };
            ser.write(new Document(element));

            String s = bytes.toString("UTF-8");
            return s.trim();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void normaliseImages(Element element) {
        for (int i = 0; i < element.getChildCount(); i++) {
            Node child = element.getChild(i);
            if (child instanceof Element) {
                Element childElement = (Element) child;
                if ("img".equals(childElement.getLocalName())) {
                    String src = childElement.getAttributeValue("src");

                    if ("/entityImage/?text=0061,0301".equals(src)) {
                        element.replaceChild(child, new Text("\u00e1"));        // aacute: small a with acute
                    }
                    else if ("/entityImage/?text=0063,0301".equals(src)) {
                        element.replaceChild(child, new Text("\u0107"));        // small c with acute
                    }
                    else if ("/entityImage/?text=0065,0301".equals(src)) {
                        element.replaceChild(child, new Text("\u00e9"));        // eacute: small e with acute
                    }
                    else if ("/entityImage/?text=0069,0301".equals(src)) {
                        element.replaceChild(child, new Text("\u00ed"));        // iacute: small i with acute
                    }

                    else if ("/entityImage/?text=0045,0305".equals(src)) {
                        element.replaceChild(child, new Text("\u0112"));        // capital E with macron
                    }

                    else if ("/entityImage/?text=0061,0308".equals(src)) {
                        element.replaceChild(child, new Text("\u00e4"));        // auml: small a with diaeresis
                    }
                    else if ("/entityImage/?text=0065,0308".equals(src)) {
                        element.replaceChild(child, new Text("\u00eb"));        // euml: small e with diaeresis
                    }
                    else if ("/entityImage/?text=006f,0308".equals(src)) {
                        element.replaceChild(child, new Text("\u00f6"));        // ouml: small o with diaeresis
                    }

                    else if ("/entityImage/?text=0063,030c".equals(src)) {
                        element.replaceChild(child, new Text("\u010d"));        // small c with caron
                    }

                    else if ("/entityImage/?text=0063,0327".equals(src)) {
                        element.replaceChild(child, new Text("\u00e7"));        // ccedil: small c with cedilla
                    }

                    else {
                        System.out.println(src);
                    }
                } else {
                    normaliseImages((Element)child);
                }
            }
        }
    }

}
