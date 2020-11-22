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
    public void chargerRequetes(Controleur c) {
        FileChooser fChooser = new FileChooser();
        File fichier = fChooser.showOpenDialog(c.getFenetre().getStage());
        if (fichier != null) {
            XMLFileOpener xmlFileOpener = XMLFileOpener.getInstance();
            try {
                Document xmlRequete = xmlFileOpener.open(fichier.getAbsolutePath());
                c.setPlanning(new Planning());
                XMLDeserializer.loadRequete(xmlRequete, null, c.getPlanning());

                c.getFenetre().getVueGraphique().afficherRequetes(c.getPlanning(), c.getCarte());
            } catch (IOException e) {
                System.out.println("Erreur lors de l'ouverture du fichier de requêtes : " + e);
            } catch (FileBadExtensionException e) {
                System.out.println("Le fichier sélectionné n'est pas de type XML");
            } catch (SAXException e) {
                System.out.println("Erreur liée au fichier XML : " + e);
            }
        } else {
            System.out.println("Aucun fichier n'a été sélectionné");
        }

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
