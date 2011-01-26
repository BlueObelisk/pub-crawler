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

package wwmm.pubcrawler.crawlers.acta;

import nu.xom.Element;
import nu.xom.Node;
import nu.xom.Text;
import org.apache.log4j.Logger;
import wwmm.pubcrawler.Unicode;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Sam Adams
 */
public class ActaUtil {

    private static final Logger LOG = Logger.getLogger(ActaUtil.class);

    private static final Map<String,Character> ENT_MAP;

    static {
        Map<String,Character> map = new HashMap<String, Character>();
        map.put("[alpha]", Unicode.GREEK_ALPHA_LC);
        map.put("[beta]", Unicode.GREEK_BETA_LC);
        map.put("[epsilon]", Unicode.GREEK_EPSION_LC);
        map.put("[eta]", Unicode.GREEK_ETA_LC);
        map.put("[mu]", Unicode.GREEK_MU_LC);
        map.put("[pi]", Unicode.GREEK_PI_LC);
        map.put("[kappa]", Unicode.GREEK_KAPPA_LC);
        map.put("[xi]", Unicode.GREEK_XI_LC);

        map.put("[asymptotically equal to]", Unicode.ASYMPTOTICALLY_EQUAL_TO);
        map.put("~", Unicode.TILDE_OPERATOR);

        ENT_MAP = Collections.unmodifiableMap(map);
    }

    private static Character getEntity(String alt) {
        return ENT_MAP.get(alt);
    }

    public static void normaliseHtml(Element html) {
        ActaUtil.replaceEntities(html);
        ActaUtil.removeSpanElements(html);
    }

    public static void replaceEntities(Element parent) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            Node node = parent.getChild(i);
            if (node instanceof Element) {
                Element child = (Element) node;
                if ("img".equals(child.getLocalName())) {
                    String alt = child.getAttributeValue("alt");
                    Character ent = getEntity(alt);
                    if (ent == null) {
                        LOG.warn("Unsupported entity: "+alt);
                    } else {
                        parent.replaceChild(child, new Text(ent.toString()));
                    }
                }
            }
        }
    }

    public static void removeSpanElements(Element parent) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            Node node = parent.getChild(i);
            if (node instanceof Element) {
                Element child = (Element) node;
                if ("span".equals(child.getLocalName())) {
                    child.detach();
                    while (child.getChildCount() > 0) {
                        Node n = child.getChild(0);
                        n.detach();
                        parent.insertChild(n, i);
                    }
                    i--;
                }
                removeSpanElements(child);
            }
        }
    }

}
