import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Task2 {

    public static final String ERROR_MSG_NO_ARGS = "Отсутствуют входные параметры. Ожидалось два параметра: path_to_file_with_circle и path_to_file_with_points.";

    public static void main(String[] args) {
        if (args.length < 2) {
            throw new IllegalArgumentException(ERROR_MSG_NO_ARGS);
        }
        Circle circle = Circle.fromFile(args[0]);
        List<Point> points = Point.loadPointsFromFile(args[1]);
        points.forEach(point -> System.out.println(circle.determinePointPosition(point)));
    }

    public static class Point {
        public static final String ERROR_MSG_FILE_NOT_FOUND = "Файл с координатами точек не найден.";
        public static final String ERROR_MSG_INVALID_COORDINATES = "Неверные координаты точки.";
        public static final String ERROR_MSG_INVALID_FORMAT = "Неверный формат данных точки.";

        private final Double x;
        private final Double y;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public static Point fromString(String s) {
            String[] arr = s.split(" ");
            if (arr.length < 2) {
                throw new IllegalArgumentException(ERROR_MSG_INVALID_FORMAT);
            }
            double x, y;
            try {
                x = Double.parseDouble(arr[0]);
                y = Double.parseDouble(arr[1]);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(ERROR_MSG_INVALID_COORDINATES, e);
            }
            return new Point(x, y);
        }

        public static List<Point> loadPointsFromFile(String strPathToFile) {
            Path path = Paths.get(strPathToFile);
            List<Point> points = new ArrayList<>();
            try (Stream<String> stream = Files.lines(path)) {
                stream.filter(s -> !s.isEmpty())
                        .map(String::strip)
                        .map(Point::fromString).forEach(points::add);
            } catch (InvalidPathException | IOException e) {
                throw new IllegalArgumentException(ERROR_MSG_FILE_NOT_FOUND, e);
            }
            return points;
        }

        public Double getX() {
            return x;
        }

        public Double getY() {
            return y;
        }
    }

    public static class Circle {

        public static final String ERROR_MSG_FILE_NOT_FOUND = "Файл с данными окружности не найден или файл имеет не корректный формат.";
        public static final String ERROR_MSG_INVALID_RADIUS = "Неверный радиус окружности.";
        public static final String ERROR_MSG_INVALID_FORMAT = "Неверный формат данных окружности.";

        Point point;
        Double radius;

        public Circle(Point point, Double radius) {
            this.point = point;
            this.radius = radius;
        }

        public static Circle fromFile(String strPathToFile) {
            Point point;
            double radius;
            try {
                Path path = Paths.get(strPathToFile);
                List<String> list = Files.readAllLines(path);
                if (list.size() < 2) {
                    throw new IllegalArgumentException(ERROR_MSG_INVALID_FORMAT);
                }
                point = Point.fromString(list.get(0));
                radius = Double.parseDouble(list.get(1));
            } catch (InvalidPathException | IOException e) {
                throw new IllegalArgumentException(ERROR_MSG_FILE_NOT_FOUND, e);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(ERROR_MSG_INVALID_RADIUS, e);
            }
            return new Circle(point, radius);
        }

        public int determinePointPosition(Point point) {
            double distance = Math.sqrt(Math.pow(point.getX() - this.point.getX(), 2) + Math.pow(point.getY() - this.point.getY(), 2));
            if (distance > this.radius) {
                return 2;
            } else if (distance < this.radius) {
                return 1;
            } else {
                return 0;
            }
        }

    }
}
