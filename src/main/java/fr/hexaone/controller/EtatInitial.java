package fr.hexaone.controller;

import fr.hexaone.model.Carte;
import fr.hexaone.utils.XMLDeserializer;
import fr.hexaone.utils.XMLFileOpener;
import fr.hexaone.utils.exception.FileBadExtensionException;
import javafx.stage.FileChooser;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;

/**
 * Implémentation d'un State représentant l'état initial de l'application
 * 
 * @author HexaOne
 * @version 1.0
 */
public class EtatInitial implements State {

    /**
     * {@inheritDoc}
     */
    @Override
    public void chargerCarte(Controleur c) {
        FileChooser fChooser = new FileChooser();
        File fichier = fChooser.showOpenDialog(c.getFenetre().getStage());
        if (fichier != null) {
            XMLFileOpener xmlFileOpener = XMLFileOpener.getInstance();
            try {
                Document xmlCarte = xmlFileOpener.open(fichier.getAbsolutePath());
                c.setCarte(new Carte());
                XMLDeserializer.loadCarte(c.getCarte(), xmlCarte);

                c.getFenetre().getVueGraphique().afficherCarte(c.getCarte());
                c.setEtatCourant(c.etatCarteChargee);
            } catch (IOException e) {
                System.out.println("Erreur lors de l'ouverture du fichier carte : " + e);
            } catch (FileBadExtensionException e) {
                System.out.println("Le fichier sélectionné n'est pas de type XML");
            } catch (SAXException e) {
                System.out.println("Erreur liée au fichier XML : " + e);
            }
        } else {
            System.out.println("Aucun fichier n'a été sélectionné");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void quitterApplication(Controleur c) {
        System.out.println("handleClicQuitter [initial state implementation] --> TODO");
    }
}
