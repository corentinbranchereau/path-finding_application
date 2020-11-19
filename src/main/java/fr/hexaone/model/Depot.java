package fr.hexaone.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Objet contenant les structures de données relatives à un Depot"
 *
 * @author HexaOne
 * @version 1.0
 */
public class Depot extends Intersection {

    /**
     * heure de départ du dépôt
     */
    protected Date departureTime;

    /**
     * constructeur de Depot
     * 
     * @param id
     * @param departureTime
     * @throws ParseException
     */
    public Depot(String id, String departureTime) throws ParseException {
        super(Integer.parseInt(id));
        this.departureTime = new SimpleDateFormat("H:m:s").parse(departureTime); // 24 hours format
    }
}
