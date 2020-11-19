package fr.hexaone;

import fr.hexaone.utils.XMLFileOpener;
import org.junit.jupiter.api.Test;

import java.io.FileFilter;

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
        Exception exception = assertThrows(XMLFileOpener.class, () ->
                XMLFileOpener.getInstance().open("fileDoesntExist.txt"));
        assertEquals("/ by zero", exception.getMessage());
    }
}
