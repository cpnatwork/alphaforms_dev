/**************************************************************************
 * alpha-Flow
 * ==============================================
 * Copyright (C) 2009-2011 by Christoph P. Neumann
 * (http://www.chr15t0ph.de)
 **************************************************************************
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 **************************************************************************
 * $Id$
 *************************************************************************/
package alpha.forms.util.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * The Class XMLDocumentSection.
 */
public class XMLDocumentSection {

	/** The xml doc. */
	private Document xmlDoc;

	/** The parent node. */
	private Node parentNode;

	/** The attributes. */
	private Map<String, String> attributes;

	/**
	 * Instantiates a new xML document section.
	 * 
	 * @param node
	 *            the node
	 * @param xmlDoc
	 *            the xml doc
	 */
	public XMLDocumentSection(Node node, Document xmlDoc) {
		this.parentNode = node;
		this.xmlDoc = xmlDoc;
		System.out
				.println("New XMLDocumentSection(" + node.getNodeName() + ")");

	}

	/**
	 * Gets the attributes.
	 * 
	 * @return the attributes
	 */
	public Map<String, String> getAttributes() {
		NamedNodeMap attrs = parentNode.getAttributes();
		Map<String, String> ret = new HashMap<String, String>();
		for (int i = 0; i < attrs.getLength(); i++) {
			Node attr = attrs.item(i);
			ret.put(attr.getNodeName(), attr.getNodeValue());
			System.out.println("Attribute(" + attr.getNodeName() + " -> "
					+ attr.getNodeValue() + ")");
		}
		return ret;
	}

	/**
	 * Gets the attribute.
	 * 
	 * @param name
	 *            the name
	 * @return the attribute
	 */
	public String getAttribute(String name) {
		if (attributes == null)
			attributes = getAttributes();
		return attributes.get(name);
	}

	/**
	 * Gets the children.
	 * 
	 * @return the children
	 */
	public List<Node> getChildren() {
		NodeList childNodes = parentNode.getChildNodes();
		List<Node> ret = new ArrayList<Node>();
		for (int i = 0; i < childNodes.getLength(); i++) {
			ret.add(childNodes.item(i));
		}
		return ret;
	}

	/**
	 * Gets the section name.
	 * 
	 * @return the section name
	 */
	public String getSectionName() {
		return parentNode.getNodeName();
	}

	/**
	 * Gets the nodes.
	 * 
	 * @param xPathExpression
	 *            the x path expression
	 * @return the nodes
	 */
	public List<Node> getNodes(String xPathExpression) {
		XPath xpath = XPathFactory.newInstance().newXPath();
		List<Node> ret = new ArrayList<Node>();
		try {
			NodeList nodes = (NodeList) xpath.evaluate(xPathExpression,
					parentNode, XPathConstants.NODESET);
			for (int i = 0; i < nodes.getLength(); i++) {
				ret.add(nodes.item(i));
			}
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return ret;

	}

	/**
	 * Gets the single node.
	 * 
	 * @param xPathExpression
	 *            the x path expression
	 * @return the single node
	 */
	public Node getSingleNode(String xPathExpression) {
		XPath xpath = XPathFactory.newInstance().newXPath();
		Node node = null;
		try {
			node = (Node) xpath.evaluate(xPathExpression, parentNode,
					XPathConstants.NODE);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return node;
	}

	/**
	 * Gets the node value.
	 * 
	 * @param xPathExpression
	 *            the x path expression
	 * @return the node value
	 */
	public String getNodeValue(String xPathExpression) {
		Node node = getSingleNode(xPathExpression);
		if (node != null) {
			return node.getTextContent();
		} else {
			return null;
		}
	}

	/**
	 * Gets the single section.
	 * 
	 * @param xPathExpression
	 *            the x path expression
	 * @return the single section
	 */
	public XMLDocumentSection getSingleSection(String xPathExpression) {
		Node node = getSingleNode(xPathExpression);
		if (node != null) {
			return new XMLDocumentSection(node, xmlDoc);
		} else {
			return null;
		}
	}

	/**
	 * Gets the document sections.
	 * 
	 * @param xPathExpression
	 *            the x path expression
	 * @return the document sections
	 */
	public List<XMLDocumentSection> getDocumentSections(String xPathExpression) {
		List<Node> nodes = getNodes(xPathExpression);
		List<XMLDocumentSection> sectionList = new ArrayList<XMLDocumentSection>();
		for (Node n : nodes) {
			sectionList.add(new XMLDocumentSection(n, xmlDoc));
		}
		return sectionList;
	}

	/**
	 * Gets the text content.
	 * 
	 * @return the text content
	 */
	public String getTextContent() {
		return parentNode.getTextContent();
	}

	/**
	 * Gets the cDATA content.
	 * 
	 * @return the cDATA content
	 */
	public String getCDATAContent() {
		for (Node n : this.getChildren()) {
			if (n.getNodeType() == Node.CDATA_SECTION_NODE) {
				CDATASection cdata = (CDATASection) n;
				return cdata.getData();
			}
		}
		return null;
	}

}
