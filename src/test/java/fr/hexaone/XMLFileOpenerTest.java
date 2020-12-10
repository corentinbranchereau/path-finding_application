package fr.hexaone;

import fr.hexaone.utils.DTDType;
import fr.hexaone.utils.XMLFileOpener;
import fr.hexaone.utils.exception.DTDValidationException;
import fr.hexaone.utils.exception.FileBadExtensionException;
import org.junit.jupiter.api.Test;

import java.io.FileFilter;
import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Test unitaire pour la classe XMLFileOpener
 * @see FileFilter
 * @author HexaOne
 * @version 1.0
 */
public class XMLFileOpenerTest
{
    /**
     * Test l'ouverture d'un fichier dont le chemin est incorrecte (fichier manquant).
     * Une FileNotFoundException est levée
     */
    @Test
    public void doisOuvrirLanceFileNotFoundException()
    {
        assertThrows(FileNotFoundException.class, () -> XMLFileOpener.getInstance().open("./src/test/resources/fileDoesntExist.xml", DTDType.REQUETE));
    }

    /**
     * Test l'ouverture d'un fichier dont le chemin est incorrecte (fichier manquant).
     * Une FileNotFoundException est levée
     */
    @Test
    public void doisOuvrirLanceBadExtensionException()
    {
        assertThrows(FileBadExtensionException.class, () -> XMLFileOpener.getInstance().open("./src/test/resources/fileFormatText.txt", DTDType.REQUETE));
    }

    /**
     * Test l'ouverture d'un fichier de requête dont le DTD n'est pas validé.
     * Une DTDValidationException est levée
     */
    @Test
    public void doisOuvrirRequeteLanceDTDValidationException()
    {
        assertThrows(DTDValidationException.class, () -> XMLFileOpener.getInstance().open("./src/test/resources/requestsSmallDTDError.xml", DTDType.REQUETE));
    }

    /**
     * Test l'ouverture d'un fichier de map dont le DTD n'est pas validé.
     * Une DTDValidationException est levée
     */
    @Test
    public void doisOuvrirCarteLanceDTDValidationException()
    {
        assertThrows(DTDValidationException.class, () -> XMLFileOpener.getInstance().open("./src/test/resources/smallMapDTDError.xml", DTDType.CARTE));
    }
}
