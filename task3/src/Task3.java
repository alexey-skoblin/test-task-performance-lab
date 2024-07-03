import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Task3 {

    public static final String ERROR_MSG_NO_ARGS = "Отсутствуют входные параметры. Ожидалось два параметра: path_to_file_with_tests и path_to_file_with_values.";

    //Короткое решение, которое скорее всего и требовалось
    public static void main(String[] args) {
        if (args.length < 2) {
            throw new IllegalArgumentException(ERROR_MSG_NO_ARGS);
        }
        try {
            String jsonToFileWithTests = Files.readString(Paths.get(args[0]));
            String jsonToFileWithValues = Files.readString(Paths.get(args[1]));
            String jsonToFileWithResults = uniteFiles(jsonToFileWithTests, jsonToFileWithValues);
            Files.write(Paths.get("src/results.json"), jsonToFileWithResults.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static String uniteFiles(String jsonToFileWithTests, String jsonToFileWithValues) {
        Pattern pattern = Pattern.compile(Task.generatePattern());
        Matcher matcher = pattern.matcher(jsonToFileWithValues);
        Map<String, String> values = new HashMap<>();
        while (matcher.find()) {
            values.put(matcher.group("id"), matcher.group("value"));
        }
        matcher = pattern.matcher(jsonToFileWithTests);
        while (matcher.find()) {
            String value = values.get(matcher.group("id"));
            if (value != null) {
                jsonToFileWithTests = jsonToFileWithTests.replace(matcher.group("value"), value);
            }
        }
        return jsonToFileWithTests;

    }

    public static class Task {
        private Long id;
        private String title;
        private String value;
        private List<Task> values;

        public static String generatePattern() {
            Field[] fields = Task.class.getDeclaredFields();
            StringBuilder builder = new StringBuilder("(?<" + Task.class.getSimpleName() + ">");
            StringJoiner joiner = new StringJoiner(",*", "\\{", "\\}");
            String space = "\\s*";
            for (Field field : fields) {
                joiner.add(space + "(?:\"" + field.getName() + "\":");
                if (!Collection.class.isAssignableFrom(field.getType())) {
                    joiner.add(space + "(?<" + field.getName() + ">.*?))*" + space);
                } else {
                    joiner.add(space + "\\[(?<" + field.getName() + ">.*?)\\])*" + space);
                }
            }
            builder.append(joiner).append(")");
            return builder.toString();
        }
    }
}