package fr.hexaone;

import fr.hexaone.controller.Command.*;
import fr.hexaone.model.*;
import fr.hexaone.utils.DTDType;
import fr.hexaone.utils.XMLDeserializer;
import fr.hexaone.utils.XMLFileOpener;
import fr.hexaone.utils.exception.*;
import javafx.scene.control.Alert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;


import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour le design pattern COMMAND
 */
public class CommandTest {

    private Carte carte;
    private Planning planning;

    /**
     * Instancie une carte avec des requêtes et le calcul d'une tournée pour effectuer
     * ensuite différentes actions
     * @throws URISyntaxException Exception de mauvais chemin de fichier
     * @throws SAXException Exception de mauvaise formation XML
     * @throws FileBadExtensionException Exception pour un fichier de type non XML
     * @throws DTDValidationException Exception pour un DTD non validé
     * @throws IOException Exception lors de la lecture du fichier sur le disque
     * @throws BadFileTypeException Exception sur le type du fichier chargé (carte ou requête)
     * @throws IllegalAttributException Exception sur un attribut erronné
     * @throws RequestOutOfMapException Exception pour une requête hors carte
     */
    @BeforeEach
    void init() throws URISyntaxException, SAXException, FileBadExtensionException, DTDValidationException, IOException, BadFileTypeException, IllegalAttributException, RequestOutOfMapException {
        carte = new Carte();
        planning = new Planning(carte);

        Document xmlCarte = XMLFileOpener.getInstance().open("./src/test/resources/smallMap.xml", DTDType.CARTE);
        XMLDeserializer.loadCarte(carte, xmlCarte);

        Document xmlRequete = XMLFileOpener.getInstance().open("./src/test/resources/requestsSmall2.xml", DTDType.REQUETE);
        XMLDeserializer.loadRequete(xmlRequete, planning);

        if (!planning.calculerMeilleurTournee()) {
            fail();
        }
    }

    /**
     * Test l'ajout d'une demande dans une tournée déjà calculée
     */
    @Test
    public void doisAjouterDemande() {
        ListOfCommands listOfCommands = new ListOfCommands();
        Requete nouvelleRequete = new Requete(25611760, 120, "pickup", TypeIntersection.COLLECTE);
        Demande nouvelleDemande = nouvelleRequete.getDemandeCollecte();
        listOfCommands.add(new AjouterDemandeCommand(planning,nouvelleDemande));
        assertTrue(planning.getDemandesOrdonnees().contains(nouvelleDemande));
    }

    /**
     * Test la suppression d'une demande dans une tournée déjà calculée
     */
    @Test
    public void doisRetirerDemande() {
        ListOfCommands listOfCommands = new ListOfCommands();
        Demande demande = planning.getDemandesOrdonnees().get(0);
        listOfCommands.add(new SupprimerDemandeCommand(planning,demande));
        assertFalse(planning.getDemandesOrdonnees().contains(demande));
    }

    /**
     * Test l'undo après un ajout de demande dans une tournée déjà calculée
     */
    @Test
    public void doisUndoAjouterDemande() {
        ListOfCommands listOfCommands = new ListOfCommands();
        Requete nouvelleRequete = new Requete(25611760, 120, "pickup", TypeIntersection.COLLECTE);
        Demande nouvelleDemande = nouvelleRequete.getDemandeCollecte();
        listOfCommands.add(new AjouterDemandeCommand(planning,nouvelleDemande));
        assertTrue(planning.getDemandesOrdonnees().contains(nouvelleDemande));
        listOfCommands.undo();
        assertFalse(planning.getDemandesOrdonnees().contains(nouvelleDemande));
    }

    /**
     * Test l'undo après la suppression d'une demande dans une tournée déjà calculée
     */
    @Test
    public void doisUndoRetirerDemande() {
        ListOfCommands listOfCommands = new ListOfCommands();
        Demande demande = planning.getDemandesOrdonnees().get(0);
        listOfCommands.add(new SupprimerDemandeCommand(planning,demande));
        assertFalse(planning.getDemandesOrdonnees().contains(demande));
        listOfCommands.undo();
        assertTrue(planning.getDemandesOrdonnees().contains(demande));
    }

