import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Movable extends Application {

    protected class CircleGraph {
        ObservableList<Node> nodes;
        List<Circle> circles;
        List<Line> lines;
        List<Text> labels;

        public CircleGraph(ObservableList<Node> nodes) {
            this.nodes = nodes;
            // iteration by index
            circles = new ArrayList<>();
            // sequential iteration
            lines = new LinkedList<>();
            labels = new LinkedList<>();
        }

        public void addCircle(double x, double y) {
            Circle c = makeCircle(x, y);
            circles.add(c);
            nodes.add(c);
        }

        public void recalculate() {
            nodes.removeAll(lines);
            nodes.removeAll(labels);
            lines.clear();
            labels.clear();

            Object[] c = circles.toArray();
            for (int i = 0; i < c.length; i++) {
              for (int j = i + 1; j < c.length; j++) {
                  Circle left = (Circle)(c[i]);
                  Circle right = (Circle)(c[j]);

                  double lx = left.getCenterX();
                  double ly = left.getCenterY();
                  double rx = right.getCenterX();
                  double ry = right.getCenterY();
                  lines.add(new Line(lx, ly, rx, ry));

                  double dx = lx - rx;
                  double dy = ly - ry;
                  labels.add(new Text(
                          (lx + rx) / 2,
                          (ly + ry) / 2,
                          String.valueOf(Math.round(Math.sqrt(dx * dx + dy * dy)))
                  ));
              }
            }

            // prepend so that circles stay at front
            nodes.addAll(0, lines);
            nodes.addAll(0, labels);
        }

        protected Circle makeCircle(double x, double y) {
            Circle c = new Circle(x, y, 10.);
            c.setOnMouseDragged(e -> {
                e.consume();
                c.setCenterX(e.getX());
                c.setCenterY(e.getY());
                recalculate();
            });
            c.setOnMousePressed(Event::consume);
            c.setOnMouseEntered(e -> c.setRadius(12.));
            c.setOnMouseExited(e -> c.setRadius(10.));
            return c;
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Pane root = new Pane();
        CircleGraph cg = new CircleGraph(root.getChildren());
        cg.addCircle(200, 300);
        cg.addCircle(300, 200);
        cg.addCircle(250, 350);
        cg.recalculate();
        root.setOnMousePressed(e -> {
            cg.addCircle(e.getX(), e.getY());
            cg.recalculate();
        });
        primaryStage.setTitle("Movable Vertices");
        primaryStage.setScene(new Scene(root, 500, 500));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
