package fr.hexaone.controller;

import java.io.File;
import java.io.IOException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import fr.hexaone.model.Planning;
import fr.hexaone.utils.XMLDeserializer;
import fr.hexaone.utils.XMLFileOpener;
import fr.hexaone.utils.exception.FileBadExtensionException;
import javafx.stage.FileChooser;

/**
 * Implémentation d'un State représentant l'état de l'application lorsque la
 * carte est chargée dans l'application
 * 
 * @author HexaOne
 * @version 1.0
 */
public class EtatCarteChargee implements State {

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleClicChargerRequetes(Controleur c) {
        System.out.println("handleClicChargerRequetes [carte loaded state implementation]");
    public void chargerRequetes(Controleur c) {
        c.setEtatCourant(c.etatRequetesChargees);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void quitterApplication(Controleur c) {
        System.out.println("handleClicQuitter [carte loaded state implementation] --> TODO");
    }
}