    /**
     * Test le redo après l'ajout d'une demande puis un undo dans une tournée déjà calculée
     */
    @Test
    public void doisRedoAjouterDemande() {
        ListOfCommands listOfCommands = new ListOfCommands();
        Requete nouvelleRequete = new Requete(25611760, 120, "pickup", TypeIntersection.COLLECTE);
        Demande nouvelleDemande = nouvelleRequete.getDemandeCollecte();
        listOfCommands.add(new AjouterDemandeCommand(planning,nouvelleDemande));
        assertTrue(planning.getDemandesOrdonnees().contains(nouvelleDemande));
        listOfCommands.undo();
        assertFalse(planning.getDemandesOrdonnees().contains(nouvelleDemande));
        listOfCommands.redo();
        assertTrue(planning.getDemandesOrdonnees().contains(nouvelleDemande));
    }

    /**
     * Test le redo après la suppression d'une demande puis un undo dans une tournée déjà calculée
     */
    @Test
    public void doisRedoRetirerDemande() {
        ListOfCommands listOfCommands = new ListOfCommands();
        Demande demande = planning.getDemandesOrdonnees().get(0);
        listOfCommands.add(new SupprimerDemandeCommand(planning,demande));
        assertFalse(planning.getDemandesOrdonnees().contains(demande));
        listOfCommands.undo();
        assertTrue(planning.getDemandesOrdonnees().contains(demande));
        listOfCommands.redo();
        assertFalse(planning.getDemandesOrdonnees().contains(demande));
    }

    /**
     * Test l'ajout d'une requête dans une tournée déjà calculée
     */
    @Test
    public void doisAjouterRequete() {
        ListOfCommands listOfCommands = new ListOfCommands();
        Requete nouvelleRequete = new Requete(25611760, 120, "pickup", 25611411, 240, "delivery");
        listOfCommands.add(new AjouterRequeteCommand(planning,nouvelleRequete));
        assertTrue(planning.getDemandesOrdonnees().contains(nouvelleRequete.getDemandeCollecte()));
        assertTrue(planning.getDemandesOrdonnees().contains(nouvelleRequete.getDemandeLivraison()));
    }

    /**
     * Test la suppression d'une requête dans une tournée déjà calculée
     */
    @Test
    public void doisRetirerRequete() {
        ListOfCommands listOfCommands = new ListOfCommands();
        Requete requete = planning.getDemandesOrdonnees().get(0).getRequete();
        listOfCommands.add(new SupprimerRequeteCommand(planning,requete));
        assertFalse(planning.getDemandesOrdonnees().contains(requete.getDemandeCollecte()));
        assertFalse(planning.getDemandesOrdonnees().contains(requete.getDemandeLivraison()));
    }

    /**
     * Test l'undo après un ajout de requête dans une tournée déjà calculée
     */
    @Test
    public void doisUndoAjouterRequete() {
        ListOfCommands listOfCommands = new ListOfCommands();
        Requete nouvelleRequete = new Requete(25611760, 120, "pickup", 25611411, 240, "delivery");
        listOfCommands.add(new AjouterRequeteCommand(planning,nouvelleRequete));
        assertTrue(planning.getDemandesOrdonnees().contains(nouvelleRequete.getDemandeCollecte()));
        assertTrue(planning.getDemandesOrdonnees().contains(nouvelleRequete.getDemandeLivraison()));
        listOfCommands.undo();
        assertFalse(planning.getDemandesOrdonnees().contains(nouvelleRequete.getDemandeCollecte()));
        assertFalse(planning.getDemandesOrdonnees().contains(nouvelleRequete.getDemandeLivraison()));
    }

