import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.function.Function;

public class ButtonImage extends Application {

    protected static final double canvasWidth = 500;
    protected static final double canvasHeight = 500;

    protected static final int UP = 0;
    protected static final int RIGHT = 1;
    protected static final int DOWN = 2;
    protected static final int LEFT = 3;

    @Override
    public void start(Stage primaryStage) throws Exception {

        Image shipImage = new Image("ship.png");
        ImageView ship = new ImageView(shipImage);
        double width = shipImage.getWidth();
        double height = shipImage.getHeight();
        ship.setLayoutX(250 - width / 2);
        ship.setLayoutY(500 - height);

        Function<Integer, Boolean> requestMovement = direction -> {
            double x = ship.getLayoutX();
            double y = ship.getLayoutY();
            switch (direction) {
                case UP:
                    if (y <= 0) {
                        return false;
                    }
                    ship.setLayoutY(y - 5);
                    break;
                case RIGHT:
                    if (x + width >= canvasWidth) {
                        return false;
                    }
                    ship.setLayoutX(x + 5);
                    break;
                case DOWN:
                    if (y + height >= canvasHeight) {
                        return false;
                    }
                    ship.setLayoutY(y + 5);
                    break;
                default:
                    if (x <= 0) {
                        return false;
                    }
                    ship.setLayoutX(x - 5);
                    break;
            }
            return true;
        };

        Pane field = new Pane(ship);
        field.setPrefSize(canvasWidth, canvasHeight);
        field.setStyle("-fx-background-color: black;");

        Button up = new Button("▴");
        Button right = new Button("▸");
        Button down = new Button("▾");
        Button left = new Button("◂");

        up.setOnMouseClicked(e -> requestMovement.apply(UP));
        right.setOnMouseClicked(e -> requestMovement.apply(RIGHT));
        down.setOnMouseClicked(e -> requestMovement.apply(DOWN));
        left.setOnMouseClicked(e -> requestMovement.apply(LEFT));

        GridPane controls = new GridPane();
        controls.setPrefSize(canvasWidth, 100);
        controls.setAlignment(Pos.CENTER);

        controls.add(up, 1, 0);
        controls.add(right, 2, 1);
        controls.add(down, 1, 2);
        controls.add(left, 0, 1);

        Pane root = new Pane(new VBox(field, controls));

        root.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case UP:
                case W:
                    requestMovement.apply(UP);
                    break;
                case RIGHT:
                case D:
                    requestMovement.apply(RIGHT);
                    break;
                case DOWN:
                case S:
                    requestMovement.apply(DOWN);
                    break;
                case LEFT:
                case A:
                    requestMovement.apply(LEFT);
                    break;
                default:
                    break;
            }
        });

        primaryStage.setTitle("Button Image");
        primaryStage.setScene(new Scene(root, canvasWidth, canvasHeight + 100));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
