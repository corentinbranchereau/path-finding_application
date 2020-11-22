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
 * @author HexaOne
 * @version 1.0
 */
public class EtatInitial implements State{

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleClicChargerCarte(Controleur c) {
        FileChooser fChooser = new FileChooser();
        File fichier = fChooser.showOpenDialog(c.getFenetre().getStage());
        if (fichier != null) {
            XMLFileOpener xmlFileOpener = XMLFileOpener.getInstance();
            try {
                Document xmlCarte = xmlFileOpener.open(fichier.getAbsolutePath());
                Carte carte = new Carte();
                XMLDeserializer.loadCarte(carte, xmlCarte);

                c.getFenetre().getVueGraphique().afficherCarte(carte);
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
    public void handleClicQuitter(Controleur c) {
        System.out.println("handleClicQuitter [initial state implementation] --> TODO");
    }
}
