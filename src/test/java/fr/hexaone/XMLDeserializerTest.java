package fr.hexaone;

import fr.hexaone.model.Carte;
import fr.hexaone.model.Depot;
import fr.hexaone.model.Intersection;
import fr.hexaone.model.Planning;
import fr.hexaone.model.Segment;
import fr.hexaone.utils.XMLDeserializer;
import fr.hexaone.utils.XMLFileOpener;
import fr.hexaone.utils.exception.FileBadExtensionException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for simple App.
 */
public class XMLDeserializerTest {

    private Carte carte;
    private Planning planning;

    /**
     * Instancie une carte et une intersection avec une liste d'intersections vide
     */
    @BeforeEach
    void init(){
        carte = new Carte();
        planning = new Planning();
    }


    /**
     * Test le chargement d'une carte avec un fichier XML correct sans exception levée.
     */
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

    /**
     * Test le chargement d'une carte avec un fichier XML correct en vérifiant le nombre d'intersections
     */
    @Test
    public void shouldLoadCarteWithGoodSize() throws FileBadExtensionException, SAXException, IOException {
        Document xml = XMLFileOpener.getInstance().open("./src/test/resources/smallMap.xml");
        XMLDeserializer.loadCarte(carte,xml);
        assertEquals(308,carte.getIntersections().size());
    }

    /**
     * Test le chargement d'une carte avec un fichier XML correct en vérifiant la présence d'une intersection précise
     */
    @Test
    public void shouldLoadCarteContainsExactKey() throws FileBadExtensionException, SAXException, IOException {
        Document xml = XMLFileOpener.getInstance().open("./src/test/resources/smallMap.xml");
        XMLDeserializer.loadCarte(carte,xml);
        assertTrue(carte.getIntersections().containsKey(54803122));
    }

    /**
     * Test le chargement d'une carte avec un fichier XML correct en vérifiant les propriétés d'une intersection
     */
    @Test
    public void shouldLoadCarteContainsExactIntersection() throws FileBadExtensionException, SAXException, IOException {
        Document xml = XMLFileOpener.getInstance().open("./src/test/resources/smallMap.xml");
        XMLDeserializer.loadCarte(carte,xml);
        Intersection intersection = carte.getIntersections().get(26086124);
        assertAll("IntersectionProperties",
                () -> assertEquals(45.759098, intersection.getLatitude()),
                () -> assertEquals(4.8629594, intersection.getLongitude()),
                () -> assertEquals(3,intersection.getSegmentsArrivants().size()),
                () -> assertEquals(3,intersection.getSegmentsPartants().size())
        );
    }

    /**
     * Test le chargement d'une carte avec un fichier XML correct en vérifiant le nombre de segments arrivants sur intersection
     */
    @Test
    public void shouldLoadCarteContainsExactNumberOfSegmentsArrivant() throws FileBadExtensionException, SAXException, IOException {
        Document xml = XMLFileOpener.getInstance().open("./src/test/resources/smallMap.xml");
        XMLDeserializer.loadCarte(carte,xml);
        Set<Segment> segmentsArrivants = carte.getIntersections().get(26086128).getSegmentsArrivants();
        assertEquals(4,segmentsArrivants.size());
    }

    /**
     * Test le chargement d'une carte avec un fichier XML correct en vérifiant le nombre de segments partants d'une intersection
     */
    @Test
    public void shouldLoadCarteContainsExactNumberOfSegmentsPartants() throws FileBadExtensionException, SAXException, IOException {
        Document xml = XMLFileOpener.getInstance().open("./src/test/resources/smallMap.xml");
        XMLDeserializer.loadCarte(carte,xml);
        Set<Segment> segmentsPartants = carte.getIntersections().get(459797866).getSegmentsPartants();
        assertEquals(1,segmentsPartants.size());
    }

    /**
     * Test le chargement des requêtes avec un fichier XML correct sans exception levée.
     */
    /*
    @Test
    @Disabled
    public void shouldLoadRequete() {
        try {
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
     */

    @AfterEach
    public void clear(){
        carte.getIntersections().clear();
        planning.getRequetes().clear();
    }
}
