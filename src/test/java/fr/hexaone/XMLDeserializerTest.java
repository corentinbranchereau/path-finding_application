package fr.hexaone;

import fr.hexaone.model.Carte;
import fr.hexaone.model.Depot;
import fr.hexaone.model.Intersection;
import fr.hexaone.model.Planning;
import fr.hexaone.utils.XMLDeserializer;
import fr.hexaone.utils.XMLFileOpener;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Unit test for simple App.
 */
public class XMLDeserializerTest {

    @Test
    public void shouldLoadCarte() {
        try {
            Carte carte = new Carte();
            XMLFileOpener xmlFileOpener = XMLFileOpener.getInstance();
            Document xml = xmlFileOpener.open("./src/test/resources/smallMap.xml");
            XMLDeserializer.loadCarte(carte, xml);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void shouldLoadRequete() {
        try {
            Planning planning = new Planning();
            XMLFileOpener xmlFileOpener = XMLFileOpener.getInstance();
            Document xml = xmlFileOpener.open("./src/test/resources/requestsSmall2.xml");
            XMLDeserializer.loadRequete(planning, xml);
            // On s'assure de la valeur des attributs du planning
            Intersection depotTest = new Depot("2835339774", "8:0:0");
            assertEquals(depotTest, planning.getDepot());
            assertEquals(2, planning.getRequetes().size());
        } catch (Exception e) {
            fail();
        }
    }
}
