package fr.hexaone.view;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class RequetesCell extends ListCell<String> {

    private final Text textView = new Text();

    private ObservableList<Text> requetesView;

    public RequetesCell(ObservableList<Text> requetesView) {
        ListCell thisCell = this;
        this.requetesView = requetesView;

        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        setAlignment(Pos.CENTER);

        setOnDragDetected(event -> {
            if (getItem() == null) {
                return;
            }

            ObservableList<String> items = getListView().getItems();

            Dragboard dragboard = startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString(getItem());
            dragboard.setDragView(new Text(getItem()).snapshot(null, null), event.getX(), event.getY());// requetesView.get(items.indexOf(getItem())));
            dragboard.setContent(content);

            event.consume();
        });

        setOnDragOver(event -> {
            if (event.getGestureSource() != thisCell && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }

            event.consume();
        });

        setOnDragEntered(event -> {
            if (event.getGestureSource() != thisCell && event.getDragboard().hasString()) {
                setOpacity(0.3);
            }
        });

        setOnDragExited(event -> {
            if (event.getGestureSource() != thisCell && event.getDragboard().hasString()) {
                setOpacity(1);
            }
        });

        setOnDragDropped(event -> {
            if (getItem() == null) {
                return;
            }

            Dragboard db = event.getDragboard();
            boolean success = false;

            if (db.hasString()) {
                ObservableList<String> items = getListView().getItems();
                int draggedIdx = items.indexOf(db.getString());
                int thisIdx = items.indexOf(getItem());

                if (draggedIdx < thisIdx) {
                    Text temp = requetesView.get(draggedIdx);
                    int currentIdx = draggedIdx;
                    while (currentIdx < thisIdx) {
                        requetesView.set(currentIdx, requetesView.get(currentIdx + 1));
                        items.set(currentIdx, items.get(currentIdx + 1));
                        currentIdx++;
                    }
                    requetesView.set(thisIdx, temp);
                    items.set(thisIdx, db.getString());

                } else if (draggedIdx > thisIdx) {
                    Text temp = requetesView.get(draggedIdx);
                    int currentIdx = draggedIdx;
                    while (currentIdx > thisIdx) {
                        requetesView.set(currentIdx, requetesView.get(currentIdx - 1));
                        items.set(currentIdx, items.get(currentIdx - 1));
                        currentIdx--;
                    }
                    requetesView.set(thisIdx, temp);
                    items.set(thisIdx, db.getString());
                }

                List<String> itemscopy = new ArrayList<>(getListView().getItems());
                getListView().getItems().setAll(itemscopy);

                success = true;
            }
            event.setDropCompleted(success);

            event.consume();
        });

        setOnDragDone(DragEvent::consume);
    }

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setGraphic(null);
        } else {
            textView.setText(requetesView.get(getListView().getItems().indexOf(item)).getText());
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.BASELINE_LEFT);
            System.out.println(textView.getFill());
            textView.setFill(textView.getFill());
            // Create centered Label
            Label label = new Label();
            label.setAlignment(Pos.CENTER_LEFT);

            hBox.getChildren().addAll(label, textView);
            setGraphic(hBox);
        }
    }
}