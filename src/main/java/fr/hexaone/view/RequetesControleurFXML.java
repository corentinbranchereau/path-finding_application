package fr.hexaone.view;

import fr.hexaone.model.Demande;
import fr.hexaone.model.Requete;
import fr.hexaone.model.TypeDemande;
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
     * La colonne de type de la demande
     */
    @FXML
    private TableColumn<Demande, String> colonneType;

    /**
     * La colonne de l'arrivée de la demande
     */
    @FXML
    private TableColumn<Demande, String> colonneArrivee;

    /**
     * La colonne de départ de la demande
     */
    @FXML
    private TableColumn<Demande, String> colonneDepart;

    /**
     * La colonne d'adresse de la demande
     */
    @FXML
    private TableColumn<Demande, String> colonneAdresse;

    /**
     * La colonne de notation orpheline
     */
    @FXML
    private TableColumn<Demande, String> colonneOrpheline;

    /**
     * Le menu contextuel
     */
    private ContextMenu menuContextuel;

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
     * Définit si les cases du tableau peuvent être déplacées ou non
     */
    private Boolean draggable = false;

    /**
     * Définit si le menu contextuel peut être affiché ou non
     */
    private Boolean menuContextuelAffichable = false;

    /**
     * Index de départ de la demande lors du drag and drop
     */
    private int indexDepartDemande;

    /**
     * Index d'arrivée de la demande lors du drag and drop
     */
    private int indexArriveeDemande;

    /**
     * Liste observable des demandes
     */
    private ObservableList<Demande> listeDemandes = FXCollections.observableArrayList();

    /**
     * Méthode qui se lance après le constructeur, une fois les éléments FXML
     * chargés. On définit les règles d'affichage du tableau, les éléments affichés
     * dans les colonnes, et les évènements associés aux lignes
     */
    @FXML
    public void initialize() {
        colonneType.setCellValueFactory(cellData -> cellData.getValue().getProprieteType());
        colonneType.setSortable(false);

        colonneArrivee.setCellValueFactory(cellData -> cellData.getValue().getProprieteDateArrivee());
        colonneArrivee.setSortable(false);

        colonneDepart.setCellValueFactory(cellData -> cellData.getValue().getProprieteDateDepart());
        colonneDepart.setSortable(false);

        colonneAdresse.setCellValueFactory(cellData -> cellData.getValue().getProprieteNomIntersection());
        colonneAdresse.setSortable(false);

        colonneOrpheline.setCellValueFactory(cellData -> cellData.getValue().getProprieteOrpheline());
        colonneOrpheline.setSortable(false);

        tableauDemandes.setItems(listeDemandes);

        tableauDemandes.getColumns().forEach(e -> e.setReorderable(false));

        // Crée le menu contextuel
        menuContextuel = new ContextMenu();

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

        menuContextuel.getItems().addAll(itemSuppDemande, itemSuppRequete, itemModifDemande);

        // Création des tooltips d'erreur
        Tooltip tooltipCollecteLivraisonInversee = new Tooltip(
                "Cette Collecte est effectuée après sa livraison associée");
        tooltipCollecteLivraisonInversee.setShowDelay(new Duration(0));
        tooltipCollecteLivraisonInversee.setHideDelay(new Duration(0));

        Tooltip tooltipLivraisonSeule = new Tooltip("Cette Livraison n'est associée à aucune collecte");
        tooltipLivraisonSeule.setShowDelay(new Duration(0));
        tooltipLivraisonSeule.setHideDelay(new Duration(0));

        Tooltip tooltipLivraisonCollecteInversee = new Tooltip(
                "Cette Livraison est effectuée avant sa collecte associée");
        tooltipLivraisonCollecteInversee.setShowDelay(new Duration(0));
        tooltipLivraisonCollecteInversee.setHideDelay(new Duration(0));

        Tooltip tooltipCollecteSeule = new Tooltip("Cette Collecte n'est associée à aucune livraison");
        tooltipCollecteSeule.setShowDelay(new Duration(0));
        tooltipCollecteSeule.setHideDelay(new Duration(0));

        // Définit le type de case affiché dans le tableau, et les évènements associés
        // aux cases
        tableauDemandes.setRowFactory(tv -> {
            TableRow<Demande> ligne = new TableRow<Demande>() {
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
                 * Méthode qui se lance à chaque fois qu'une ligne du tableau est mise à jour.
                 * On y définit la couleur des cases, et les tooltip d'erreur à afficher si
                 * nécessaire
                 * 
                 * @param demande L'objet demande associé à la ligne mise à jour
                 */
                protected void doUpdateItem(Demande demande) {
                    if (demande != null) {
                        Map<Requete, Color> mapCouleur = fenetre.getMapCouleurRequete();
                        Color couleur = mapCouleur.get(demande.getRequete());
                        if (getChildren().size() > 0) {
                            ((Cell) getChildren().get(0)).setTextFill(couleur);

                            if (getChildren().size() > 3) {
                                Cell texteCase = (Cell) getChildren().get(4);
                                texteCase.setTextFill(Color.RED);
                                Boolean inversee = false;

                                Demande demandeAssociee;
                                if (demande.getTypeDemande() == TypeDemande.COLLECTE) {
                                    demandeAssociee = demande.getRequete().getDemandeLivraison();
                                } else {
                                    demandeAssociee = demande.getRequete().getDemandeCollecte();
                                }
                                if (demandeAssociee != null) {
                                    int indexDemandeAssociee = tableauDemandes.getItems().indexOf(demandeAssociee);

                                    if ((demande.getTypeDemande() == TypeDemande.COLLECTE
                                            && this.getIndex() > indexDemandeAssociee)
                                            || (demande.getTypeDemande() == TypeDemande.LIVRAISON
                                                    && this.getIndex() < indexDemandeAssociee)) {
                                        inversee = true;

                                    }
                                }
                                if (demande.getTypeDemande() == TypeDemande.COLLECTE) {
                                    if (inversee) {
                                        texteCase.setText(" ! ");
                                        Tooltip.install(texteCase, tooltipCollecteLivraisonInversee);

                                    } else {
                                        texteCase.setText(demande.getProprieteOrpheline().get());
                                        if (demande.getProprieteOrpheline().get() != null) {

                                            Tooltip.install(texteCase, tooltipCollecteSeule);
                                        } else {
                                            Tooltip.uninstall(texteCase, tooltipCollecteSeule);
                                            Tooltip.uninstall(texteCase, tooltipCollecteLivraisonInversee);
                                        }
                                    }

                                } else if (demande.getTypeDemande() == TypeDemande.LIVRAISON) {
                                    if (inversee) {
                                        texteCase.setText(" ! ");

                                        Tooltip.install(texteCase, tooltipLivraisonCollecteInversee);

                                    } else {
                                        texteCase.setText(demande.getProprieteOrpheline().get());
                                        if (demande.getProprieteOrpheline().get() != null) {

                                            Tooltip.install(texteCase, tooltipLivraisonSeule);
                                        } else {
                                            Tooltip.uninstall(texteCase, tooltipLivraisonSeule);
                                            Tooltip.uninstall(texteCase, tooltipLivraisonCollecteInversee);
                                        }
                                    }
                                }
                            }
                            setTextFill(couleur);

                            // On met à jour la date d'arrivée, la date de départ et le nom

                            if (demande.getProprieteDateArrivee() != null && getChildren().size() == 5) {
                                Cell caseDateArrivee = (Cell) getChildren().get(1);
                                caseDateArrivee.setText(demande.getProprieteDateArrivee().get());
                            }
                            if (demande.getProprieteDateDepart() != null && getChildren().size() == 5) {
                                Cell caseDateDepart = (Cell) getChildren().get(2);
                                caseDateDepart.setText(demande.getProprieteDateDepart().get());
                            }
                            if (demande.getProprieteNomIntersection() != null && getChildren().size() == 5) {
                                Cell caseNom = (Cell) getChildren().get(3);
                                caseNom.setText(demande.getProprieteNomIntersection().get());
                            }

                        }
                    } else {
                        if (getChildren().size() > 3) {
                            Cell texteCase = (Cell) getChildren().get(4);
                            texteCase.setText("");
                            Tooltip.uninstall(texteCase, tooltipLivraisonSeule);
                            Tooltip.uninstall(texteCase, tooltipLivraisonCollecteInversee);
                        }
                    }
                }
            };

            // On ajoute l'entrée <index, ligne> dans la map
            this.mapIndexLignes.put(ligne.getIndex(), ligne);

            // Evènement clic droit sur une case du tableau appelant le menu contextuel
            ligne.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
                @Override
                public void handle(ContextMenuEvent event) {
                    if (ligne.getItem() == null || !menuContextuelAffichable) {
                        return;
                    }
                    if (fenetre.getControleur().getDemandeSelectionnee() != ligne.getItem()) {
                        fenetre.getControleur().selectionnerDemande(ligne.getItem());
                        menuContextuel.show(ligne, event.getScreenX(), event.getScreenY());
                    } else {
                        menuContextuel.show(ligne, event.getScreenX(), event.getScreenY());
                    }
                }
            });

            // Evènement d'une ligne du tableau cliquée et déplacée
            ligne.setOnDragDetected(event -> {
                if (!ligne.isEmpty() && draggable) {
                    Integer index = ligne.getIndex();

                    indexDepartDemande = index;

                    Dragboard db = ligne.startDragAndDrop(TransferMode.MOVE);
                    db.setDragView(ligne.snapshot(null, null));
                    ClipboardContent cc = new ClipboardContent();

                    cc.put(SERIALIZED_MIME_TYPE, index);
                    db.setContent(cc);

                    event.consume();
                }
            });

            // Evènement d'une ligne du tableau déplacée dessus une autre
            ligne.setOnDragOver(event -> {
                Dragboard db = event.getDragboard();
                if (db.hasContent(SERIALIZED_MIME_TYPE) && draggable) {
                    if (ligne.getIndex() != ((Integer) db.getContent(SERIALIZED_MIME_TYPE)).intValue()) {
                        event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                        this.fenetre.getVueTextuelle().rechargerHighlight();
                        ligne.setStyle("-fx-background-color:GREY");
                        event.consume();
                    }
                }
            });

            ligne.setOnDragExited(event -> {
                Dragboard db = event.getDragboard();
                if (db.hasContent(SERIALIZED_MIME_TYPE) && draggable) {
                    if (ligne.getIndex() != ((Integer) db.getContent(SERIALIZED_MIME_TYPE)).intValue()) {
                        this.tableauDemandes.getSelectionModel().clearSelection();
                        ligne.setStyle("-fx-background-color:WHITE");
                        ligne.setStyle("-fx-text-fill:black");
                        this.fenetre.getVueTextuelle().rechargerHighlight();

                        ligne.setTextFill(Color.BLACK);
                        event.consume();
                    }
                }
            });

            // Evènement d'une ligne du tableau lachée sur une autre
            ligne.setOnDragDropped(event -> {
                Dragboard db = event.getDragboard();
                if (db.hasContent(SERIALIZED_MIME_TYPE) && draggable) {
                    int indexDragged = (Integer) db.getContent(SERIALIZED_MIME_TYPE);
                    Demande demandeDragged = tableauDemandes.getItems().remove(indexDragged);
                    int indexDrop;

                    if (ligne.isEmpty())
                        indexDrop = tableauDemandes.getItems().size();
                    else
                        indexDrop = ligne.getIndex();

                    indexArriveeDemande = indexDrop;
                    tableauDemandes.getItems().add(indexDrop, demandeDragged);
                    event.setDropCompleted(true);
                    this.tableauDemandes.getSelectionModel().clearSelection();
                    ligne.setStyle("-fx-background-color:WHITE");
                    ligne.setStyle("-fx-text-fill:black");
                    tableauDemandes.getSelectionModel().select(indexDrop);
                    this.fenetre.getVueTextuelle().modifierPlanning(indexDepartDemande, indexArriveeDemande);
                    this.fenetre.getVueTextuelle().rechargerHighlight();
                    event.consume();

                }
            });

            // Evènement du click de souris
            ligne.setOnMouseClicked(event -> {
                if (event.getButton() != MouseButton.SECONDARY) {
                    if (ligne.getItem() != null)
                        fenetre.getControleur().selectionnerDemande(ligne.getItem());
                }
                this.tableauDemandes.getSelectionModel().clearSelection();
            });

            return ligne;
        });

        colonneType.setPrefWidth(colonneType.getPrefWidth() + 1);
    }

    /**
     * @return Le tableau tableauDemandes
     */
    public TableView<Demande> getTableauDemandes() {
        return tableauDemandes;
    }

    /**
     * Change la valeur du tableau de demandes
     * 
     * @param tableauDemandes Le nouveau tableau de demandes
     */
    public void setTableauDemandes(TableView<Demande> tableauDemandes) {
        this.tableauDemandes = tableauDemandes;
    }

    /**
     * @return La colonne du type
     */
    public TableColumn<Demande, String> getColonneType() {
        return colonneType;
    }

    /**
     * Change la valeur de la colonne du type
     * 
     * @param colonneType La nouvelle colonne du type
     */
    public void setColonneType(TableColumn<Demande, String> colonneType) {
        this.colonneType = colonneType;
    }

    /**
     * @return La colonne de l'arrivée
     */
    public TableColumn<Demande, String> getColonneArrivee() {
        return colonneArrivee;
    }

    /**
     * Change la valeur de la colonne de l'arrivée
     * 
     * @param colonneArrivee La nouvelle colonne de l'arrivée
     */
    public void setArriveeColumn(TableColumn<Demande, String> colonneArrivee) {
        this.colonneArrivee = colonneArrivee;
    }

    /**
     * @return La colonne de départ
     */
    public TableColumn<Demande, String> getColonneDepart() {
        return colonneDepart;
    }

    /**
     * Change la valeur de la colonne de départ
     * 
     * @param colonneDepart La nouvelle colonne de départ
     */
    public void setColonneDepart(TableColumn<Demande, String> colonneDepart) {
        this.colonneDepart = colonneDepart;
    }

    /**
     * @return La colonne de l'adresse
     */
    public TableColumn<Demande, String> getColonneAdresse() {
        return colonneAdresse;
    }

    /**
     * Change la valeur de la colonne de l'adresse
     * 
     * @param colonneAdresse La nouvelle colonne de l'adresse
     */
    public void setColonneAdresse(TableColumn<Demande, String> colonneAdresse) {
        this.colonneAdresse = colonneAdresse;
    }

    /**
     * Change la valeur de la fenêtre
     * 
     * @param fenetre La nouvelle fenêtre
     */
    public void setFenetre(Fenetre fenetre) {
        this.fenetre = fenetre;
    }

    /**
     * Définit si les colonnes peuvent être glissées/déposées.
     * 
     * @param draggable
     */
    public void setDraggable(boolean draggable) {
        this.draggable = draggable;
    }

    /**
     * @return True si les colonnes peuvent être glissées/déposées, false sinon.
     */
    public Boolean getDraggable() {
        return draggable;
    }

    /**
     * @return Le menu contextuel
     */
    public ContextMenu getMenuContextuel() {
        return menuContextuel;
    }

    /**
     * @return La map faisant le lien entre les lignes du tableau et leur index
     */
    public Map<Integer, TableRow<Demande>> getMapIndexLignes() {
        return mapIndexLignes;
    }

    /**
     * @return La colonne de la propriété orpheline
     */
    public TableColumn<Demande, String> getColonneOrpheline() {
        return colonneOrpheline;
    }

    /**
     * @return La liste observable listeDemandes
     */
    public ObservableList<Demande> getListeDemandes() {
        return listeDemandes;
    }

    /**
     * Change la valeur de la liste observable listeDemandes
     *
     * @param listeDemandes La nouvelle liste
     */
    public void setListeDemandes(ObservableList<Demande> listeDemandes) {
        this.listeDemandes = listeDemandes;
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
     * Définit si le menu contextuel peut s'afficher ou non
     * 
     * @param visible Booléen définissant si le menu contextuel peut s'afficher
     */
    public void setMenuContextuelAffichable(boolean visible) {
        this.menuContextuelAffichable = visible;
    }

    /**
     * @return True si le menu contextuel peut s'afficher, false sinon
     */
    public boolean getMenuContextuelAffichable() {
        return this.menuContextuelAffichable;
    }
}
