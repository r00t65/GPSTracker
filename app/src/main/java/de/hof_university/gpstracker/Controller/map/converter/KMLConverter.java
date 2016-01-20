package de.hof_university.gpstracker.Controller.map.converter;

import android.support.annotation.NonNull;
import de.hof_university.gpstracker.Model.track.Track;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by alex on 20.01.16.
 * <p>
 * Redesign by alex on 20.01.16
 */
public class KMLConverter implements KMLConverterInterface {

    private final Track track;

    private final String tagRoot = "kml";
    private final String tagDoc = "Document";
    private final String tagName = "name";
    private final String tagDes = "description";
    private final String tagStyle = "Style";

    private final String tagDesValue = "GPSTracker MC FH Hof";

    public KMLConverter(@NonNull final Track track){
        this.track = track;
    }

    /**
     * http://developers.google.com/kml/documentation/kml_tut?hl=en
     * @return
     * @throws ParserConfigurationException
     */
    @Override
    public Document convert() throws ParserConfigurationException {
        final DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        final DocumentBuilder documentBuilder = builderFactory.newDocumentBuilder();

        final Document doc = documentBuilder.newDocument();

        Element rootElement = this.createElementNS(doc,tagRoot);

        doc.appendChild(rootElement);
        //--------------------------------------------------------------------------------------------------------
        //--------------------------------------------------------------------------------------------------------

        Element Document = doc.createElement(tagDoc);

        rootElement.appendChild(Document);

        Element name = this.createElementWithTextElement(doc,tagName,this.track.getName());
        Element des = this.createElementWithTextElement(doc,tagDes,this.track.getName());

        //--------------------------------------------------------------------------------------------------------
        final Element style = this.addAttribute(doc,this.createElement(doc,tagStyle),"id","");

        final Element linestyle = this.createElement(doc,"LineStyle");
        Element color = this.createElementWithTextElement(doc,"color","7f00ffff");
        final Element width = this.createElementWithTextElement(doc,"width","4");
        linestyle.appendChild(color);linestyle.appendChild(width);

        final Element polystyle = this.createElement(doc,"PolyStyle");
        color = this.createElementWithTextElement(doc,"color","7f00ff00");
        polystyle.appendChild(color);

        style.appendChild(linestyle);style.appendChild(polystyle);
        //--------------------------------------------------------------------------------------------------------
        final Element placemark = this.createElement(doc,"Placemark");

        final Element styleurl = this.createElementWithTextElement(doc,"styleUrl","#yellowGreenPoly");

        //--------------------------------------------------------------------------------------------------------
        //--------------------------------------------------------------------------------------------------------

        return doc;
    }

    private Element createElementWithTextElement(@NonNull final Document document,@NonNull final String elementName,@NonNull final String text){
        final Element element = document.createElement(elementName);
        element.appendChild(document.createTextNode(text));
        return element;
    }

    private Element createElement(@NonNull final Document document,@NonNull final String elementName){
        return document.createElement(elementName);
    }

    private Element createElementNS(@NonNull final Document document,@NonNull final String elementName){
        return document.createElementNS("http://www.opengis.net/kml/2.2",elementName);
    }

    private Element addAttribute(@NonNull final Document document,@NonNull final Element element,@NonNull final String attName,@NonNull final String attValue){
        Attr attr = document.createAttribute(attName);attr.setValue(attValue);
        element.setAttributeNode(attr);
        return element;
    }
}
