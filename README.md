# 4IF_PLD_AGILE
Agile Long Duration Project at INSA Lyon - Pick &amp; Delivery
Group project of 6 persons

[Site internet contenant la JavaDoc](http://agile.hexaone.fr/)

[image de l'application](./app.png)

## Fonctionnalités

### Chargement d'une carte au format XML:
Notre exemple s'appuie sur l'agglomération lyonnaise. La carte possède différentes fonctionnalitées telles qu'un zoom, une mise en valeur des rues et des intersections. On retrouve aussi le nom de chaque rue au survol.

### Chargement de requêtes au format XML:
Il est possible de charger une liste comprenant des collectes et livraisons avec une durée d'arrêt associée. On peut alors voir ces demandes sur la carte ainsi que dans la vue textuelle sous forme de tableau.

### Calcul de la meilleure tournée:
Une combinaison N-Dijkstra et algorithme génétique appliqué au problème du voyageur de commerce permet de calculer une tournée la plus rapide sur l'ensemble des demandes, en veillant à passer d'abord au point de collecte avant une livraison. Cette dernière est affichée directement sur la carte et met à jour la vue textuelle.

### Mise en avant d'une demande
Une synchronisation vue graphique/vue textuelle grâce au design architectural MVC permet de sélectionner sur l'une des vues la demande voulue (paire collecte/livraison), mettant cette dernière en valeur sur les deux vues.

Il existe bien d'autres fonctionnalités à découvrir telles que l'ajout, la modification et la suppression de demande, l'undo/redo ou encore la modification d'une tournée par un drag'n'drop. Un menu d'aide est aussi disponible afin de guider l'utilisateur tout au long du cycle de vie de l'application.

Developed by HexaOne
