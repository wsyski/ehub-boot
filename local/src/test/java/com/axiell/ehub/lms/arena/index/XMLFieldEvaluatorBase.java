package com.axiell.ehub.lms.arena.index;

import net.sf.saxon.dom.NodeOverNodeInfo;
import net.sf.saxon.om.NodeInfo;
import org.w3c.dom.Node;

public abstract class XMLFieldEvaluatorBase {

    protected static Object[] nodeInfos2Nodes(final Object[] objects) {
        int len = objects.length;
        Object[] nodes = new Object[len];
        for (int i = 0; i < len; i++) {
            Object object = objects[i];
            if (Node.class.isAssignableFrom(object.getClass())) {
                nodes[i] = object;
            } else if (NodeInfo.class.isAssignableFrom(object.getClass())) {
                nodes[i] = NodeOverNodeInfo.wrap((NodeInfo) object);
            } else {
                nodes[i] = object.toString();
            }
        }
        return nodes;
    }
}
