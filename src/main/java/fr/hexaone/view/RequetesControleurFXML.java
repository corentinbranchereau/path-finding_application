package fr.hexaone.view;

import fr.hexaone.model.Demande;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

public class RequetesControleurFXML {

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

    @FXML
    public void initialize() {
        // Initialize the person table with the two columns.
        typeColumn.setCellValueFactory(cellData -> cellData.getValue().getType());
        typeColumn.setSortable(false);

        arriveeColumn.setCellValueFactory(cellData -> cellData.getValue().getDateArrivee());
        arriveeColumn.setSortable(true);

        departColumn.setCellValueFactory(cellData -> cellData.getValue().getDateDepart());
        departColumn.setSortable(false);

        adresseColumn.setCellValueFactory(cellData -> cellData.getValue().getNomIntersection());
        adresseColumn.setSortable(false);

        demandeTable.setRowFactory(tv -> {
            TableRow<Demande> row = new TableRow<>();

            row.setOnDragDetected(event -> {
                if (!row.isEmpty()) {
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
                if (db.hasContent(SERIALIZED_MIME_TYPE)) {
                    if (row.getIndex() != ((Integer) db.getContent(SERIALIZED_MIME_TYPE)).intValue()) {
                        event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                        event.consume();
                    }
                }
            });

            row.setOnDragDropped(event -> {
                Dragboard db = event.getDragboard();
                if (db.hasContent(SERIALIZED_MIME_TYPE)) {
                    int draggedIndex = (Integer) db.getContent(SERIALIZED_MIME_TYPE);
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
                    event.consume();
                }
            });

            return row;
        });

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

}
