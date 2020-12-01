package fr.hexaone.view;

import java.util.Map;
import java.lang.ModuleLayer.Controller;

import fr.hexaone.model.Demande;
import fr.hexaone.model.Requete;
import fr.hexaone.model.TypeIntersection;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.fxml.FXML;
import javafx.scene.control.Cell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.TransferMode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class RequetesControleurFXML {

    /**
     * le tableau
     */
    @FXML
    protected TableView<Demande> demandeTable;
    @FXML
    protected TableColumn<Demande, String> typeColumn;
    @FXML
    protected TableColumn<Demande, String> arriveeColumn;
    @FXML
    protected TableColumn<Demande, String> departColumn;
    @FXML
    protected TableColumn<Demande, String> adresseColumn;

    private static final DataFormat SERIALIZED_MIME_TYPE = new DataFormat("application/x-java-serialized-object");

    protected Fenetre fenetre;

    /**
     * définit si les cases du tableau peuvent être déplacées ou non
     */
    protected Boolean draggable = false;

    /**
     * Méthode qui se lance après le constructeur, une fois les éléments FXML
     * chargés On définit les règles d'affichage du tableau
     */
    @FXML
    public void initialize() {
        // Initialize the person table with the two columns.
        typeColumn.setCellValueFactory(cellData -> cellData.getValue().getTypeProperty());
        typeColumn.setSortable(false);

        arriveeColumn.setCellValueFactory(cellData -> cellData.getValue().getDateArriveeProperty());
        arriveeColumn.setSortable(true);

        departColumn.setCellValueFactory(cellData -> cellData.getValue().getDateDepartProperty());
        departColumn.setSortable(false);

        adresseColumn.setCellValueFactory(cellData -> cellData.getValue().getNomIntersectionProperty());
        adresseColumn.setSortable(false);

        demandeTable.setRowFactory(tv -> {
            TableRow<Demande> row = new TableRow<Demande>() {
                @Override
                public void updateIndex(int i) {
                    super.updateIndex(i);
                    doUpdateItem(getItem());
                }

                @Override
                protected void updateItem(Demande item, boolean empty) {
                    super.updateItem(item, empty);
                    doUpdateItem(item);
                }

                protected void doUpdateItem(Demande item) {
                    if (item != null) {
                        Map<Requete, Color> mapCouleur = fenetre.getMapCouleurRequete();
                        Color couleur = mapCouleur.get(item.getRequete());
                        if (getChildren().size() > 0) {
                            ((Cell) getChildren().get(0)).setTextFill(couleur);
                        }
                        setTextFill(couleur);
                    }
                }
            };

            row.setOnDragDetected(event -> {
                if (!row.isEmpty() && draggable) {
                    Integer index = row.getIndex();

                    Dragboard db = row.startDragAndDrop(TransferMode.MOVE);
                    db.setDragView(row.snapshot(null, null));
                    ClipboardContent cc = new ClipboardContent();
                    cc.put(SERIALIZED_MIME_TYPE, index);
                    db.setContent(cc);

                    event.consume();
                }
            });

            row.setOnDragOver(event -> {
                Dragboard db = event.getDragboard();
                if (db.hasContent(SERIALIZED_MIME_TYPE) && draggable) {
                    if (row.getIndex() != ((Integer) db.getContent(SERIALIZED_MIME_TYPE)).intValue()) {
                        event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                        event.consume();
                    }
                }
            });

            row.setOnDragDropped(event -> {
                Dragboard db = event.getDragboard();
                if (db.hasContent(SERIALIZED_MIME_TYPE) && draggable) {
                    int draggedIndex = (Integer) db.getContent(SERIALIZED_MIME_TYPE);
                    int droppedIndex = row.getIndex();

                    Demande draggedDemande = demandeTable.getItems().get(draggedIndex);
                    Demande draggedDemandeOpposee;
                    if (draggedDemande.getTypeIntersection() == TypeIntersection.COLLECTE) {
                        draggedDemandeOpposee = draggedDemande.getRequete().getDemandeLivraison();
                    } else {
                        draggedDemandeOpposee = draggedDemande.getRequete().getDemandeCollecte();
                    }
                    int draggedIndexOposee = demandeTable.getItems().indexOf(draggedDemandeOpposee);

                    if ((draggedDemande.getTypeIntersection() == TypeIntersection.COLLECTE
                            && droppedIndex < draggedIndexOposee)
                            || (draggedDemande.getTypeIntersection() == TypeIntersection.LIVRAISON
                                    && droppedIndex > draggedIndexOposee)) {
                        Demande draggedPerson = demandeTable.getItems().remove(draggedIndex);

                        int dropIndex;

                        if (row.isEmpty()) {
                            dropIndex = demandeTable.getItems().size();
                        } else {
                            dropIndex = row.getIndex();
                        }
                        demandeTable.getItems().add(dropIndex, draggedPerson);

                        event.setDropCompleted(true);
                        demandeTable.getSelectionModel().select(dropIndex);
                    }
                    event.consume();
                }
            });

            row.setOnMouseClicked(event -> {
                fenetre.controleur.setDemandeSelectionnee(demandeTable.getSelectionModel().getSelectedItem());
            });

            return row;
        });

        typeColumn.setPrefWidth(typeColumn.getPrefWidth() + 1);
        // Listen for selection changes and show the person details when changed.
        // personTable.getSelectionModel().selectedItemProperty()
        // .addListener((observable, oldValue, newValue) ->
        // showPersonDetails(newValue));
    }

    /**
     * @return TableView<Demande> return the demandeTable
     */
    public TableView<Demande> getDemandeTable() {
        return demandeTable;
    }

    /**
     * @param demandeTable the demandeTable to set
     */
    public void setDemandeTable(TableView<Demande> demandeTable) {
        this.demandeTable = demandeTable;
    }

    /**
     * @return TableColumn<Demande, String> return the typeColumn
     */
    public TableColumn<Demande, String> getTypeColumn() {
        return typeColumn;
    }

    /**
     * @param typeColumn the typeColumn to set
     */
    public void setTypeColumn(TableColumn<Demande, String> typeColumn) {
        this.typeColumn = typeColumn;
    }

    /**
     * @return TableColumn<Demande, String> return the arriveeColumn
     */
    public TableColumn<Demande, String> getArriveeColumn() {
        return arriveeColumn;
    }

    /**
     * @param arriveeColumn the arriveeColumn to set
     */
    public void setArriveeColumn(TableColumn<Demande, String> arriveeColumn) {
        this.arriveeColumn = arriveeColumn;
    }

    /**
     * @return TableColumn<Demande, String> return the departColumn
     */
    public TableColumn<Demande, String> getDepartColumn() {
        return departColumn;
    }

    /**
     * @param departColumn the departColumn to set
     */
    public void setDepartColumn(TableColumn<Demande, String> departColumn) {
        this.departColumn = departColumn;
    }

    /**
     * @return TableColumn<Demande, String> return the adresseColumn
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
     * définit la fenetre, mais également associe les items du tableau, et les trie
     * 
     * @param fenetre the fenetre to set
     */
    public void setFenetre(Fenetre fenetre) {
        this.fenetre = fenetre;
        demandeTable.setItems(fenetre.getListeDemandes());
        arriveeColumn.setSortType(TableColumn.SortType.ASCENDING);
        demandeTable.getSortOrder().add(arriveeColumn);
        demandeTable.sort();
        arriveeColumn.setSortable(false);

    }

    /**
     * Set if the columns can be dragged or not
     * 
     * @param draggable
     */
    public void setDraggable(boolean draggable) {
        this.draggable = draggable;
    }

    /**
     * get if the columns are draggable
     * 
     * @return boolean
     */
    public Boolean getDraggable() {
        return draggable;
    }

}
