import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Task3Extended {

    public static final String ERROR_MSG_NO_ARGS = "Отсутствуют входные параметры. Ожидалось два параметра: path_to_file_with_tests и path_to_file_with_values.";

    //Полноценное решение через сериализацию/десериализацию json - без форматирования json - кто вообще будет форматировать json руками?
    public static void main(String[] args)  {
        if (args.length < 2) {
            throw new IllegalArgumentException(ERROR_MSG_NO_ARGS);
        }
        try {
            Task.TaskConverter converter = new Task.TaskConverter();
            String json = Files.readString(Paths.get(args[0]));
            Map<Long, Task> tasks = converter.getValues(json);
            json = Files.readString(Paths.get(args[1]));
            Map<Long, Task> values = converter.getValues(json);
            Map<Long, Task> result = new HashMap<>(tasks);
            for (Map.Entry<Long, Task> entry : values.entrySet()) {
                result.get(entry.getKey()).setValue(entry.getValue().getValue());
            }
            Files.write(Paths.get("src/report.json"), converter.toJson("tests", new ArrayList<>(result.values())).getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
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

        public Long getId() {
            return id;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public static class TaskConverter {

            private static final String REGEX_BEGIN = "^\\{,*\\s*\".*\":,*\\s*\\[";
            private static final String REGEX_END = "\\]\\s*\\}$";

            private String removeMainArrayInJson(String json) {
                json = json.replaceFirst(REGEX_BEGIN, "");
                json = json.replaceFirst(REGEX_END, "");
                return json;
            }

            public Map<Long, Task> getValues(String json) {
                Map<Long, Task> taskMap = new HashMap<>();
                json = removeMainArrayInJson(json);
                Pattern pattern = Pattern.compile(generatePattern());
                Matcher matcher = pattern.matcher(json);
                while (matcher.find()) {
                    Task task = fromJson(matcher, taskMap);
                    taskMap.put(task.getId(), task);
                    json = json.replace(matcher.group(), task.getId().toString());
                    matcher = pattern.matcher(json);
                }
                return taskMap;
            }

            public Task fromJson(Matcher matcher, Map<Long, Task> taskMap) {
                Task task = new Task();
                for (Field field : Task.class.getDeclaredFields()) {
                    String fieldValue = matcher.group(field.getName());
                    if (fieldValue == null || fieldValue.isBlank()) {
                        continue;
                    }
                    setFieldValue(task, field, fieldValue, taskMap);
                }
                return task;
            }

            private void setFieldValue(Task task, Field field, String fieldValue, Map<Long, Task> taskMap) {
                try {

                    field.setAccessible(true);
                    Class<?> fieldType = field.getType();
                    if (fieldType == Long.class || fieldType == long.class) {
                        field.set(task, Long.valueOf(fieldValue));
                    } else if (List.class.isAssignableFrom(fieldType)) {
                        List<Task> values = Arrays.stream(fieldValue.split(","))
                                .map(String::trim)
                                .filter(id -> !id.isEmpty())
                                .map(id -> taskMap.get(Long.valueOf(id)))
                                .filter(Objects::nonNull)
                                .toList();
                        field.set(task, values.isEmpty() ? null : values);
                    } else {
                        field.set(task, fieldValue.replace("\"", ""));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                }

            public String toJson(String title, List<Task> tasks) {
                StringBuilder builder = new StringBuilder("{");
                builder.append("\"").append(title).append("\": ");
                StringJoiner joinerField = new StringJoiner(",", "[", "]");
                for (Task task : tasks) {
                    joinerField.add(toJson(task));
                }
                return builder.append(joinerField).append("}").toString();
            }

            public String toJson(Task task){
                try {
                    StringJoiner joinerField = new StringJoiner(",", "{", "}");
                    for (Field field : Task.class.getDeclaredFields()) {
                        field.setAccessible(true);
                        Object value = field.get(task);
                        if (value == null) {
                            continue;
                        }
                        String fieldName = "\"" + field.getName() + "\":";
                        if (Collection.class.isAssignableFrom(field.getType())) {
                            String collectionJson = ((Collection<?>) value).stream()
                                    .map(element -> toJson((Task) element))
                                    .collect(Collectors.joining(",", "[", "]"));
                            joinerField.add(fieldName + collectionJson);
                        } else {
                            String fieldValue = (field.getType() == Long.class || field.getType() == long.class)
                                    ? value.toString()
                                    : "\"" + value + "\"";
                            joinerField.add(fieldName + fieldValue);
                        }
                    }
                    return joinerField.toString();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return "";
            }
        }
    }
}


