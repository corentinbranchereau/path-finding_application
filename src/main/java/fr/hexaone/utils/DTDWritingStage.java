package fr.hexaone.utils;

/**
 * Enum permettant de connaître l'état actuel du placement
 * et de l'écrire de la balise DTD dans un XML, juste
 * après la balise xml.
 */
public enum DTDWritingStage {
    NONE,
    START_BALISE,
    END_BALISE,
    WRITED
}
