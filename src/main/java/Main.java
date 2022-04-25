import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    private final static String INPUT_FILE_PATH = "src/main/resources/lng.csv";
    private final static String OUTPUT_FILE_PATH = "src/main/resources/output.txt";

    private static Set<Line> uniqueLine = new HashSet<>();
    private static Map<LineParameter, Set<Line>> groupedByParameters = new HashMap<>();
    private static Map<LineParameter, Set<Line>> withoutSingle = new HashMap<>();
    private static Set<Set<Line>> groups = new HashSet<>();

    public static void main(String[] args) {

        long start = System.currentTimeMillis();

        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE_PATH))){

            String line;
            while((line = br.readLine()) != null) {
                checkLine(line);
            }

            groupedByParameters
                    .entrySet()
                    .stream()
                    .filter(entry -> entry.getValue().size() > 1)
                    .forEach(entry -> withoutSingle.put(entry.getKey(), entry.getValue()));

            boolean isAlreadyInGroup;

            for(Set<Line> g1 : withoutSingle.values()) {
                isAlreadyInGroup = false;
                for(Set<Line> g2 : groups) {
                    if(isIntersect(g1, g2)) {
                        g2.addAll(g1);
                        isAlreadyInGroup = true;
                        break;
                    }
                }
                if(!isAlreadyInGroup) {
                    groups.add(g1);
                }
            }
            List<Set<Line>> sortedGroups = groups.stream().sorted(Comparator.comparing(Set::size)).collect(Collectors.toList());
            Collections.reverse(sortedGroups);
            writeToFile(sortedGroups);

            double time = ((double) (System.currentTimeMillis() - start)) / 1000;

            System.out.println(time);
            
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void writeToFile(List<Set<Line>> sortedGroups) {
        int groupNumber = 1;
        StringBuilder builder = new StringBuilder();
        builder.append("Всего групп: ").append(sortedGroups.size()).append("\n");
        for(Set<Line> group : sortedGroups) {
            builder.append("Группа ").append(groupNumber).append("\n");
            for(Line line : group) {
                builder.append(line).append("\n");
            }
            groupNumber++;
        }

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(OUTPUT_FILE_PATH));
            bw.write(builder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean isIntersect(Set<Line> g1, Set<Line> g2) {
        for(Line l : g1) {
            if(g2.contains(l)) {
                return true;
            }
        }
        return false;
    }

    private static void checkLine(String l) {

        String[] lineParams = l.replaceAll("\"", "").split(";", -1);

        if (lineParams.length == 3) {
            Line line = new Line(lineParams);
            if (!line.isAllParametersEmpty() && !uniqueLine.contains(line)) {
                uniqueLine.add(line);
                for (LineParameter parameter : line.getParameters()) {
                    if (!parameter.getValue().isBlank()) {
                        Set<Line> g = groupedByParameters.getOrDefault(parameter, new HashSet<>());
                        g.add(line);
                        groupedByParameters.put(parameter, g);
                    }
                }
            }
        }
    }
}