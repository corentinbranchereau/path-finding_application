package fr.hexaone;

import fr.hexaone.model.Carte;
import fr.hexaone.model.Intersection;
import fr.hexaone.model.Planning;
import fr.hexaone.model.Requete;
import fr.hexaone.model.Segment;
import fr.hexaone.utils.XMLDeserializer;
import fr.hexaone.utils.XMLFileOpener;
import fr.hexaone.utils.exception.FileBadExtensionException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledForJreRange;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    void init() {
        carte = new Carte();
        planning = new Planning();
    }

    /**
     * Test le chargement d'une carte avec un fichier XML correct sans exception
     * levée.
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
     * Test le chargement d'une carte avec un fichier XML correct en vérifiant le
     * nombre d'intersections
     */
    @Test
    public void shouldLoadCarteWithGoodSize() throws FileBadExtensionException, SAXException, IOException {
        Document xml = XMLFileOpener.getInstance().open("./src/test/resources/smallMap.xml");
        XMLDeserializer.loadCarte(carte, xml);
        assertEquals(308, carte.getIntersections().size());
    }

    /**
     * Test le chargement d'une carte avec un fichier XML correct en vérifiant la
     * présence d'une intersection précise
     */
    @Test
    public void shouldLoadCarteContainsExactKey() throws FileBadExtensionException, SAXException, IOException {
        Document xml = XMLFileOpener.getInstance().open("./src/test/resources/smallMap.xml");
        XMLDeserializer.loadCarte(carte, xml);
        assertTrue(carte.getIntersections().containsKey(54803122L));
    }

    /**
     * Test le chargement d'une carte avec un fichier XML correct en vérifiant les
     * propriétés d'une intersection
     */
    @Test
    public void shouldLoadCarteContainsExactIntersection() throws FileBadExtensionException, SAXException, IOException {
        Document xml = XMLFileOpener.getInstance().open("./src/test/resources/smallMap.xml");
        XMLDeserializer.loadCarte(carte, xml);
        Intersection intersection = carte.getIntersections().get(26086124L);
        assertAll("IntersectionProperties", () -> assertEquals(45.759098, intersection.getLatitude()),
                () -> assertEquals(4.8629594, intersection.getLongitude()),
                () -> assertEquals(3, intersection.getSegmentsArrivants().size()),
                () -> assertEquals(3, intersection.getSegmentsPartants().size()));
    }

    /**
     * Test le chargement d'une carte avec un fichier XML correct en vérifiant le
     * nombre de segments arrivants sur intersection
     */
    @Test
    public void shouldLoadCarteContainsExactNumberOfSegmentsArrivant()
            throws FileBadExtensionException, SAXException, IOException {
        Document xml = XMLFileOpener.getInstance().open("./src/test/resources/smallMap.xml");
        XMLDeserializer.loadCarte(carte, xml);
        Set<Segment> segmentsArrivants = carte.getIntersections().get(26086128L).getSegmentsArrivants();
        assertEquals(4, segmentsArrivants.size());
    }

    /**
     * Test le chargement d'une carte avec un fichier XML correct en vérifiant le
     * nombre de segments partants d'une intersection
     */
    @Test
    public void shouldLoadCarteContainsExactNumberOfSegmentsPartants()
            throws FileBadExtensionException, SAXException, IOException {
        Document xml = XMLFileOpener.getInstance().open("./src/test/resources/smallMap.xml");
        XMLDeserializer.loadCarte(carte, xml);
        Set<Segment> segmentsPartants = carte.getIntersections().get(459797866L).getSegmentsPartants();
        assertEquals(1, segmentsPartants.size());
    }

    /**
     * Test le chargement des requêtes avec un fichier XML correct sans exception
     * levée.
     */
    @Test
    public void shouldLoadRequete() {
        try {
            XMLFileOpener xmlFileOpener = XMLFileOpener.getInstance();
            Document xml = xmlFileOpener.open("./src/test/resources/requestsMedium5.xml");
            XMLDeserializer.loadRequete(xml, planning);
        } catch (Exception e) {
            fail();
        }
    }

    /**
     * Test le chargement des requêtes avec un fichier XML correct en vérifiant les
     * propriétés du depot.
     */
    // TODO : Bug Date
    @Test
    @Disabled
    public void shouldLoadRequeteDepot() {
        try {
            XMLFileOpener xmlFileOpener = XMLFileOpener.getInstance();
            Document xml = xmlFileOpener.open("./src/test/resources/requestsMedium5.xml");
            XMLDeserializer.loadRequete(xml, planning);
            assertAll("DepotProperties", () -> assertEquals(4150019167L, planning.getIdDepot()), () -> assertTrue(
                    planning.getDateDebut().compareTo(new SimpleDateFormat("H:m:s").parse("8:0:0")) == 0));
        } catch (Exception e) {
            fail();
        }
    }

    /**
     * Test le chargement des requêtes avec un fichier XML correct en vérifiant le
     * nombre de requete.
     */
    @Test
    public void shouldLoadRequeteWithGoodNumberOfRequete() {
        try {
            XMLFileOpener xmlFileOpener = XMLFileOpener.getInstance();
            Document xml = xmlFileOpener.open("./src/test/resources/requestsMedium5.xml");
            XMLDeserializer.loadRequete(xml, planning);
            assertEquals(5, planning.getRequetes().size());
        } catch (Exception e) {
            fail();
        }
    }

    /**
     * Test le chargement des requêtes avec un fichier XML correct en vérifiant les
     * propriétés requete précise.
     */
    @Test
    public void shouldLoadRequeteWithExactRequete() {
        try {
            XMLFileOpener xmlFileOpener = XMLFileOpener.getInstance();
            Document xml = xmlFileOpener.open("./src/test/resources/requestsMedium5.xml");
            XMLDeserializer.loadRequete(xml, carte, planning);
            boolean testPresence = false;
            Long idPickupTest = 1400900990L, idDeliveryTest = 208769083L;
            int pickupDurationTest = 180, deliveryDurationTest = 240;
            for (Requete requete : planning.getRequetes()) {
                if (requete.getIdPickup() == idPickupTest && requete.getIdDelivery() == idDeliveryTest
                        && requete.getDureePickup() == pickupDurationTest
                        && requete.getDureeDelivery() == deliveryDurationTest)
                    testPresence = true;
            }
            assertTrue(testPresence);
        } catch (Exception e) {
            fail();
        }
    }

    @AfterEach
    public void clear() {
        carte.getIntersections().clear();
        planning.getRequetes().clear();
    }
}