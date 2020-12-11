package fr.hexaone;

import fr.hexaone.utils.TypeDTD;
import fr.hexaone.utils.XMLFileOpener;
import fr.hexaone.utils.exception.DTDValidationException;
import fr.hexaone.utils.exception.FileBadExtensionException;
import org.junit.jupiter.api.Test;

import java.io.FileFilter;
import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Test unitaire pour la classe XMLFileOpener
 * 
 * @see FileFilter
 * @author HexaOne
 * @version 1.0
 */
public class XMLFileOpenerTest {
    /**
     * Test l'ouverture d'un fichier dont le chemin est incorrecte (fichier
     * manquant). Une FileNotFoundException est levée
     */
    @Test
    public void doisOuvrirLanceFileNotFoundException() {
        assertThrows(FileNotFoundException.class, () -> XMLFileOpener.getInstance()
                .ouvrirXml("./src/test/resources/fileDoesntExist.xml", TypeDTD.REQUETE));
    }

    /**
     * Test l'ouverture d'un fichier dont le chemin est incorrecte (fichier
     * manquant). Une FileNotFoundException est levée
     */
    @Test
    public void doisOuvrirLanceBadExtensionException() {
        assertThrows(FileBadExtensionException.class, () -> XMLFileOpener.getInstance()
                .ouvrirXml("./src/test/resources/fileFormatText.txt", TypeDTD.REQUETE));
    }

    /**
     * Test l'ouverture d'un fichier de requête dont le DTD n'est pas validé. Une
     * DTDValidationException est levée
     */
    @Test
    public void doisOuvrirRequeteLanceDTDValidationException() {
        assertThrows(DTDValidationException.class, () -> XMLFileOpener.getInstance()
                .ouvrirXml("./src/test/resources/requestsSmallDTDError.xml", TypeDTD.REQUETE));
    }

    /**
     * Test l'ouverture d'un fichier de map dont le DTD n'est pas validé. Une
     * DTDValidationException est levée
     */
    @Test
    public void doisOuvrirCarteLanceDTDValidationException() {
        assertThrows(DTDValidationException.class, () -> XMLFileOpener.getInstance()
                .ouvrirXml("./src/test/resources/smallMapDTDError.xml", TypeDTD.CARTE));
    }
}
