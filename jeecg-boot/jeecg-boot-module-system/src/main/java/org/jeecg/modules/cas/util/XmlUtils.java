package org.jeecg.modules.cas.util;


import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.w3c.dom.Document;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import lombok.extern.slf4j.Slf4j;

/**
 * 解析cas,ST验证后的xml
 * @author: jeecg-boot
 */
@Slf4j
public final class XmlUtils {

    /**
     * Creates a new namespace-aware DOM document object by parsing the given XML.
     *
     * @param xml XML content.
     *
     * @return DOM document.
     */
    public static Document newDocument(final String xml) {
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        final Map<String, Boolean> features = new HashMap(5);
        features.put(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        features.put("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        for (final Map.Entry<String, Boolean> entry : features.entrySet()) {
            try {
                factory.setFeature(entry.getKey(), entry.getValue());
            } catch (ParserConfigurationException e) {
                log.warn("Failed setting XML feature {}: {}", entry.getKey(), e);
            }
        }
        factory.setNamespaceAware(true);
        try {
            return factory.newDocumentBuilder().parse(new InputSource(new StringReader(xml)));
        } catch (Exception e) {
            throw new RuntimeException("XML parsing error: " + e);
        }
    }

    /**
     * Get an instance of an XML reader from the XMLReaderFactory.
     *
     * @return the XMLReader.
     */
    public static XMLReader getXmlReader() {
        try {
            final XMLReader reader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
            reader.setFeature("http://xml.org/sax/features/namespaces", true);
            reader.setFeature("http://xml.org/sax/features/namespace-prefixes", false);
            reader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            return reader;
        } catch (final Exception e) {
            throw new RuntimeException("Unable to create XMLReader", e);
        }
    }


    /**
     * Retrieve the text for a group of elements. Each text element is an entry
     * in a list.
     * <p>This method is currently optimized for the use case of two elements in a list.
     *
     * @param xmlAsString the xml response
     * @param element     the element to look for
     * @return the list of text from the elements.
     */
    public static List<String> getTextForElements(final String xmlAsString, final String element) {
        final List<String> elements = new ArrayList<String>(2);
        final XMLReader reader = getXmlReader();

        final DefaultHandler handler = new DefaultHandler() {

            private boolean foundElement = false;

            private StringBuilder buffer = new StringBuilder();

            @Override
            public void startElement(final String uri, final String localName, final String qName,
                                     final Attributes attributes) throws SAXException {
                if (localName.equals(element)) {
                    this.foundElement = true;
                }
            }

            @Override
            public void endElement(final String uri, final String localName, final String qName) throws SAXException {
                if (localName.equals(element)) {
                    this.foundElement = false;
                    elements.add(this.buffer.toString());
                    this.buffer = new StringBuilder();
                }
            }

            @Override
            public void characters(char[] ch, int start, int length) throws SAXException {
                if (this.foundElement) {
                    this.buffer.append(ch, start, length);
                }
            }
        };

        reader.setContentHandler(handler);
        reader.setErrorHandler(handler);

        try {
            reader.parse(new InputSource(new StringReader(xmlAsString)));
        } catch (final Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }

        return elements;
    }

    /**
     * Retrieve the text for a specific element (when we know there is only
     * one).
     *
     * @param xmlAsString the xml response
     * @param element     the element to look for
     * @return the text value of the element.
     */
    public static String getTextForElement(final String xmlAsString, final String element) {
        final XMLReader reader = getXmlReader();
        final StringBuilder builder = new StringBuilder();

        final DefaultHandler handler = new DefaultHandler() {

            private boolean foundElement = false;

            @Override
            public void startElement(final String uri, final String localName, final String qName,
                                     final Attributes attributes) throws SAXException {
                if (localName.equals(element)) {
                    this.foundElement = true;
                }
            }

            @Override
            public void endElement(final String uri, final String localName, final String qName) throws SAXException {
                if (localName.equals(element)) {
                    this.foundElement = false;
                }
            }

            @Override
            public void characters(char[] ch, int start, int length) throws SAXException {
                if (this.foundElement) {
                    builder.append(ch, start, length);
                }
            }
        };

        reader.setContentHandler(handler);
        reader.setErrorHandler(handler);

        try {
            reader.parse(new InputSource(new StringReader(xmlAsString)));
        } catch (final Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }

        return builder.toString();
    }
    
    
    public static Map<String, Object> extractCustomAttributes(final String xml) {
        final SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setNamespaceAware(true);
        spf.setValidating(false);
        try {
            final SAXParser saxParser = spf.newSAXParser();
            final XMLReader xmlReader = saxParser.getXMLReader();
            final CustomAttributeHandler handler = new CustomAttributeHandler();
            xmlReader.setContentHandler(handler);
            xmlReader.parse(new InputSource(new StringReader(xml)));
            return handler.getAttributes();
        } catch (final Exception e) {
        	log.error(e.getMessage(), e);
            return Collections.emptyMap();
        }
    }
    
    private static class CustomAttributeHandler extends DefaultHandler {

        private Map<String, Object> attributes;

        private boolean foundAttributes;

        private String currentAttribute;

        private StringBuilder value;

        @Override
        public void startDocument() throws SAXException {
            this.attributes = new HashMap(5);
        }

        @Override
        public void startElement(final String namespaceURI, final String localName, final String qName,
                final Attributes attributes) throws SAXException {
            if ("attributes".equals(localName)) {
                this.foundAttributes = true;
            } else if (this.foundAttributes) {
                this.value = new StringBuilder();
                this.currentAttribute = localName;
            }
        }

        @Override
        public void characters(final char[] chars, final int start, final int length) throws SAXException {
            if (this.currentAttribute != null) {
                value.append(chars, start, length);
            }
        }

        @Override
        public void endElement(final String namespaceURI, final String localName, final String qName)
                throws SAXException {
            if ("attributes".equals(localName)) {
                this.foundAttributes = false;
                this.currentAttribute = null;
            } else if (this.foundAttributes) {
                final Object o = this.attributes.get(this.currentAttribute);

                if (o == null) {
                    this.attributes.put(this.currentAttribute, this.value.toString());
                } else {
                    final List<Object> items;
                    if (o instanceof List) {
                        items = (List<Object>) o;
                    } else {
                        items = new LinkedList<Object>();
                        items.add(o);
                        this.attributes.put(this.currentAttribute, items);
                    }
                    items.add(this.value.toString());
                }
            }
        }

        public Map<String, Object> getAttributes() {
            return this.attributes;
        }
    }
    
    
    public static void main(String[] args) {
		String result = "<cas:serviceResponse xmlns:cas='http://www.yale.edu/tp/cas'>\r\n" + 
				"    <cas:authenticationSuccess>\r\n" + 
				"        <cas:user>admin</cas:user>\r\n" + 
				"        <cas:attributes>\r\n" + 
				"            <cas:credentialType>UsernamePasswordCredential</cas:credentialType>\r\n" + 
				"            <cas:isFromNewLogin>true</cas:isFromNewLogin>\r\n" + 
				"            <cas:authenticationDate>2019-08-01T19:33:21.527+08:00[Asia/Shanghai]</cas:authenticationDate>\r\n" + 
				"            <cas:authenticationMethod>RestAuthenticationHandler</cas:authenticationMethod>\r\n" + 
				"            <cas:successfulAuthenticationHandlers>RestAuthenticationHandler</cas:successfulAuthenticationHandlers>\r\n" + 
				"            <cas:longTermAuthenticationRequestTokenUsed>false</cas:longTermAuthenticationRequestTokenUsed>\r\n" + 
				"        </cas:attributes>\r\n" + 
				"    </cas:authenticationSuccess>\r\n" + 
				"</cas:serviceResponse>";
		
		String errorRes = "<cas:serviceResponse xmlns:cas='http://www.yale.edu/tp/cas'>\r\n" + 
				"    <cas:authenticationFailure code=\"INVALID_TICKET\">未能够识别出目标 &#39;ST-5-1g-9cNES6KXNRwq-GuRET103sm0-DESKTOP-VKLS8B3&#39;票根</cas:authenticationFailure>\r\n" + 
				"</cas:serviceResponse>";
		
		String error = XmlUtils.getTextForElement(errorRes, "authenticationFailure");
		System.out.println("------"+error);
		
		String error2 = XmlUtils.getTextForElement(result, "authenticationFailure");
		System.out.println("------"+error2);
		String principal = XmlUtils.getTextForElement(result, "user");
		System.out.println("---principal---"+principal);
		Map<String, Object> attributes = XmlUtils.extractCustomAttributes(result);
		System.out.println("---attributes---"+attributes);
	}
}
