import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Task4 {

    public static final String ERROR_MSG_NO_ARGS = "Отсутствует входной параметр. Ожидался один параметр: path_to_file_with_array.";

    public static void main(String[] args) {
        if (args.length < 1) {
            throw new IllegalArgumentException(ERROR_MSG_NO_ARGS);
        }
        Array array = Array.fromFile(args[0]);
        System.out.println(array.findMinMovesToMedian());
    }

    public static class Array {

        public static final String ERROR_MSG_FILE_NOT_FOUND = "Файл с элементами массива не найден.";
        public static final String ERROR_MSG_INVALID_FORMAT = "Неверный формат данных массива.";

        List<Integer> list;

        public Array(List<Integer> list) {
            this.list = new ArrayList<>(list);
        }

        public static Array fromFile(String strPath) {
            List<Integer> list = new ArrayList<>();
            Path path = Path.of(strPath);
            try (Stream<String> lines = Files.lines(path)) {
                lines.map(String::trim)
                        .filter(line -> !line.isEmpty())
                        .forEach(line -> list.add(Integer.valueOf(line)));
            } catch (IOException e) {
                throw new IllegalArgumentException(ERROR_MSG_FILE_NOT_FOUND + strPath, e);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(ERROR_MSG_INVALID_FORMAT + strPath, e);
            }
            return new Array(list);
        }

        public Integer findMedianUsingSort() {
            list.sort(Integer::compareTo);
            if (list.size() % 2 == 1) {
                return list.get(list.size() / 2);
            } else {
                return (list.get(list.size() / 2) + list.get(list.size() / 2 - 1)) / 2;
            }
        }

        public Integer findMedianUsingQuickSelect() {
            return QuickSelect.quickSelect(list, 0, list.size() - 1, list.size() / 2);
        }

        public Integer findMinMovesToMedian() {
            if (list.isEmpty()) {
                return 0;
            }
            //Поиск медианы двумя методами
            //Сортировка и центральный элемент
//            int median = findMedianUsingSort();
            //С помощью QuickSelect
            int median = findMedianUsingQuickSelect();
            int moves = 0;
            for (Integer integer : list) {
                moves += Math.abs(integer - median);
            }
            return moves;
        }

    }

    public static class QuickSelect {

        public static int quickSelect(List<Integer> list, int left, int right, int k) {
            if (left == right) {
                return list.get(left);
            }
            int pivot = list.get(left + (right - left) / 2);
            int i = left;
            int j = right;
            while (i <= j) {
                while (list.get(i) < pivot) {
                    i++;
                }
                while (list.get(j) > pivot) {
                    j--;
                }
                if (i <= j) {
                    int tmp = list.get(i);
                    list.set(i, list.get(j));
                    list.set(j, tmp);
                    i++;
                    j--;
                }
            }
            if (k <= j) {
                return quickSelect(list, left, j, k);
            } else if (i <= k) {
                return quickSelect(list, i, right, k);
            } else {
                return list.get(k);
            }
        }
    }
}
