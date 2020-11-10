package com.github.liulus.yurt.convention.util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author liulu
 * @version V1.0
 * @since 2020/11/9
 */
public abstract class FormatUtils {

    private FormatUtils() {
    }

    public static String formatMessage(String messagePattern, Object... args) {
        return formatMessage(messagePattern, "{}", args);
    }

    /**
     * 消息模板格式化, 将 token 按参数顺序替换
     * eg -> messagePattern: hi {}. My name is {}.   args: ['Alice', 'Bob']
     * will return the string "Hi Alice. My name is Bob.".
     *
     * @param token          the token will be replaced
     * @param messagePattern the message template
     * @param args           replace args
     * @return String
     */
    public static String formatMessage(String messagePattern, String token, Object... args) {
        if (messagePattern == null || messagePattern.isEmpty()) {
            return "";
        }
        if (args == null || args.length == 0) {
            return messagePattern;
        }
        int msgLen = messagePattern.length();
        StringBuilder result = new StringBuilder(msgLen + 100);
        int offset = 0;
        for (Object arg : args) {
            int tokenIdx = messagePattern.indexOf(token, offset);
            if (tokenIdx < 0) {
                break;
            }
            result.append(messagePattern, offset, tokenIdx).append(arg);
            offset = tokenIdx + token.length();
        }

        if (offset < msgLen) {
            result.append(messagePattern, offset, msgLen);
        }
        return result.toString();
    }

    private static final DocumentBuilderFactory DOCUMENT_BUILDER_FACTORY = DocumentBuilderFactory.newInstance();

    private static Transformer transformer;

