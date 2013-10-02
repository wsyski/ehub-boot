package com.axiell.ehub.lms.arena.index;

import net.sf.saxon.Configuration;
import net.sf.saxon.dom.DocumentWrapper;
import net.sf.saxon.expr.JPConverter;
import net.sf.saxon.functions.FunctionLibraryList;
import net.sf.saxon.functions.JavaExtensionLibrary;
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

public class XQueryFieldEvaluator extends XMLFieldEvaluatorBase {
    private Configuration config = Configuration.makeConfiguration(null, null);
    private JavaExtensionLibrary jel = new JavaExtensionLibrary(config);

    public XQueryFieldEvaluator() {
        FunctionLibraryList fll = new FunctionLibraryList();
        fll.addFunctionLibrary(jel);
        config.setExtensionBinder("java", fll);
    }

    private static ValueRepresentation convertJavaToSaxon(final Object object) throws XPathException {
        Object objectNotNull = object == null ? StringUtils.EMPTY : object;
        return JPConverter.allocate(objectNotNull.getClass(), null).convert(objectNotNull, null);
    }

    protected final Object[] evaluateExpression(final String query, final Node contextNode, final Map<String, Object> variables, final String defaultNamespace,
                                                final Map<String, String> namespaces) throws XPathException {
        Map<QName, Object> qualifiedVariables = new HashMap<>();
        if (variables != null) {
            for (Map.Entry<String, Object> entry : variables.entrySet()) {
                qualifiedVariables.put(QName.valueOf(entry.getKey()), entry.getValue());
            }
        }
        StaticQueryContext sqc = new StaticQueryContext(config);
        for (Map.Entry<QName, Object> entry : qualifiedVariables.entrySet()) {
            sqc.declareGlobalVariable(StructuredQName.fromClarkName(entry.getKey().toString()), SequenceType.SINGLE_ITEM,
                    convertJavaToSaxon(entry.getValue()), false);
        }
        DynamicQueryContext dqc = new DynamicQueryContext(config);
        XQueryExpression e = sqc.compileQuery(getNamespaceDeclaration(defaultNamespace, namespaces) + query);
        dqc.setContextItem(new DocumentWrapper(contextNode, "", config));
        List<Object> objectList = e.evaluate(dqc);
        return nodeInfos2Nodes(objectList.toArray());
    }

    private static String getNamespaceDeclaration(final String defaultNamespace, final Map<String, String> namespaces) {
        StringBuffer sb = new StringBuffer();
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
}
