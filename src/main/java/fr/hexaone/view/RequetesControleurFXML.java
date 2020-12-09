package fr.hexaone.view;

import fr.hexaone.model.Demande;
import fr.hexaone.model.Requete;
import fr.hexaone.model.TypeIntersection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;

/**
 * Controleur JavaFX permettant de récupérer les éléments graphiques issus du
 * fichier Requetes FXML
 * 
 * @author HexaOne
 * @version 1.0
 */

public class RequetesControleurFXML {

    /**
     * Le tableau de demande
     */
    @FXML
    private TableView<Demande> tableauDemandes;

    /**
     * La colonne de type
     */
    @FXML
    private TableColumn<Demande, String> typeColumn;

    /**
     * La colonne d'arrivée
     */
    @FXML
    private TableColumn<Demande, String> arriveeColumn;

    /**
     * La colonne de départ
     */
    @FXML
    private TableColumn<Demande, String> departColumn;

    /**
     * La colonne d'adresse
     */
    @FXML
    private TableColumn<Demande, String> adresseColumn;

    /**
     * La colonne de notation orpheline
     */
    @FXML
    private TableColumn<Demande, String> orphelineColumn;

    /**
     * Menu contextuel
     */
    private ContextMenu contextMenu;

    /**
     * Indicateur de type
     */
    private static final DataFormat SERIALIZED_MIME_TYPE = new DataFormat("application/x-java-serialized-object");

    /**
     * La fenêtre
     */
    private Fenetre fenetre;

    /**
     * Map faisant le lien entre les lignes du tableau et leur index dans celui-ci
     */
    private Map<Integer, TableRow<Demande>> mapIndexLignes = new HashMap<>();

    /**
     * définit si les cases du tableau peuvent être déplacées ou non
     */
    private Boolean draggable = false;

    /**
     * définit si le menu contextuel peut être affiché ou non
     */
    private Boolean showContextualMenu = false;

    /**
     * index demande de départ drag and drop
     */
    protected int indexDemandeDepart;

    /**
     * index demande d'arrivée drag and drop
     */
    protected int indexDemandeArrivee;

    /**
     * Liste observable des demandes
     */
    private ObservableList<Demande> listeDemandes = FXCollections.observableArrayList();