    /**
     * Test l'undo après la suppression d'une requête dans une tournée déjà calculée
     */
    @Test
    public void doisUndoRetirerRequete() {
        ListOfCommands listOfCommands = new ListOfCommands();
        Requete requete = planning.getDemandesOrdonnees().get(0).getRequete();
        listOfCommands.add(new SupprimerRequeteCommand(planning,requete));
        assertFalse(planning.getDemandesOrdonnees().contains(requete.getDemandeCollecte()));
        assertFalse(planning.getDemandesOrdonnees().contains(requete.getDemandeLivraison()));
        listOfCommands.undo();
        assertTrue(planning.getDemandesOrdonnees().contains(requete.getDemandeCollecte()));
        assertTrue(planning.getDemandesOrdonnees().contains(requete.getDemandeLivraison()));
    }

    /**
     * Test le redo après l'ajout d'une requête puis un undo dans une tournée déjà calculée
     */
    @Test
    public void doisRedoAjouterRequete() {
        ListOfCommands listOfCommands = new ListOfCommands();
        Requete nouvelleRequete = new Requete(25611760, 120, "pickup", 25611411, 240, "delivery");
        listOfCommands.add(new AjouterRequeteCommand(planning,nouvelleRequete));
        assertTrue(planning.getDemandesOrdonnees().contains(nouvelleRequete.getDemandeCollecte()));
        assertTrue(planning.getDemandesOrdonnees().contains(nouvelleRequete.getDemandeLivraison()));
        listOfCommands.undo();
        assertFalse(planning.getDemandesOrdonnees().contains(nouvelleRequete.getDemandeCollecte()));
        assertFalse(planning.getDemandesOrdonnees().contains(nouvelleRequete.getDemandeLivraison()));
        listOfCommands.redo();
        assertTrue(planning.getDemandesOrdonnees().contains(nouvelleRequete.getDemandeCollecte()));
        assertTrue(planning.getDemandesOrdonnees().contains(nouvelleRequete.getDemandeLivraison()));
    }

    /**
     * Test le redo après la suppression d'une requête puis un undo dans une tournée déjà calculée
     */
    @Test
    public void doisRedoRetirerRequete() {
        ListOfCommands listOfCommands = new ListOfCommands();
        Requete requete = planning.getDemandesOrdonnees().get(0).getRequete();
        listOfCommands.add(new SupprimerRequeteCommand(planning,requete));
        assertFalse(planning.getDemandesOrdonnees().contains(requete.getDemandeCollecte()));
        assertFalse(planning.getDemandesOrdonnees().contains(requete.getDemandeLivraison()));
        listOfCommands.undo();
        assertTrue(planning.getDemandesOrdonnees().contains(requete.getDemandeCollecte()));
        assertTrue(planning.getDemandesOrdonnees().contains(requete.getDemandeLivraison()));
        listOfCommands.redo();
        assertFalse(planning.getDemandesOrdonnees().contains(requete.getDemandeCollecte()));
        assertFalse(planning.getDemandesOrdonnees().contains(requete.getDemandeLivraison()));
    }

    /**
     * Test la modification d'une demande dans une tournée déjà calculée
     */
    @Test
    public void doisModifierDemande() {
        ListOfCommands listOfCommands = new ListOfCommands();
        listOfCommands.add(new ModifierDemandeCommand(planning,planning.getDemandesOrdonnees().get(0),120,26086114L));
        assertEquals(26086114L, (long) planning.getDemandesOrdonnees().get(0).getIdIntersection());
        assertEquals(120, (int) planning.getDemandesOrdonnees().get(0).getDuree());
    }

    /**
     * Test la modification d'un planning dans une tournée déjà calculée
     */
    @Test
    public void doisModifierPlanning() {
        ListOfCommands listOfCommands = new ListOfCommands();
        Demande demandeNumero1 = planning.getDemandesOrdonnees().get(0);
        Demande demandeNumero2 = planning.getDemandesOrdonnees().get(1);
        listOfCommands.add(new ModifierPlanningCommand(planning,0,1));
        assertEquals(demandeNumero1,planning.getDemandesOrdonnees().get(1));
        assertEquals(demandeNumero2,planning.getDemandesOrdonnees().get(0));

    }

