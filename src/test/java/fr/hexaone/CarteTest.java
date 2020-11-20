package fr.hexaone;

import fr.hexaone.model.Carte;

import fr.hexaone.model.Intersection;
import fr.hexaone.model.IntersectionSpeciale;
import fr.hexaone.model.Requete;

import java.util.ArrayList;
import java.util.List;

import org.javatuples.Pair;
import org.junit.jupiter.api.Test;

/**
 * Unit test for simple App.
 */
public class CarteTest {

    @Test
    public void verifyPopTest() {

        Carte carte = new Carte(null);

        List<Intersection> listIntersections = new ArrayList<Intersection>();
        for (int i = 0; i < 10; i++) {
            listIntersections.add(new Intersection(0, 0, i, null, null));
        }

        List<Requete> requetes = new ArrayList<Requete>();

        requetes.add(new Requete(new IntersectionSpeciale(0, 0, 8, null, null, 0, null),
                new IntersectionSpeciale(0, 0, 3, null, null, 0, null)));

        List<Requete> requetes2 = new ArrayList<Requete>();

        requetes.add(new Requete(new IntersectionSpeciale(0, 0, 3, null, null, 0, null),
                new IntersectionSpeciale(0, 0, 4, null, null, 0, null)));

        assert (carte.verifierPop(listIntersections, requetes) == false);

        assert (carte.verifierPop(listIntersections, requetes2) == true);

    }


}