    static {
        try {
            transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, StandardCharsets.UTF_8.name());
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        } catch (TransformerConfigurationException e) {
            //
        }
    }

    public static String marshalXml(Map<String, ?> obj) {
        return marshalXml(obj, false, false, "", "");
    }

    public static String marshalXml(Map<String, ?> obj, boolean wrapCdata) {
        return marshalXml(obj, wrapCdata, false, "", "");
    }

    public static String marshalXml(Map<String, ?> obj, boolean wrapCdata, boolean nodeUsePrefix,
                                    String namespace, String namespacePrefix) {
        Node node = marshalToNode(obj, wrapCdata, nodeUsePrefix, namespace, namespacePrefix);
        if (node == null) {
            return "";
        }
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        // 没有额外节点, 直接输出
        if (obj.size() == 1) {
            try {
                transformer.transform(new DOMSource(node), result);
            } catch (TransformerException e) {
                //
                return "";
            }
        } else {
            // 取出手动添加的ROOT节点下的子节点
            NodeList childNodes = node.getChildNodes().item(0).getChildNodes();
            int len = childNodes.getLength();
            for (int i = 0; i < len; i++) {
                try {
                    transformer.transform(new DOMSource(childNodes.item(i)), result);
                } catch (TransformerException e) {
                    //
                    return "";
                }
            }
        }
        return writer.toString();
    }


    public static Node marshalToNode(Map<String, ?> obj, boolean wrapCdata, boolean nodeUsePrefix,
                                     String namespace, String namespacePrefix) {
        if (obj == null) {
            return null;
        }
        Document document;
        try {
            document = DOCUMENT_BUILDER_FACTORY.newDocumentBuilder().newDocument();
        } catch (ParserConfigurationException e) {
            //
            return null;
        }
        // 大于1个节点需要手动添加root节点
        boolean multiNode = obj.size() > 1;
        String rootName = multiNode ? "ROOT" : obj.keySet().iterator().next();
        boolean hasNamespace = namespace != null && namespace.length() > 0;
        Element rootEle = hasNamespace ? document.createElementNS(namespace, rootName)
                : document.createElement(rootName);
        if (hasNamespace) {
            rootEle.setPrefix(namespacePrefix);
        }
        Node root = document.appendChild(rootEle);
        appendNodes(root, multiNode ? obj : obj.values(), wrapCdata, nodeUsePrefix, namespace, namespacePrefix);
        return document;
    }

    protected static void appendNodes(Node node, Object children, boolean wrapCdata, boolean nodeUsePrefix,
                                      String namespace, String namespacePrefix) {
        if (children == null) {
            return;
        }
        Document document = node instanceof Document ? (Document) node : node.getOwnerDocument();
        // 分别处理节点
        if (children instanceof byte[]) {
            String text = new String((byte[]) children, StandardCharsets.UTF_8);
            appendTextNode(node, text, wrapCdata);
            return;
        }
        if (children instanceof String) {
            String text = String.valueOf(children);
            appendTextNode(node, text, wrapCdata);
            return;
        }
        if (children instanceof Map) {
            //noinspection unchecked
            Map<String, ?> map = (Map<String, ?>) children;
            for (Map.Entry<String, ?> entry : map.entrySet()) {
                Element ele = nodeUsePrefix ? document.createElementNS(namespace, entry.getKey())
                        : document.createElement(entry.getKey());
                Node element = node.appendChild(ele);
                if (nodeUsePrefix) {
                    element.setPrefix(namespacePrefix);
                }
                appendNodes(element, entry.getValue(), wrapCdata, nodeUsePrefix, namespace, namespacePrefix);
            }
            return;
        }
        if (children instanceof Collection) {
            Collection<?> collection = (Collection<?>) children;
            collection.forEach(o -> appendNodes(node, o, wrapCdata, nodeUsePrefix, namespace, namespacePrefix));
        }
    }

    private static void appendTextNode(Node node, String text, boolean wrapCdata) {
        Document document = node instanceof Document ? (Document) node : node.getOwnerDocument();
        if (wrapCdata) {
            node.appendChild(document.createCDATASection(text));
            return;
        }
        node.setTextContent(text);
    }

    public static Map<String, Object> unmarshalXml(String xmlText) {
        if (xmlText == null) {
            return Collections.emptyMap();
        }
        return unmarshalXml(xmlText.getBytes());
    }

    public static Map<String, Object> unmarshalXml(byte[] xmlText) {
        Document document = null;
        try {
            document = DOCUMENT_BUILDER_FACTORY.newDocumentBuilder().parse(new ByteArrayInputStream(xmlText));
        } catch (Exception e) {
            //
            return Collections.emptyMap();
        }
        Object res = unmarshalNode(document);
        if (res == null) {
            return Collections.emptyMap();
        }
        if (res instanceof Map) {
            //noinspection unchecked
            return ((Map<String, Object>) res);
        }
        return Collections.emptyMap();
    }

    private static Object unmarshalNode(Node node) {
        if (isTextNode(node)) {
            String originText = node.getTextContent();
            return originText == null ? null : originText.trim();
        }
        if (node.hasChildNodes()) {
            NodeList childNodes = node.getChildNodes();
            int len = childNodes.getLength();
            Map<String, Object> mapResult = new LinkedHashMap<>();
            for (int i = 0; i < len; i++) {
                Node item = childNodes.item(i);
                String nodeName = getNodeName(item);
                Object val = unmarshalNode(item);
                if (val == null || "".equals(val) || Objects.equals("\n", val)) {
                    continue;
                }
                mapResult.compute(nodeName, (key, oldValue) -> mergeValue(oldValue, val));
            }
            return mapResult;
        }
        return null;
    }

    private static boolean isTextNode(Node node) {
        return node instanceof Text ||
                (node.getChildNodes().getLength() == 1 && node.getFirstChild() instanceof Text);
    }

    private static String getNodeName(Node node) {
        String nodeName = node.getNodeName();
        int index = nodeName.indexOf(":");
        return index < 0 ? nodeName : nodeName.substring(0, index);
    }

    private static Object mergeValue(Object oldValue, Object newValue) {
        if (oldValue == null) {
            return newValue;
        }
        if (oldValue instanceof List) {
            //noinspection unchecked
            ((List<Object>) oldValue).add(newValue);
            return oldValue;
        }
        List<Object> list = new ArrayList<>();
        list.add(oldValue);
        list.add(newValue);
        return list;
    }

}
