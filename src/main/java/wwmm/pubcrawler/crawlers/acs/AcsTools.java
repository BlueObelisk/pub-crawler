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

                    if ("/entityImage/?text=0065,0300".equals(src)) {
                        element.replaceChild(child, new Text("\u00e8"));        // egrave: small a with grave
                    }

                    else if ("/entityImage/?text=0041,0301".equals(src)) {
                        element.replaceChild(child, new Text("\u00c1"));        // Aacute: capital A with acute
                    }
                    else if ("/entityImage/?text=0045,0301".equals(src)) {
                        element.replaceChild(child, new Text("\u00c9"));        // Eacute: capital E with acute
                    }

                    else if ("/entityImage/?text=0061,0301".equals(src)) {
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
                    else if ("/entityImage/?text=006e,0301".equals(src)) {
                        element.replaceChild(child, new Text("\u0144"));        // small n with acute
                    }
                    else if ("/entityImage/?text=006f,0301".equals(src)) {
                        element.replaceChild(child, new Text("\u00f3"));        // oacute: small o with acute
                    }
                    else if ("/entityImage/?text=0075,0301".equals(src)) {
                        element.replaceChild(child, new Text("\u00fa"));        // uacute: small u with acute
                    }
                    else if ("/entityImage/?text=0079,0301".equals(src)) {
                        element.replaceChild(child, new Text("\u00fd"));        // small y with acute
                    }

                    else if ("/entityImage/?text=006f,0302".equals(src)) {
                        element.replaceChild(child, new Text("\u00f4"));        // small o with circumflex
                    }

                    else if ("/entityImage/?text=0061,0303".equals(src)) {
                        element.replaceChild(child, new Text("\u00e3"));        // small a with tilde
                    }
                    else if ("/entityImage/?text=006e,0303".equals(src)) {
                        element.replaceChild(child, new Text("\u00f1"));        // small n with tilde
                    }

                    else if ("/entityImage/?text=0045,0305".equals(src)) {
                        element.replaceChild(child, new Text("\u0112"));        // capital E with macron
                    }

                    else if ("/entityImage/?text=0067,0306".equals(src)) {
                        element.replaceChild(child, new Text("\u011f"));        // small g with breve
                    }

                    else if ("/entityImage/?text=007a,0307".equals(src)) {
                        element.replaceChild(child, new Text("\u017c"));        // small z with dot above ??
                    }

                    else if ("/entityImage/?text=0061,0308".equals(src)) {
                        element.replaceChild(child, new Text("\u00e4"));        // auml: small a with diaeresis
                    }
                    else if ("/entityImage/?text=0065,0308".equals(src)) {
                        element.replaceChild(child, new Text("\u00eb"));        // euml: small e with diaeresis
                    }
                    else if ("/entityImage/?text=0069,0308".equals(src)) {
                        element.replaceChild(child, new Text("\u00ef"));        // iuml: small i with diaeresis
                    }
                    else if ("/entityImage/?text=006f,0308".equals(src)) {
                        element.replaceChild(child, new Text("\u00f6"));        // ouml: small o with diaeresis
                    }
                    else if ("/entityImage/?text=0075,0308".equals(src)) {
                        element.replaceChild(child, new Text("\u00fc"));        // uuml: small u with diaeresis
                    }

                    else if ("/entityImage/?text=0061,030a".equals(src)) {
                        element.replaceChild(child, new Text("\u00e5"));        // small a with ring above
                    }

                    else if ("/entityImage/?text=006f,030b".equals(src)) {
                        element.replaceChild(child, new Text("\u0151"));        // small o with double acute 
                    }

                    // http://pubs.acs.org/doi/suppl/10.1021/ic902279k
                    else if ("/entityImage/?text=0043,030c".equals(src)) {
                        element.replaceChild(child, new Text("\u010c"));        // capital C with caron
                    }
                    else if ("/entityImage/?text=0053,030c".equals(src)) {
                        element.replaceChild(child, new Text("\u0160"));        // capital S with caron
                    }

                    else if ("/entityImage/?text=0063,030c".equals(src)) {
                        element.replaceChild(child, new Text("\u010d"));        // small c with caron
                    }
                    else if ("/entityImage/?text=0073,030c".equals(src)) {
                        element.replaceChild(child, new Text("\u0161"));        // small s with caron
                    }

                    else if ("/entityImage/?text=0063,0327".equals(src)) {
                        element.replaceChild(child, new Text("\u00e7"));        // ccedil: small c with cedilla
                    }
                    else if ("/entityImage/?text=0065,0327".equals(src)) {
                        element.replaceChild(child, new Text("\u0229"));        // small e with cedilla
                    }
                    else if ("/entityImage/?text=0074,0327".equals(src)) {
                        element.replaceChild(child, new Text("\u0163"));        // small t with cedilla ??
                    }

                    // http://pubs.acs.org/doi/suppl/10.1021/ic902279k
                    else if ("/entityImage/?text=006f,0337".equals(src)) {
                        element.replaceChild(child, new Text("\u00f8"));        // small o with stroke ??
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
