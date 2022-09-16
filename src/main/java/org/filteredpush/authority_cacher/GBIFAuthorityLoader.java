package org.filteredpush.authority_cacher;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class GBIFAuthorityLoader implements AuthorityLoader {

	private static final Log logger = LogFactory.getLog(GBIFAuthorityLoader.class);

	private Map<String,String> values;
	
	private String targetURI;
	
	public GBIFAuthorityLoader() { 
		targetURI ="https://rs.gbif.org/vocabulary/gbif/rank.xml";
		values = new HashMap<String,String>();
		init();
	}
	
	public GBIFAuthorityLoader(String uriForVocabularyXML) { 
		targetURI = uriForVocabularyXML;
		values = new HashMap<String,String>();
		init();
	}
	
	protected void init() { 
		try {
			this.load();
		} catch (IOException | ParserConfigurationException | SAXException e) {
			logger.debug(e.getMessage(), e);
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		GBIFAuthorityLoader loader = new GBIFAuthorityLoader();
		try {
			loader.load();
		} catch (IOException | ParserConfigurationException | SAXException e) {
			logger.debug(e.getMessage(), e);
		}
		
		Iterator<String> i = loader.getValues().keySet().iterator();
		while(i.hasNext()) { 
			String key = i.next();
			logger.debug(key + ":" + loader.getValues().get(key));
		}
		
	}
	
	public Map<String,String> getValues() { 
		return values;
	}
	
	@Override
	public Map<String,String> getValuesMapCopy() { 
		return new HashMap<String,String>(values);
	}
	
	public void load() throws IOException, ParserConfigurationException, SAXException { 
		URL target = new URL(targetURI);
		InputStream file = target.openStream();
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		//an instance of builder to parse the specified xml file
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(file);
		doc.getDocumentElement().normalize();
		logger.debug(doc.getDocumentElement().getNodeName());
		logger.debug(doc.getDocumentElement().getNodeValue());
		logger.debug(doc.getNodeType());
		NodeList nodes = doc.getChildNodes();
		for (int i=0; i<nodes.getLength(); i++) { 
			Node node = nodes.item(i);
		 	logger.debug(node.getNodeName());
		 	logger.debug(node.getNodeValue());
		 	logger.debug(node.getNodeType());
		 	if (node.getNodeType()==Node.ELEMENT_NODE) { 
		 	NamedNodeMap nodeAttributes = node.getAttributes();
		 		for (int at=0; at<nodeAttributes.getLength(); at++) { 
		 			Node attribute = nodeAttributes.item(at);
		 			logger.debug(attribute.getLocalName());
		 			logger.debug(attribute.getNodeValue());
		 		}
		 	}
		 	NodeList children = node.getChildNodes();
		 	for (int j=0; j<children.getLength(); j++) { 
		 		Node cnode = children.item(j);
		 		logger.debug(cnode.getNodeName());
		 		logger.debug(cnode.getNodeValue());
			 	logger.debug(cnode.getNodeType());
			 	String identifier = "";
			 	if (cnode.getNodeType()==Node.ELEMENT_NODE) { 
			 		NamedNodeMap cnodeAttributes = cnode.getAttributes();
			 		for (int at=0; at<cnodeAttributes.getLength(); at++) { 
			 			Node attribute = cnodeAttributes.item(at);
			 			logger.debug(attribute.getNodeName());
			 			logger.debug(attribute.getNodeValue());
			 			if (attribute.getNodeName().equals("dc:identifier")) { 
			 				identifier = attribute.getNodeValue();
			 				values.put(identifier, identifier);
			 			}
			 		}
			 	}
			 	NodeList cchildren = cnode.getChildNodes();
			 	logger.debug(cchildren.getLength());
			 	for (int k=0; k<cchildren.getLength(); k++) { 
			 		Node ccnode = cchildren.item(k);
			 		logger.debug(ccnode.getNodeType());
			 		NodeList ccchildren = ccnode.getChildNodes();
			 		logger.debug(ccchildren.getLength());
			 		for (int l=0; l<cchildren.getLength(); l++) { 
			 			Node altnode = ccchildren.item(l);
			 			if (altnode!=null && altnode.getNodeType()==Node.ELEMENT_NODE) { 
			 				NamedNodeMap altnodeAttributes = altnode.getAttributes();
			 				for (int at=0; at<altnodeAttributes.getLength(); at++) { 
			 					Node attribute = altnodeAttributes.item(at);
			 					logger.debug(attribute.getNodeName());
			 					logger.debug(attribute.getNodeValue());
			 					if (attribute.getNodeName().equals("dc:title")) { 
			 						values.put(attribute.getNodeValue(), identifier);
			 					}
			 				}
			 			}
				 	}
			 	}
		 	}
		}
	}

}
