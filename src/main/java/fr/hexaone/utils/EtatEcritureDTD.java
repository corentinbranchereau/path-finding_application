package fr.hexaone.utils;

/**
 * Enum permettant de connaître l'état actuel du placement et de l'écriture de
 * la balise DTD dans un XML, juste après la balise XML.
 * 
 * @author HexaOne
 * @version 1.0
 */
public enum EtatEcritureDTD {
    AUCUN, DEBUT_BALISE, FIN_BALISE, ECRIT
}
