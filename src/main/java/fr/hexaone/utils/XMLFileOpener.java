package fr.hexaone.utils;

import java.io.*;

/**
 * Permet d'ouvrir un fichier et de version type (XML).
 * Implémente FileFilter du package java.io
 * @see FileFilter
 * @author HexaOne
 * @version 1.0
 */
public class XMLFileOpener implements FileFilter {

    /**
     * L'instance SINGLETON de la classe.
     */
    private static XMLFileOpener instance = null;

    /**
     * Constructeur par défaut privé.
     */
    private XMLFileOpener(){}

    /**
     * Implémentation du design pattern SINGLETON afin d'implémenter l'interface FileFilter du package java.io
     * en gardant un comportement static pour la classe
     * @return l'unique instance de la classe XMLFileOpener. Si elle n'existe pas, l'instancie.
     */
    public static XMLFileOpener getInstance(){
        if(instance == null) instance = new XMLFileOpener();
        return instance;
    }

    /**
     * Ouverture d'un fichier dont l'URI est spécifié en paramètre
     * @param path L'URI du fichier
     * @throws FileNotFoundException Lorsque l'URI spécifié n'existe pas
     * @return Le flux vers le fichier ouvert
     */
    public BufferedReader open(String path) throws FileNotFoundException {
        return new BufferedReader(new FileReader(new File(path)));
    }

    /**
     * Vérifie l'extension du fichier ouvert
     * @param file Le fichier à vérifier
     * @return true si le fichier est au format XML, false sinon.
     */
    @Override
    public boolean accept(File file) {
        return file.getName().endsWith(".xml");
    }
}
