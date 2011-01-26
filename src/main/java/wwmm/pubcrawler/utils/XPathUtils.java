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

package wwmm.pubcrawler.utils;

import nu.xom.Document;
import nu.xom.Node;
import nu.xom.Nodes;

import java.util.ArrayList;
import java.util.List;

public class XPathUtils {
    /**
     * <p>
     * Convenience method for doing an XPath query and receiving
     * back a list of <code>Node</code> rather than the <code>Nodes</code>
     * returned in the default in XOM.
     * </p>
     *
     * @param doc   you want to query
     * @param xpath - the XPath String you want to use in the query.
     * @return list of <code>Node</code> that represent the parts of
     *         the queried XML document that matched the provided XPath query.
     */
    public static List<Node> queryHTML(Document doc, String xpath) {
        Node node = doc.getRootElement();
        return queryHTML(node, xpath);
    }

    /**
     * <p>
     * Convenience method for doing an XPath query and receiving
     * back a list of <code>Node</code> rather than the <code>Nodes</code>
     * returned in the default in XOM.
     * </p>
     *
     * @param node  you want to query
     * @param xpath - the XPath String you want to use in the query.
     * @return list of <code>Node</code> that represent the parts of
     *         the queried XML document that matched the provided XPath query.
     */
    public static List<Node> queryHTML(Node node, String xpath) {
        Nodes nodes = node.query(xpath, XHtml.XPATH_CONTEXT);
        return getNodeListFromNodes(nodes);
    }

    /**
     * <p>
     * Convenience method for getting a list of <code>Node</code>
     * from <code>Nodes</code>.
     * </p>
     *
     * @param nodes you want converted to a list.
     * @return list of <Node> containing the same XML elemenst as
     *         the provided <code>Nodes</code>.
     */
    public static List<Node> getNodeListFromNodes(Nodes nodes) {
        List<Node> nodeList = new ArrayList<Node>(nodes.size());
        for (int i = 0; i < nodes.size(); i++) {
            nodeList.add(nodes.get(i));
        }
        return nodeList;
    }



    public static String getString(Node root, String xpath) {
        Node node = getNode(root, xpath);
        return node == null ? null : node.getValue();
    }

    public static List<String> getStrings(Node root, String xpath) {
        Nodes nodes = root.query(xpath, XHtml.XPATH_CONTEXT);
        List<String> strings = new ArrayList<String>();
        for (int i = 0; i < nodes.size(); i++) {
            strings.add(nodes.get(i).getValue());
        }
        return strings;
    }

    public static Node getNode(Node root, String xpath) {
        Nodes nodes = root.query(xpath, XHtml.XPATH_CONTEXT);
        if (nodes.size() == 1) {
            return nodes.get(0);
        }
        if (nodes.size() == 0) {
            return null;
        }
        throw new IllegalArgumentException("XPath returned multiple nodes: "+nodes.size());
    }

}