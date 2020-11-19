package fr.hexaone;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import org.junit.Test;
Plaimport org.w3c.dom.Document;

import fr.hexaone.model.Carte;
import fr.hexaone.model.Planning;
import fr.hexaone.utils.XMLDeserializer;
import fr.hexaone.utils.XMLFileOpener;

/**
 * Unit test for simple App.
 */
public class XMLDeserializerTest {

    @Test
    public void shouldLoadCarte() {
        try {
            Carte carte = new Carte();
            XMLFileOpener xmlFileOpener = new XMLFileOpener();
            Document xml = xmlFileOpener.open("path/to/xml");
            XMLDeserializer.loadCarte(carte, xml);
        } catch (Exception e) {
            assertTrue(false);
        }
    }

    @Test
    public void shouldLoadRequete() {
        try {
            Planning planning = new Planning();
            XMLFileOpener xmlFileOpener = new XMLFileOpener();
            Document xml = xmlFileOpener.open("path/to/xml");
            XMLDeserializer.loadRequete(planning, xml);
        } catch (Exception e) {
            assertTrue(false);
        }
    }
}
