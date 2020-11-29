package fr.hexaone;

import javafx.application.Application;
import javafx.collections.*;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.util.*;

public class App2 extends Application {

    private ObservableList<String> requetesString = FXCollections.observableArrayList("1", "2", "3", "4", "5", "6",
            "7");

    private ObservableList<Text> requetesView = FXCollections.observableArrayList();

    @Override
    public void start(Stage stage) throws Exception {
        requetesString.forEach(requetes -> requetesView.add(new Text(requetes)));

        ListView<String> requetesList = new ListView<>(requetesString);
        requetesList.setCellFactory(param -> new RequetesCell());
        requetesList.setPrefWidth(180);

        TextFlow layout = new TextFlow(requetesList);
        layout.setPadding(new Insets(10));

        stage.setScene(new Scene(layout));
        stage.show();
    }

    public static void main(String[] args) {
        launch(App2.class);
    }

    private class RequetesCell extends ListCell<String> {
        private final Text imageView = new Text();

        public RequetesCell() {
            ListCell thisCell = this;

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
                imageView.setText(requetesView.get(getListView().getItems().indexOf(item)).getText());
                setGraphic(imageView);
            }
        }
    }

    // Iconset Homepage:
    // http://jozef89.deviantart.com/art/Origami-requetesString-400642253
    // License: CC Attribution-Noncommercial-No Derivate 3.0
    // Commercial usage: Not allowed

}