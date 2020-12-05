package fr.hexaone.utils;

/**
 * Enumération des différents types de DTD disponibles et leur
 * chemin d'accès.
 */
public enum DTDType {

    CARTE("pathToMap"),
    REQUETE("pathToRequete");

    /**
     * Le chemin du DTD dans le dossier resources.
     */
    private String path;

    /**
     * Constructeur privé de l'énum.
     * @param path Le chemin du DTD dans le dossier resources.
     */
    private DTDType(String path){
        this.path = path;
    }

    /**
     * Getter
     * @return Le chemin du DTD dans le dossier resources.
     */
    public String getPath() {
        return path;
    }
}
