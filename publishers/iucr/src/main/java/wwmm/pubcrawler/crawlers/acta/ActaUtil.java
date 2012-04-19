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
        final Map<String,Character> map = new HashMap<String, Character>();

        map.put("...", Unicode.ELIPSIS);

        map.put("less-than or equal to", Unicode.MathematicalOperators.Less_Than_Or_Equal_To);
        map.put("rightwards arrow", Unicode.Arrows.Rightwards_Arrow);
        map.put("triple bond", Unicode.MathematicalOperators.Identical_To);

        map.put("[alpha]",   Unicode.GREEK_ALPHA_LC);
        map.put("[beta]",    Unicode.GREEK_BETA_LC);
        map.put("[gamma]",   Unicode.GREEK_GAMMA_LC);
        map.put("[delta]",   Unicode.GREEK_DELTA_LC);
        map.put("[epsilon]", Unicode.GREEK_EPSION_LC);
        map.put("[zeta]",    Unicode.GREEK_ZETA_LC);
        map.put("[eta]",     Unicode.GREEK_ETA_LC);
        map.put("[theta]",   Unicode.GREEK_THETA_LC);
        map.put("[iota]",    Unicode.GREEK_IOTA_LC);
        map.put("[kappa]",   Unicode.GREEK_KAPPA_LC);
        map.put("[lambda]",  Unicode.GREEK_LAMBDA_LC);
        map.put("[mu]",      Unicode.GREEK_MU_LC);
        map.put("[nu]",      Unicode.GREEK_NU_LC);
        map.put("[xi]",      Unicode.GREEK_XI_LC);
        map.put("[omnicron]", Unicode.GREEK_OMNICRON_LC);
        map.put("[pi]",      Unicode.GREEK_PI_LC);
        map.put("[rho]",     Unicode.GREEK_RHO_LC);
//        map.put("[]", Unicode.GREEK_FINAL_SIGMA_LC);
        map.put("[sigma]",   Unicode.GREEK_SIGMA_LC);
        map.put("[tau]",     Unicode.GREEK_TAU_LC);
        map.put("[upsilon]", Unicode.GREEK_UPSILON_LC);
//        map.put("[phi]",     Unicode.GREEK_PHI_LC);
        map.put("[chi]",     Unicode.GREEK_CHI_LC);
        map.put("[psi]",     Unicode.GREEK_PSI_LC);
        map.put("[omega]",   Unicode.GREEK_OMEGA_LC);

        map.put("[varphi]", Unicode.GREEK_PHI_LC);

        map.put("[Alpha]",   Unicode.GREEK_ALPHA_UC);
        map.put("[Beta]",    Unicode.GREEK_BETA_UC);
        map.put("[Gamma]",   Unicode.GREEK_GAMMA_UC);
        map.put("[Delta]",   Unicode.GREEK_DELTA_UC);
        map.put("[Epsilon]", Unicode.GREEK_EPSION_UC);
        map.put("[Zeta]",    Unicode.GREEK_ZETA_UC);
        map.put("[Eta]",     Unicode.GREEK_ETA_UC);
        map.put("[Theta]",   Unicode.GREEK_THETA_UC);
        map.put("[Iota]",    Unicode.GREEK_IOTA_UC);
        map.put("[Kappa]",   Unicode.GREEK_KAPPA_UC);
        map.put("[Lambda]",  Unicode.GREEK_LAMBDA_UC);
        map.put("[Mu]",      Unicode.GREEK_MU_UC);
        map.put("[Nu]",      Unicode.GREEK_NU_UC);
        map.put("[Xi]",      Unicode.GREEK_XI_UC);
        map.put("[Omnicron]", Unicode.GREEK_OMNICRON_UC);
        map.put("[Pi]",      Unicode.GREEK_PI_UC);
        map.put("[Rho]",     Unicode.GREEK_RHO_UC);
        map.put("[Sigma]",   Unicode.GREEK_SIGMA_UC);
        map.put("[Tau]",     Unicode.GREEK_TAU_UC);
        map.put("[Upsilon]", Unicode.GREEK_UPSILON_UC);
        map.put("[Phi]",     Unicode.GREEK_PHI_UC);
        map.put("[Chi]",     Unicode.GREEK_CHI_UC);
        map.put("[Psi]",     Unicode.GREEK_PSI_UC);
        map.put("[Omega]",   Unicode.GREEK_OMEGA_UC);

        map.put("[asymptotically equal to]", Unicode.ASYMPTOTICALLY_EQUAL_TO);
        map.put("~", Unicode.TILDE_OPERATOR);

        ENT_MAP = Collections.unmodifiableMap(map);
    }

    private static Character getEntity(final String alt) {
        return ENT_MAP.get(alt);
    }

    public static void normaliseHtml(final Element html) {
        ActaUtil.replaceEntities(html);
        ActaUtil.removeSpanElements(html);
    }

    public static void replaceEntities(final Element parent) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            final Node node = parent.getChild(i);
            if (node instanceof Element) {
                final Element child = (Element) node;
                if ("img".equals(child.getLocalName())) {
                    final String alt = child.getAttributeValue("alt");
                    final Character ent = getEntity(alt);
                    if (ent == null) {
                        LOG.warn("Unsupported entity: "+alt);
                    } else {
                        parent.replaceChild(child, new Text(ent.toString()));
                    }
                }
            }
        }
    }

    public static void removeSpanElements(final Element parent) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            final Node node = parent.getChild(i);
            if (node instanceof Element) {
                final Element child = (Element) node;
                if ("span".equals(child.getLocalName())) {
                    child.detach();
                    while (child.getChildCount() > 0) {
                        final Node n = child.getChild(0);
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
