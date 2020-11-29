package fr.hexaone;

import fr.hexaone.utils.XMLFileOpener;
import fr.hexaone.utils.exception.DTDValidationException;
import fr.hexaone.utils.exception.FileBadExtensionException;
import org.junit.jupiter.api.Test;

import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    public void shouldOpenThrowFileNotFoundException()
    {
        assertThrows(FileNotFoundException.class, () -> XMLFileOpener.getInstance().open("./src/test/resources/fileDoesntExist.xml"));
    }

    /**
     * Test l'ouverture d'un fichier dont le chemin est incorrecte (fichier manquant).
     * Une FileNotFoundException est levée
     */
    @Test
    public void shouldOpenThrowBadExtensionException()
    {
        assertThrows(FileBadExtensionException.class, () -> XMLFileOpener.getInstance().open("./src/test/resources/fileFormatText.txt"));
    }

    /**
     * Test l'ouverture d'un fichier de requête dont le DTD n'est pas validé.
     * Une DTDValidationException est levée
     */
    @Test
    public void shouldOpenRequestThrowDTDValidationException()
    {
        assertThrows(DTDValidationException.class, () -> XMLFileOpener.getInstance().open("./src/test/resources/requestsSmallDTDError.xml"));
    }

    /**
     * Test l'ouverture d'un fichier de map dont le DTD n'est pas validé.
     * Une DTDValidationException est levée
     */
    @Test
    public void shouldOpenMapThrowDTDValidationException()
    {
        assertThrows(DTDValidationException.class, () -> XMLFileOpener.getInstance().open("./src/test/resources/smallMapDTDError.xml"));
    }
}