    /**
     * Test l'undo après la modification d'une demande dans une tournée déjà calculée
     */
    @Test
    public void doisUndoModifierDemande() {
        ListOfCommands listOfCommands = new ListOfCommands();
        Long idIntersectionDepart = planning.getDemandesOrdonnees().get(0).getIdIntersection();
        int dureeDepart = planning.getDemandesOrdonnees().get(0).getDuree();
        listOfCommands.add(new ModifierDemandeCommand(planning,planning.getDemandesOrdonnees().get(0),120,26086114L));
        assertEquals(26086114L, (long) planning.getDemandesOrdonnees().get(0).getIdIntersection());
        assertEquals(120, (int) planning.getDemandesOrdonnees().get(0).getDuree());
        listOfCommands.undo();
        assertEquals(idIntersectionDepart, (long) planning.getDemandesOrdonnees().get(0).getIdIntersection());
        assertEquals(dureeDepart, (int) planning.getDemandesOrdonnees().get(0).getDuree());
    }

    /**
     * Test l'undo après la modification d'un planning dans une tournée déjà calculée
     */
    @Test
    public void doisUndoModifierPlanning() {
        ListOfCommands listOfCommands = new ListOfCommands();
        Demande demandeNumero1 = planning.getDemandesOrdonnees().get(0);
        Demande demandeNumero2 = planning.getDemandesOrdonnees().get(1);
        listOfCommands.add(new ModifierPlanningCommand(planning,0,1));
        assertEquals(demandeNumero1,planning.getDemandesOrdonnees().get(1));
        assertEquals(demandeNumero2,planning.getDemandesOrdonnees().get(0));
        listOfCommands.undo();
        assertEquals(demandeNumero1,planning.getDemandesOrdonnees().get(0));
        assertEquals(demandeNumero2,planning.getDemandesOrdonnees().get(1));
    }

    /**
     * Test le redo après la modification d'une demande puis un undo dans une tournée déjà calculée
     */
    @Test
    public void doisRedoModifierDemande() {
        ListOfCommands listOfCommands = new ListOfCommands();
        Long idIntersectionDepart = planning.getDemandesOrdonnees().get(0).getIdIntersection();
        int dureeDepart = planning.getDemandesOrdonnees().get(0).getDuree();
        listOfCommands.add(new ModifierDemandeCommand(planning,planning.getDemandesOrdonnees().get(0),120,26086114L));
        assertEquals(26086114L, (long) planning.getDemandesOrdonnees().get(0).getIdIntersection());
        assertEquals(120, (int) planning.getDemandesOrdonnees().get(0).getDuree());
        listOfCommands.undo();
        assertEquals(idIntersectionDepart, (long) planning.getDemandesOrdonnees().get(0).getIdIntersection());
        assertEquals(dureeDepart, (int) planning.getDemandesOrdonnees().get(0).getDuree());
        listOfCommands.redo();
        assertEquals(26086114L, (long) planning.getDemandesOrdonnees().get(0).getIdIntersection());
        assertEquals(120, (int) planning.getDemandesOrdonnees().get(0).getDuree());
    }

    /**
     * Test le redo après la modification d'un planning puis un undo dans une tournée déjà calculée
     */
    @Test
    public void doisRedoModifierRequete() {
        ListOfCommands listOfCommands = new ListOfCommands();
        Demande demandeNumero1 = planning.getDemandesOrdonnees().get(0);
        Demande demandeNumero2 = planning.getDemandesOrdonnees().get(1);
        listOfCommands.add(new ModifierPlanningCommand(planning,0,1));
        assertEquals(demandeNumero1,planning.getDemandesOrdonnees().get(1));
        assertEquals(demandeNumero2,planning.getDemandesOrdonnees().get(0));
        listOfCommands.undo();
        assertEquals(demandeNumero1,planning.getDemandesOrdonnees().get(0));
        assertEquals(demandeNumero2,planning.getDemandesOrdonnees().get(1));
        listOfCommands.redo();
        assertEquals(demandeNumero1,planning.getDemandesOrdonnees().get(1));
        assertEquals(demandeNumero2,planning.getDemandesOrdonnees().get(0));
    }

    /**
     * Nettoyage de la mémoire après un test.
     */
    @AfterEach
    public void clear() {
        carte.getIntersections().clear();
        planning.getRequetes().clear();
    }
}
