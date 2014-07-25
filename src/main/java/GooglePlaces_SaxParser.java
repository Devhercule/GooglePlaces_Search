import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
public class GooglePlaces_SaxParser extends DefaultHandler {

	String toparse;
	String tmpValue;
	List references ;
	List adr ;
	boolean isDetail;
	
    public GooglePlaces_SaxParser(String toparse, boolean isDetail) {
    	
        this.toparse = toparse;
        references= new ArrayList();
        adr= new ArrayList();
        this.isDetail = isDetail;
        
        
        parseDocument();
        
    }
    private void parseDocument() {
        // parse
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
        	SAXParser saxParser;
            saxParser = factory.newSAXParser();
            DefaultHandler handler = new DefaultHandler() {
                public void startElement(String uri, String localName, String element, Attributes attributes) throws SAXException {

                	
                }
                public void endElement(String uri, String localName, String element)throws SAXException {

                	if(!isDetail)
                	{
            	        if (element.equals("reference")) {
            	           references.add(tmpValue);
            	        }
                	}
                	else
                	{
                		 if (element.equals("formatted_phone_number")) {
                			  adr.add(tmpValue);
                		 }
                		if(element.equals("formatted_address"))
                			  adr.add(tmpValue);
                	}
                    
                }           
                public void characters(char ch[], int start, int length) throws SAXException {
                	 tmpValue = new String(ch, start, length);
                }               
             };      
            saxParser.parse(new InputSource(new StringReader(toparse)), handler);
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
               
                e.printStackTrace();
            }catch (ParserConfigurationException e) {
                e.printStackTrace();    
            }
    }
  
   
  
}
