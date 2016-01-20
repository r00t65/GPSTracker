package de.hof_university.gpstracker.Controller.map.converter;

import org.w3c.dom.Document;

import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by alex on 20.01.16.
 * <p>
 * Redesign by alex on 20.01.16
 */
public interface KMLConverterInterface {
    public Document convert() throws ParserConfigurationException;
}