    /**
     * Méthode qui se lance après le constructeur, une fois les éléments FXML
     * chargés On définit les règles d'affichage du tableau, les éléments affiché
     * dans les colonnes, et les évènements associés aux lignes
     */
    @FXML
    public void initialize() {
        typeColumn.setCellValueFactory(cellData -> cellData.getValue().getTypeProperty());
        typeColumn.setSortable(false);

        arriveeColumn.setCellValueFactory(cellData -> cellData.getValue().getDateArriveeProperty());
        arriveeColumn.setSortable(false);

        departColumn.setCellValueFactory(cellData -> cellData.getValue().getDateDepartProperty());
        departColumn.setSortable(false);

        adresseColumn.setCellValueFactory(cellData -> cellData.getValue().getNomIntersectionProperty());
        adresseColumn.setSortable(false);

        orphelineColumn.setCellValueFactory(cellData -> cellData.getValue().getOrphelineProperty());
        orphelineColumn.setSortable(false);

        tableauDemandes.setItems(listeDemandes);

        tableauDemandes.getColumns().forEach(e -> e.setReorderable(false));

        // Create ContextMenu
        contextMenu = new ContextMenu();

        MenuItem itemSuppDemande = new MenuItem("Supprimer cette demande");
        itemSuppDemande.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                fenetre.getControleur().supprimerDemande();
            }
        });
        MenuItem itemSuppRequete = new MenuItem("Supprimer la requête associée");
        itemSuppRequete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                fenetre.getControleur().supprimerRequete();
            }
        });

        MenuItem itemModifDemande = new MenuItem("Modifier le lieu ou la durée");
        itemModifDemande.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                fenetre.getControleur().modifierDemande();
            }
        });

        contextMenu.getItems().addAll(itemSuppDemande, itemSuppRequete, itemModifDemande);

        // Création des tooltip d'erreur
        Tooltip tooltipCollecteLivraisonReversed = new Tooltip(
                "Cette Collecte est effectuée après sa livraison associée");
        tooltipCollecteLivraisonReversed.setShowDelay(new Duration(0));
        tooltipCollecteLivraisonReversed.setHideDelay(new Duration(0));

        Tooltip tooltipLivraisonSeule = new Tooltip("Cette Livraison n'est associée à aucune collecte");
        tooltipLivraisonSeule.setShowDelay(new Duration(0));
        tooltipLivraisonSeule.setHideDelay(new Duration(0));

        Tooltip tooltipLivraisonCollecteReversed = new Tooltip(
                "Cette Livraison est effectuée avant sa collecte associée");
        tooltipLivraisonCollecteReversed.setShowDelay(new Duration(0));
        tooltipLivraisonCollecteReversed.setHideDelay(new Duration(0));

        Tooltip tooltipCollecteSeule = new Tooltip("Cette Collecte n'est associée à aucune livraison");
        tooltipCollecteSeule.setShowDelay(new Duration(0));
        tooltipCollecteSeule.setHideDelay(new Duration(0));

        /*
         * définit le type de case affiché dans le tableau, et les évènements associés
         * aux cases.
         */
        tableauDemandes.setRowFactory(tv -> {

            TableRow<Demande> row = new TableRow<Demande>() {
                @Override
                public void updateIndex(int i) {
                    super.updateIndex(i);
                    mapIndexLignes.put(i, this);
                    doUpdateItem(getItem());
                }

                @Override
                protected void updateItem(Demande item, boolean empty) {
                    super.updateItem(item, empty);
                    doUpdateItem(item);
                }

                /**
                 * méthode qui se lance à chaque fois qu'une ligne du tableau est mise à jour.
                 * On y définit la couleur des cases, et les tooltip d'erreur à afficher si
                 * nécessaire
                 * 
                 * @param item l'objet demande associé à la ligne mise à jour
                 */
                protected void doUpdateItem(Demande item) {
                    if (item != null) {
                        Map<Requete, Color> mapCouleur = fenetre.getMapCouleurRequete();
                        Color couleur = mapCouleur.get(item.getRequete());
                        if (getChildren().size() > 0) {
                            ((Cell) getChildren().get(0)).setTextFill(couleur);

                            if (getChildren().size() > 3) {
                                Cell textCell = (Cell) getChildren().get(4);
                                textCell.setTextFill(Color.RED);
                                Boolean reversed = false;

                                Demande itemAssocie;
                                if (item.getTypeIntersection() == TypeIntersection.COLLECTE) {
                                    itemAssocie = item.getRequete().getDemandeLivraison();
                                } else {
                                    itemAssocie = item.getRequete().getDemandeCollecte();
                                }
                                if (itemAssocie != null) {
                                    int indexItemAssocie = tableauDemandes.getItems().indexOf(itemAssocie);

                                    if ((item.getTypeIntersection() == TypeIntersection.COLLECTE
                                            && this.getIndex() > indexItemAssocie)
                                            || (item.getTypeIntersection() == TypeIntersection.LIVRAISON
                                                    && this.getIndex() < indexItemAssocie)) {
                                        reversed = true;

                                    }
                                }
                                if (item.getTypeIntersection() == TypeIntersection.COLLECTE) {
                                    if (reversed) {
                                        textCell.setText(" ! ");
                                        Tooltip.install(textCell, tooltipCollecteLivraisonReversed);

                                    } else {
                                        textCell.setText(item.getOrphelineProperty().get());
                                        if (item.getOrphelineProperty().get() != null) {

                                            Tooltip.install(textCell, tooltipCollecteSeule);
                                        } else {
                                            Tooltip.uninstall(textCell, tooltipCollecteSeule);
                                            Tooltip.uninstall(textCell, tooltipCollecteLivraisonReversed);
                                        }
                                    }

                                } else if (item.getTypeIntersection() == TypeIntersection.LIVRAISON) {
                                    if (reversed) {
                                        textCell.setText(" ! ");

                                        Tooltip.install(textCell, tooltipLivraisonCollecteReversed);

                                    } else {
                                        textCell.setText(item.getOrphelineProperty().get());
                                        if (item.getOrphelineProperty().get() != null) {

                                            Tooltip.install(textCell, tooltipLivraisonSeule);
                                        } else {
                                            Tooltip.uninstall(textCell, tooltipLivraisonSeule);
                                            Tooltip.uninstall(textCell, tooltipLivraisonCollecteReversed);
                                        }
                                    }
                                }
                            }
                            setTextFill(couleur);

                            // On met à jour la date d'arrivée, la date de départ et le nom

                            if (item.getDateArriveeProperty() != null && getChildren().size() == 5) {
                                Cell dateArriveeCell = (Cell) getChildren().get(1);
                                dateArriveeCell.setText(item.getDateArriveeProperty().get());
                            }
                            if (item.getDateDepartProperty() != null && getChildren().size() == 5) {
                                Cell dateDepartCell = (Cell) getChildren().get(2);
                                dateDepartCell.setText(item.getDateDepartProperty().get());
                            }
                            if (item.getNomIntersectionProperty() != null && getChildren().size() == 5) {
                                Cell nomCell = (Cell) getChildren().get(3);
                                nomCell.setText(item.getNomIntersectionProperty().get());
                            }

                        }
                    } else {
                        if (getChildren().size() > 3) {
                            Cell textCell = (Cell) getChildren().get(4);
                            textCell.setText("");
                            Tooltip.uninstall(textCell, tooltipLivraisonSeule);
                            Tooltip.uninstall(textCell, tooltipLivraisonCollecteReversed);
                        }
                    }
                }
            };

            // On ajoute l'entrée <indx, ligne> dans la map
            this.mapIndexLignes.put(row.getIndex(), row);

            /*
             * évènement click droit sur une case du tableau appelant le menu contextuel
             */
            row.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
                @Override
                public void handle(ContextMenuEvent event) {
                    if (row.getItem() == null || !showContextualMenu) {
                        return;
                    }
                    if (fenetre.getControleur().getDemandeSelectionnee() != row.getItem()) {
                        fenetre.getControleur().selectionnerDemande(row.getItem());
                        contextMenu.show(row, event.getScreenX(), event.getScreenY());
                    } else {
                        contextMenu.show(row, event.getScreenX(), event.getScreenY());
                    }
                }
            });

            /*
             * évènement d'une ligne du tableau cliquée et déplacée
             */
            row.setOnDragDetected(event -> {
                if (!row.isEmpty() && draggable) {
                    Integer index = row.getIndex();

                    indexDemandeDepart = index;

                    Dragboard db = row.startDragAndDrop(TransferMode.MOVE);
                    db.setDragView(row.snapshot(null, null));
                    ClipboardContent cc = new ClipboardContent();

                    cc.put(SERIALIZED_MIME_TYPE, index);
                    db.setContent(cc);

                    event.consume();
                }
            });

            /*
             * évènement d'une ligne du tableau déplacée dessus une autre
             */
            row.setOnDragOver(event -> {
                Dragboard db = event.getDragboard();
                if (db.hasContent(SERIALIZED_MIME_TYPE) && draggable) {
                    if (row.getIndex() != ((Integer) db.getContent(SERIALIZED_MIME_TYPE)).intValue()) {
                        event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                        this.fenetre.getVueTextuelle().rechargerHighlight();
                        row.setStyle("-fx-background-color:GREY");
                        event.consume();
                    }
                }
            });

            row.setOnDragExited(event -> {
                Dragboard db = event.getDragboard();
                if (db.hasContent(SERIALIZED_MIME_TYPE) && draggable) {
                    if (row.getIndex() != ((Integer) db.getContent(SERIALIZED_MIME_TYPE)).intValue()) {
                        this.tableauDemandes.getSelectionModel().clearSelection();
                        row.setStyle("-fx-background-color:WHITE");
                        row.setStyle("-fx-text-fill:black");
                        this.fenetre.getVueTextuelle().rechargerHighlight();

                        row.setTextFill(Color.BLACK);
                        event.consume();
                    }
                }
            });

            /*
             * evenement d'une ligne du tableau lachée sur une autre
             */
            row.setOnDragDropped(event -> {
                Dragboard db = event.getDragboard();
                if (db.hasContent(SERIALIZED_MIME_TYPE) && draggable) {
                    int draggedIndex = (Integer) db.getContent(SERIALIZED_MIME_TYPE);
                    Demande draggedItem = tableauDemandes.getItems().remove(draggedIndex);
                    int dropIndex;

                    if (row.isEmpty())
                        dropIndex = tableauDemandes.getItems().size();
                    else
                        dropIndex = row.getIndex();

                    indexDemandeArrivee = dropIndex;
                    tableauDemandes.getItems().add(dropIndex, draggedItem);
                    event.setDropCompleted(true);
                    this.tableauDemandes.getSelectionModel().clearSelection();
                    row.setStyle("-fx-background-color:WHITE");
                    row.setStyle("-fx-text-fill:black");
                    tableauDemandes.getSelectionModel().select(dropIndex);
                    this.fenetre.getVueTextuelle().modifierPlanning(indexDemandeDepart, indexDemandeArrivee);
                    this.fenetre.getVueTextuelle().rechargerHighlight();
                    event.consume();

                }
            });

            /*
             * Evenement du click de souris
             */
            row.setOnMouseClicked(event -> {
                if (event.getButton() != MouseButton.SECONDARY) {
                    if (row.getItem() != null)
                        fenetre.getControleur().selectionnerDemande(row.getItem());
                }
                this.tableauDemandes.getSelectionModel().clearSelection();
            });

            return row;
        });

        typeColumn.setPrefWidth(typeColumn.getPrefWidth() + 1);

    }

    /**
     * getteur tableau
     * 
     * @return TableView(Demande) retourne le tableau tableauDemandes
     */
    public TableView<Demande> getTableauDemandes() {
        return tableauDemandes;
    }

    /**
     * setteur tableau
     * 
     * @param tableauDemandes the tableauDemandes to set
     */
    public void setTableauDemandes(TableView<Demande> tableauDemandes) {
        this.tableauDemandes = tableauDemandes;
    }

    /**
     * getteur colonne type
     * 
     * @return the typeColumn
     */
    public TableColumn<Demande, String> getTypeColumn() {
        return typeColumn;
    }

    /**
     * setteur colonne type
     * 
     * @param typeColumn the typeColumn to set
     */
    public void setTypeColumn(TableColumn<Demande, String> typeColumn) {
        this.typeColumn = typeColumn;
    }

    /**
     * getteur colonne horaire arrivée
     * 
     * @return the arriveeColumn
     */
    public TableColumn<Demande, String> getArriveeColumn() {
        return arriveeColumn;
    }

    /**
     * setteur de la colonne d'horaire arrivée
     * 
     * @param arriveeColumn set l'arriveeColumn
     */
    public void setArriveeColumn(TableColumn<Demande, String> arriveeColumn) {
        this.arriveeColumn = arriveeColumn;
    }

    /**
     * getteur colonne départ
     * 
     * @return the departColumn
     */
    public TableColumn<Demande, String> getDepartColumn() {
        return departColumn;
    }

    /**
     * setteur de la colonne horaire départ
     * 
     * @param departColumn définit departColumn
     */
    public void setDepartColumn(TableColumn<Demande, String> departColumn) {
        this.departColumn = departColumn;
    }

    /**
     * getteur de la colonne adresse
     * 
     * @return the adresseColumn
     */
    public TableColumn<Demande, String> getAdresseColumn() {
        return adresseColumn;
    }

    /**
     * @param adresseColumn the adresseColumn to set
     */
    public void setAdresseColumn(TableColumn<Demande, String> adresseColumn) {
        this.adresseColumn = adresseColumn;
    }

    /**
     * définit la fenetre
     * 
     * @param fenetre the fenetre to set
     */
    public void setFenetre(Fenetre fenetre) {
        this.fenetre = fenetre;
    }

    /**
     * Définit si les colonnes peuvent être glissées déposées.
     * 
     * @param draggable
     */
    public void setDraggable(boolean draggable) {
        this.draggable = draggable;
    }

    /**
     * retourne le boolean draggable
     * 
     * @return boolean
     */
    public Boolean getDraggable() {
        return draggable;
    }

    /**
     * Menu contextuel qui apparait au clic droit sur une demande
     * 
     * @return le menu contextuel
     */
    public ContextMenu getContextMenu() {
        return contextMenu;
    }

    /**
     * Renvoie la map faisant le lien entre les lignes du tableau et leur index
     * 
     * @return La map faisant le lien entre les lignes du tableau et leur index
     */
    public Map<Integer, TableRow<Demande>> getMapIndexLignes() {
        return mapIndexLignes;
    }

    public TableColumn<Demande, String> getOrphelineColumn() {
        return orphelineColumn;
    }

    /**
     * @return ObservableList(Demande) retourne la liste observable listeDemandes
     */
    public ObservableList<Demande> getListeDemandes() {
        return listeDemandes;
    }

    /**
     * setter de liste observable
     *
     * @param list la liste observable
     */
    public void setListeDemandes(ObservableList<Demande> list) {
        this.listeDemandes = list;
    }

    /**
     * Méthode qui permet d'ajouter une demande au tableau dans la vue textuelle
     * 
     * @param demande la demande à ajouter
     */
    public void ajouterDemande(Demande demande) {
        listeDemandes.add(demande);
    }

    /**
     * setter du boolean contextualMenu
     * 
     * @param visible
     */
    public void showContextualMenu(boolean visible) {
        this.showContextualMenu = visible;
    }

    /**
     * getter boolean contextualMenu
     * 
     * @return boolean
     */
    public boolean getContextualMenuVisibility() {
        return this.showContextualMenu;
    }

}
