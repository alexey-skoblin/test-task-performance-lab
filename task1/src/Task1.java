import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Task1 {

    public static void main(String[] args) {
        CircleList circleList = CircleList.fromConsole(args);
        List<Integer> list = circleList.findCircularPathAsList();
        StringBuilder stringBuilder = new StringBuilder();
        list.forEach(stringBuilder::append);
        System.out.print(stringBuilder);
    }

    public static class CircleList {

        public static final String ERROR_MSG_NO_ARGS = "Отсутствуют входные параметры. Ожидалось два параметра: n и m.";
        public static final String ERROR_MSG_INVALID_ARGS_COUNT = "Неверное количество входных параметров. Ожидалось два параметра: n и m.";
        public static final String ERROR_MSG_INVALID_NUMBER_FORMAT = "Неверный формат входных параметров. Оба параметра должны быть целыми числами.";
        public static final String ERROR_MSG_INVALID_NUMBER_VALUE = "Неверный формат входных параметров. Параметры n и m должны быть положительными.";
        public static final int COUNT_ARGS = 2;
        private final Integer n;
        private final Integer m;
        private final List<Integer> list;

        public CircleList(int n, int m) {
            if (n < 1 || m < 1) {
                String s = "Параметр " + (n < 1 ? "n = " + n : "m = " + m) + " должен быть больше нуля.";
                throw new IllegalArgumentException(ERROR_MSG_INVALID_NUMBER_VALUE + s);
            }
            this.n = n;
            this.m = m;
            this.list = IntStream.range(1, this.n + 1)
                    .boxed()
                    .collect(Collectors.toList());
        }

        public static CircleList fromConsole(String... args) {
            if (args.length == 0) {
                throw new IllegalArgumentException(ERROR_MSG_NO_ARGS);
            }
            if (args.length < COUNT_ARGS) {
                throw new IllegalArgumentException(ERROR_MSG_INVALID_ARGS_COUNT);
            }
            int n, m;
            try {
                n = Integer.parseInt(args[0]);
                m = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(ERROR_MSG_INVALID_NUMBER_FORMAT, e);
            }
            return new CircleList(n, m);
        }

        public Integer getElement(int index) {
            return list.get(index % list.size());
        }

        public int getM() {
            return m;
        }

        public List<Integer> findCircularPathAsList() {
            List<Integer> result = new ArrayList<>();
            int count = 0;
            do {
                result.add(getElement(count));
                count += getM() - 1;
            }
            while (!Objects.equals(getElement(count), getElement(0)));
            return result;
        }
    }
}
