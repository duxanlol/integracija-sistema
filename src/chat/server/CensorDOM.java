package chat.server;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.SAXException;
public class CensorDOM {
	
	public static String xmlPath = "C:\\xml\\reci.xml";
	private Document dom;
	private Set<String> censoredWords;
	
	public CensorDOM() {
		dom = loadDocument();
		censoredWords = new HashSet<String>();
		parseWordsFromXML();

	}
	
	public Document loadDocument() {
        // Get a factory
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            // Get a new instance of document builder
            DocumentBuilder builder = factory.newDocumentBuilder();
            // Parse the file and return DOM representation
            return builder.parse(xmlPath);
        // Print any errors
        } catch (ParserConfigurationException ex) {
            ex.printStackTrace();
            return null;
        } catch (SAXException ex) {
            ex.printStackTrace();
            return null;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
	
	public synchronized void addRec(String rec, String nadimak) {
        Element root = dom.getDocumentElement();
        Element recElement = dom.createElement("rec");
        recElement.setAttribute("nadimak", nadimak);
        recElement.appendChild(dom.createTextNode(rec.toLowerCase()));
        root.appendChild(recElement);
        censoredWords.add(rec);
        saveDocument();
	}
	
	public synchronized boolean removeRec(String rec, String nadimak) {
        Element root = dom.getDocumentElement();
        NodeList nl = root.getElementsByTagName("rec");
        for (int i = 0; i < nl.getLength(); i++) {
        	if (((Element) nl.item(i)).getAttribute("nadimak").equalsIgnoreCase(nadimak) &&
        			((Element) nl.item(i)).getTextContent().equalsIgnoreCase(rec)) {
        		root.removeChild(((Element) nl.item(i)));
        		censoredWords.remove(rec);
        		saveDocument();
        		return true;
        	}
        }
        //Ne cuvamo dokument kad nema promene.
        return false;
	}
	
	public Set<String> getWords(){
		return censoredWords;
	}
	
	private void parseWordsFromXML() {
		Element root = dom.getDocumentElement();
	    NodeList nl = root.getElementsByTagName("rec");
	    for (int i = 0; i < nl.getLength(); i++) {
	    	censoredWords.add(((Element) nl.item(i)).getTextContent().toLowerCase());
	    }
	}
	
    private void saveDocument() { //Save cuva kao UTF-16 a cita kao UTF-8 i tu puca. 
        try {

            // Get a registry
            DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();

            // Get a new instance of loader/saver
            DOMImplementationLS impl = (DOMImplementationLS) registry.getDOMImplementation("LS");
            LSSerializer writer = impl.createLSSerializer();

            // Write the whole document
            String text = writer.writeToString(dom);;
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(xmlPath), StandardCharsets.UTF_16BE);
            outputStreamWriter.write(text);
            outputStreamWriter.close();
        // Print any errors
        } catch (ClassCastException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
	
}
