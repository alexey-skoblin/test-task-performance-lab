import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Task1 {

    public static void main(String[] args) {
        CircleList circleList = new CircleList(args);
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

        public CircleList(String[] args) {
            if (args.length == 0) {
                throw new IllegalArgumentException(ERROR_MSG_NO_ARGS);
            }
            if (args.length < COUNT_ARGS) {
                throw new IllegalArgumentException(ERROR_MSG_INVALID_ARGS_COUNT);
            }
            try {
                this.n = Integer.valueOf(args[0]);
                this.m = Integer.valueOf(args[1]);
            } catch (Exception e) {
                throw new IllegalArgumentException(ERROR_MSG_INVALID_NUMBER_FORMAT, e);
            }
            if (n < 1 || m < 1) {
                String s = "Параметр " + (n < 1 ? "n = " + n : "m = " + m) + " должен быть больше нуля.";
                throw new IllegalArgumentException(ERROR_MSG_INVALID_NUMBER_VALUE + s);
            }
            list = IntStream.range(1, n + 1)
                    .boxed()
                    .collect(Collectors.toList());
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
