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
import org.apache.log4j.Logger;
import wwmm.pubcrawler.Unicode;
import wwmm.pubcrawler.utils.XPathUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Sam Adams
 */
public class AcsTools {

    private static final Logger LOG = Logger.getLogger(AcsTools.class);

    private static final Map<String,Character> ENTITY_MAP;

    static {
        Map<String,Character> map = new HashMap<String, Character>();

        // 0300 = grave

        map.put("/entityImage/?text=0041,0300", Unicode.Latin1_Supplement.  Latin_Capital_Letter_A_With_Grave);

        map.put("/entityImage/?text=0061,0300", Unicode.Latin1_Supplement.  Latin_Small_Letter_A_With_Grave);
        map.put("/entityImage/?text=0065,0300", Unicode.Latin1_Supplement.  Latin_Small_Letter_E_With_Grave);
        map.put("/entityImage/?text=0069,0300", Unicode.Latin1_Supplement.  Latin_Small_Letter_I_With_Grave);
        map.put("/entityImage/?text=006f,0300", Unicode.Latin1_Supplement.  Latin_Small_Letter_O_With_Grave);
        map.put("/entityImage/?text=0075,0300", Unicode.Latin1_Supplement.  Latin_Small_Letter_U_With_Grave);


        // 0301 = acute

        map.put("/entityImage/?text=0041,0301", Unicode.Latin1_Supplement.  Latin_Capital_Letter_A_With_Acute);
        map.put("/entityImage/?text=0043,0301", Unicode.LatinExtendedA.     Latin_Capital_Letter_C_With_Acute);
        map.put("/entityImage/?text=0045,0301", Unicode.Latin1_Supplement.  Latin_Capital_Letter_E_With_Acute);
        map.put("/entityImage/?text=0049,0301", Unicode.Latin1_Supplement.  Latin_Capital_Letter_I_With_Acute);
        map.put("/entityImage/?text=0053,0301", Unicode.LatinExtendedA.     Latin_Capital_Letter_S_With_Acute);

        map.put("/entityImage/?text=0061,0301", Unicode.Latin1_Supplement.  Latin_Small_Letter_A_With_Acute);
        map.put("/entityImage/?text=0063,0301", Unicode.LatinExtendedA.     Latin_Small_Letter_C_With_Acute);
        map.put("/entityImage/?text=0065,0301", Unicode.Latin1_Supplement.  Latin_Small_Letter_E_With_Acute);
        map.put("/entityImage/?text=0069,0301", Unicode.Latin1_Supplement.  Latin_Small_Letter_I_With_Acute);
        map.put("/entityImage/?text=006e,0301", Unicode.LatinExtendedA.     Latin_Small_Letter_N_With_Acute);
        map.put("/entityImage/?text=006f,0301", Unicode.Latin1_Supplement.  Latin_Small_Letter_O_With_Acute);
        map.put("/entityImage/?text=0073,0301", Unicode.LatinExtendedA.     Latin_Small_Letter_S_With_Acute);
        map.put("/entityImage/?text=0075,0301", Unicode.Latin1_Supplement.  Latin_Small_Letter_U_With_Acute);
        map.put("/entityImage/?text=0079,0301", Unicode.Latin1_Supplement.  Latin_Small_Letter_Y_With_Acute);
        map.put("/entityImage/?text=007a,0301", Unicode.LatinExtendedA.     Latin_Small_Letter_Z_With_Acute);


        // 0302 = circumflex

        map.put("/entityImage/?text=0041,0302", Unicode.Latin1_Supplement.  Latin_Capital_Letter_A_With_Circumflex);
//                    map.put("/entityImage/?text=004c,0302", Unicode.LatinExtendedA.     Latin_Capital_Letter_L_With_Circumflex);

        map.put("/entityImage/?text=0061,0302", Unicode.Latin1_Supplement.  Latin_Small_Letter_A_With_Circumflex);
        map.put("/entityImage/?text=0065,0302", Unicode.Latin1_Supplement.  Latin_Small_Letter_E_With_Circumflex);
        map.put("/entityImage/?text=0069,0302", Unicode.Latin1_Supplement.  Latin_Small_Letter_I_With_Circumflex);
        map.put("/entityImage/?text=006f,0302", Unicode.Latin1_Supplement.  Latin_Small_Letter_O_With_Circumflex);


        // 0303 = tilde

        map.put("/entityImage/?text=0061,0303", Unicode.Latin1_Supplement.  Latin_Small_Letter_A_With_Tilde);
        map.put("/entityImage/?text=006e,0303", Unicode.Latin1_Supplement.  Latin_Small_Letter_N_With_Tilde);
//                    map.put("/entityImage/?text=0073,0303", Unicode.LatinExtendedA.     Latin_Small_Letter_S_With_Tilde);


        // 0305 = macron

        map.put("/entityImage/?text=0045,0305", Unicode.LatinExtendedA.     Latin_Capital_Letter_E_With_Macron);
        map.put("/entityImage/?text=004f,0305", Unicode.LatinExtendedA.     Latin_Capital_Letter_O_With_Macron);

        map.put("/entityImage/?text=0065,0305", Unicode.LatinExtendedA.     Latin_Small_Letter_E_With_Macron);


        // 0306 = breve

        map.put("/entityImage/?text=0061,0306", Unicode.LatinExtendedA.     Latin_Small_Letter_A_With_Breve);
        map.put("/entityImage/?text=0065,0306", Unicode.LatinExtendedA.     Latin_Small_Letter_E_With_Breve);
        map.put("/entityImage/?text=0067,0306", Unicode.LatinExtendedA.     Latin_Small_Letter_G_With_Breve);


        // 0307 = dot above

        map.put("/entityImage/?text=0049,0307", Unicode.LatinExtendedA.     Latin_Capital_Letter_I_With_Dot_Above);
        map.put("/entityImage/?text=005a,0307", Unicode.LatinExtendedA.     Latin_Capital_Letter_Z_With_Dot_Above);

        map.put("/entityImage/?text=0065,0307", Unicode.LatinExtendedA.     Latin_Small_Letter_E_With_Dot_Above);
//                    map.put("/entityImage/?text=0075,0307", Unicode.LatinExtendedA.     Latin_Small_Letter_U_With_Dot_Above);
        map.put("/entityImage/?text=007a,0307", Unicode.LatinExtendedA.     Latin_Small_Letter_Z_With_Dot_Above);


        // 0308 = diaeresis

        map.put("/entityImage/?text=0041,0308", Unicode.Latin1_Supplement.  Latin_Capital_Letter_A_With_Diaeresis);
        map.put("/entityImage/?text=004f,0308", Unicode.Latin1_Supplement.  Latin_Capital_Letter_O_With_Diaeresis);
        map.put("/entityImage/?text=0055,0308", Unicode.Latin1_Supplement.  Latin_Capital_Letter_U_With_Diaeresis);

        map.put("/entityImage/?text=0061,0308", Unicode.Latin1_Supplement.  Latin_Small_Letter_A_With_Diaeresis);
        map.put("/entityImage/?text=0065,0308", Unicode.Latin1_Supplement.  Latin_Small_Letter_E_With_Diaeresis);
        map.put("/entityImage/?text=0069,0308", Unicode.Latin1_Supplement.  Latin_Small_Letter_I_With_Diaeresis);
        map.put("/entityImage/?text=006f,0308", Unicode.Latin1_Supplement.  Latin_Small_Letter_O_With_Diaeresis);
        map.put("/entityImage/?text=0075,0308", Unicode.Latin1_Supplement.  Latin_Small_Letter_U_With_Diaeresis);

        ENTITY_MAP = Collections.unmodifiableMap(map);
    }

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

                    Character c = ENTITY_MAP.get(src);
                    if (c != null) {
                        element.replaceChild(child, new Text(c.toString()));
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
                        LOG.warn("Unknown ACS entity: "+src);
                    }
                } else {
                    normaliseImages((Element)child);
                }
            }
        }
    }

}
