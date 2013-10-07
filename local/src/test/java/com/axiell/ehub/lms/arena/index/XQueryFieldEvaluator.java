package com.axiell.ehub.lms.arena.index;

import net.sf.saxon.Configuration;
import net.sf.saxon.dom.DocumentWrapper;
import net.sf.saxon.dom.NodeOverNodeInfo;
import net.sf.saxon.expr.JPConverter;
import net.sf.saxon.functions.FunctionLibraryList;
import net.sf.saxon.functions.JavaExtensionLibrary;
import net.sf.saxon.om.NodeInfo;
import net.sf.saxon.om.StructuredQName;
import net.sf.saxon.om.ValueRepresentation;
import net.sf.saxon.query.DynamicQueryContext;
import net.sf.saxon.query.StaticQueryContext;
import net.sf.saxon.query.XQueryExpression;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.value.SequenceType;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Node;

import javax.xml.namespace.QName;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XQueryFieldEvaluator  {
    private Configuration config = Configuration.makeConfiguration(null, null);

    public XQueryFieldEvaluator() {
        FunctionLibraryList fll = new FunctionLibraryList();
        JavaExtensionLibrary jel = new JavaExtensionLibrary(config);
        fll.addFunctionLibrary(jel);
        config.setExtensionBinder("java", fll);
    }

    protected final Object[] evaluateExpression(final String query, final Node contextNode, final Map<String, Object> variables, final String defaultNamespace,
                                                final Map<String, String> namespaces) throws XPathException {
        StaticQueryContext sqc = getStaticQueryContext(variables);
        DynamicQueryContext dqc = getDynamicQueryContext(contextNode);
        XQueryExpression e = sqc.compileQuery(getNamespaceDeclaration(defaultNamespace, namespaces) + query);
        List objectList = e.evaluate(dqc);
        return nodeInfos2Nodes(objectList.toArray());
    }

    private DynamicQueryContext getDynamicQueryContext(final Node contextNode) {
        DynamicQueryContext dqc = new DynamicQueryContext(config);
        dqc.setContextItem(new DocumentWrapper(contextNode, "", config));
        return dqc;
    }

    private StaticQueryContext getStaticQueryContext(Map<String, Object> variables) throws XPathException {
        Map<QName, Object> qualifiedVariables = evaluateQualifiedVariables(variables);
        StaticQueryContext sqc = new StaticQueryContext(config);
        for (Map.Entry<QName, Object> entry : qualifiedVariables.entrySet()) {
            sqc.declareGlobalVariable(StructuredQName.fromClarkName(entry.getKey().toString()), SequenceType.SINGLE_ITEM,
                    convertJavaToSaxon(entry.getValue()), false);
        }
        return sqc;
    }

    private Map<QName, Object> evaluateQualifiedVariables(final Map<String, Object> variables) {
        Map<QName, Object> qualifiedVariables = new HashMap<>();
        if (variables != null) {
            for (Map.Entry<String, Object> entry : variables.entrySet()) {
                qualifiedVariables.put(QName.valueOf(entry.getKey()), entry.getValue());
            }
        }
        return qualifiedVariables;
    }

    private static ValueRepresentation convertJavaToSaxon(final Object object) throws XPathException {
        Object objectNotNull = object == null ? StringUtils.EMPTY : object;
        return JPConverter.allocate(objectNotNull.getClass(), null).convert(objectNotNull, null);
    }

    private static String getNamespaceDeclaration(final String defaultNamespace, final Map<String, String> namespaces) {
        StringBuilder sb = new StringBuilder();
        if (defaultNamespace != null) {
            sb.append("declare default element namespace \"");
            sb.append(defaultNamespace);
            sb.append("\";");
        }
        if (namespaces != null) {
            for (Map.Entry<String, String> entry : namespaces.entrySet()) {
                sb.append("declare namespace ");
                sb.append(entry.getKey());
                sb.append("=\"");
                sb.append(namespaces.get(entry.getKey()));
                sb.append("\";");
            }
        }
        return sb.toString();
    }

    static Object[] nodeInfos2Nodes(final Object[] objects) {
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
