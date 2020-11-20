package fr.hexaone;

import org.junit.jupiter.api.Test;

import fr.hexaone.view.Fenetre;
import javafx.application.Application;
import javafx.application.Platform;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

/**
 * Test unitaire pour la classe Fenetre
 * 
 * @see Fenetre
 * @author HexaOne
 * @version 1.0
 */
public class FenetreTest {

    /**
     * Remplace la sortie système pour vérifier ce qui s'affiche
     */
    private static ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    /**
     * Remplace la sortie système d'erreur pour vérifier ce qui s'affiche
     */
    private static ByteArrayOutputStream errContent = new ByteArrayOutputStream();

    /**
     * Sortie système originale
     */
    private static PrintStream originalOut = System.out;

    /**
     * Sortie système d'erreur originale
     */
    private static PrintStream originalErr = System.err;

    /**
     * Modifie la sortie système et la sortie système d'erreur pour intercepter ce
     * qui s'affiche, puis lance l'application (dans un autre thread) pour les
     * tests.
     */
    @BeforeAll
    public static void setUp() {
        // Remplacement des sorties
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));

        // Lancement de l'application
        Thread appThread = new Thread() {
            @Override
            public void run() {
                Application.launch(App.class);
            }
        };
        appThread.setDaemon(true);
        appThread.start();

        // On laisse un délai pour que l'application puisse s'initialiser
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // Rien
        }
    }

    /**
     * Remet les sorties systèmes originales et arrête l'application
     */
    @AfterAll
    public static void shutdown() {
        System.setOut(originalOut);
        System.setErr(originalErr);
        Platform.exit();
    }

    /**
     * Test le clic sur le bouton "Charger une carte" du menu
     */
    @Test
    public void shouldLoadMap() {
        // On vide la sortie avant le test
        try {
            outContent.flush();
            outContent.reset();
        } catch (IOException e) {
            originalErr.println("Erreur dans le test shouldLoadMap : " + e);
        }

        // On déclenche le bouton pour charger une carte
        App.controleur.getFenetre().getFenetreControleur().getChargerCarteItem().fire();

        // On récupère le string affiché
        String outString = outContent.toString().replace("\n", "").replace("\r", "");

        assertTrue(outString.equals("Charger carte"));
    }

    /**
     * Test le clic sur le bouton "Charger des requêtes" du menu
     */
    @Test
    public void shouldLoadRequetes() {
        // On vide la sortie avant le test
        try {
            outContent.flush();
            outContent.reset();
        } catch (IOException e) {
            originalErr.println("Erreur dans le test shouldLoadRequetes : " + e);
        }

        // On déclenche le bouton pour charger des requêtes
        App.controleur.getFenetre().getFenetreControleur().getChargerRequetesItem().fire();

        // On récupère le string affiché
        String outString = outContent.toString().replace("\n", "").replace("\r", "");

        assertTrue(outString.equals("Charger requêtes"));
    }

    /**
     * Test le clic sur le bouton "Quitter" du menu
     */
    @Test
    public void shouldQuitApp() {
        // On vide la sortie avant le test
        try {
            outContent.flush();
            outContent.reset();
        } catch (IOException e) {
            originalErr.println("Erreur dans le test shouldQuitApp : " + e);
        }

        // On déclenche le bouton pour quitter l'application
        App.controleur.getFenetre().getFenetreControleur().getQuitterItem().fire();

        // On récupère le string affiché
        String outString = outContent.toString().replace("\n", "").replace("\r", "");

        assertTrue(outString.equals("Quitter"));
    }

    /**
     * Test le clic sur le bouton lançant le calcul du planning
     */
    @Test
    public void shouldCalculatePlanning() {
        // On vide la sortie pour les autres tests
        try {
            outContent.flush();
            outContent.reset();
        } catch (IOException e) {
            originalErr.println("Erreur dans le test shouldCalculatePlanning : " + e);
        }

        // On déclenche le bouton pour lancer le calcul du planning
        App.controleur.getFenetre().getFenetreControleur().getBoutonLancer().fire();

        // On récupère le string affiché
        String outString = outContent.toString().replace("\n", "").replace("\r", "");
        assertTrue(outString.equals("Lancement du calcul"));
    }
}