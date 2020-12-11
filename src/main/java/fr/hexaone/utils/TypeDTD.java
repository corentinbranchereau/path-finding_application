package fr.hexaone.utils;

/**
 * Enumération des différents types de DTD disponibles et leur chemin d'accès.
 */
public enum TypeDTD {

    /**
     * Le DTD carte
     */
    CARTE("carte.dtd"),
    /**
     * Le DTD requête
     */
    REQUETE("requetes.dtd");

    /**
     * Le chemin du DTD dans le dossier des ressources.
     */
    private String chemin;

    /**
     * Constructeur privé de l'enum.
     * 
     * @param chemin Le chemin du DTD dans le dossier des ressources.
     */
    private TypeDTD(String chemin) {
        this.chemin = chemin;
    }

    /**
     * @return Le chemin du DTD dans le dossier des ressources.
     */
    public String getChemin() {
        return chemin;
    }
}
