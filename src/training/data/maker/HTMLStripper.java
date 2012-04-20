/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package training.data.maker;

import java.util.Iterator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author raj
 */
public class HTMLStripper {
    
    public static String stripHTML(String code) {
        
        Document doc = Jsoup.parse(code);
        
        doc = replaceAllTags(doc, "script");
        doc = replaceAllTags(doc, "style");
                
        code = doc.text();
        
        return code;
    }
    
    private static Document replaceAllTags(Document doc, String tag) {
        
        Elements scriptTags  = doc.getElementsByTag(tag);
        
        Iterator <Element> elementsIterator = scriptTags.iterator();
        Element current=null;
        Element replacement=null;;
        while (elementsIterator.hasNext()) {
            
            replacement = doc.createElement("p");
            
            current = elementsIterator.next();
            current.text("");
            current.replaceWith(replacement);
            
        }
        return doc;
    }
    
}
