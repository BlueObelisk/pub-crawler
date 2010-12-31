package wwmm.pubcrawler.core.utils;

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
}