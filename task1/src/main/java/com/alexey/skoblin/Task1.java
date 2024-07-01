package com.alexey.skoblin;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Getter
@Setter
public class Task1 {

    public static void main(String[] args) {
        CircleList circleList = new CircleList(args);
        List<Integer> list = circleList.findCircularPathAsList();
        log.atInfo().log("List: {}", list);
    }

    public static class CircleList {

        public static final String ERROR_MSG_NO_ARGS = "Отсутствуют входные параметры. Ожидалось два параметра: n и m.";
        public static final String ERROR_MSG_INVALID_ARGS_COUNT = "Неверное количество входных параметров. Ожидалось два параметра: n и m.";
        public static final String ERROR_MSG_INVALID_NUMBER_FORMAT = "Неверный формат входных параметров. Оба параметра должны быть целыми числами.";
        public static final String ERROR_MSG_INVALID_NUMBER_VALUE = "Неверный формат входных параметров. Параметры n и m должны быть натуральными числами.";
        public static final int COUNT_ARGS = 2;
        @Getter
        private final int n;
        @Getter
        private final int m;
        private final List<Integer> list;

        public CircleList(String[] args) {
            if (args.length == 0) {
                throw new IllegalArgumentException(ERROR_MSG_NO_ARGS);
            }
            if (args.length < COUNT_ARGS) {
                throw new IllegalArgumentException(ERROR_MSG_INVALID_ARGS_COUNT);
            }
            try {
                n = Integer.parseInt(args[0]);
                m = Integer.parseInt(args[1]);
            } catch (Exception e) {
                throw new IllegalArgumentException(ERROR_MSG_INVALID_NUMBER_FORMAT, e);
            }
            if (n < 1 || m < 1) {
                throw new IllegalArgumentException(ERROR_MSG_INVALID_NUMBER_VALUE);
            }
            list = IntStream.range(1, n + 1)
                    .boxed()
                    .collect(Collectors.toList());
        }

        public Integer getElement(int index) {
            return list.get(index % list.size());
        }

        public List<Integer> findCircularPathAsList() {
            List<Integer> result = new ArrayList<>();
            int count = 0;
            do {
                result.add(getElement(count));
                count += m - 1;
            }
            while (!Objects.equals(getElement(count), getElement(0)));
            return result;
        }
    }
}
