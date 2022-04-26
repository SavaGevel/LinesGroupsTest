import java.io.*;
import java.util.*;

public class Main {

    private final static String INPUT_FILE_PATH = "src/main/resources/lng-big.csv";
    private final static String OUTPUT_FILE_PATH = "src/main/resources/output.txt";

    private static Set<Line> uniqueLine = new HashSet<>();
    private static Map<LineParameter, ArrayList<Line>> groupedByParameters = new HashMap<>();
    private static Map<LineParameter, ArrayList<Line>> withoutSingle = new HashMap<>();
//    private static ArrayList<Set<Line>> finalGroups = new ArrayList<>();
    private static Map<Integer, ArrayList<Group>> sortedGroups = new TreeMap<>(Collections.reverseOrder());
    private static Set<Set<Line>> groups = new HashSet<>();

    private static int countGroup = 0;

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

            grouping();
            writeToFile();

            System.out.println(countGroup);

            System.out.println((((double) (System.currentTimeMillis() - start)) / 1000) + " seconds");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void checkLine(String l) {

        String[] lineParams = l.replaceAll("\"", "").split(";", -1);

        if (lineParams.length == 3) {
            Line line = new Line(lineParams);
            if (!line.isAllParametersEmpty() && !uniqueLine.contains(line)) {
                uniqueLine.add(line);
                for (LineParameter parameter : line.getParameters()) {
                    if (parameter.isNotEmpty()) {
                        ArrayList<Line> g = groupedByParameters.getOrDefault(parameter, new ArrayList<>());
                        g.add(line);
                        groupedByParameters.put(parameter, g);
                    }
                }
            }
        }
    }

    private static void grouping() {

        finalGrouping();

        for (Map.Entry<LineParameter, ArrayList<Line>> entry : withoutSingle.entrySet()) {
            ArrayList<Group> v = sortedGroups.getOrDefault(entry.getValue().size(), new ArrayList<>());
            Group group = new Group(entry.getValue());
            v.add(group);
            sortedGroups.put(group.size(), v);
            countGroup++;
        }
    }

    private static void finalGrouping() {
        Map<Line, Integer> lineConnectedParametersCount = new HashMap<>();

        for(Map.Entry<LineParameter, ArrayList<Line>> entry : withoutSingle.entrySet()) {
            for(Line line : entry.getValue()) {
                Integer count = lineConnectedParametersCount.getOrDefault(line, 0);
                lineConnectedParametersCount.put(line, count + 1);
            }
        }

        List<Set<Line>> lineSubGroups = new ArrayList<>();

        for(Line line : lineConnectedParametersCount.keySet()) {
            if(lineConnectedParametersCount.get(line) > 1) {

                Set<Line> lineSubGroup = new HashSet<>();

                for(LineParameter parameter : line.getNotEmptyParameters()) {
                    if(withoutSingle.containsKey(parameter)) {
                        lineSubGroup.addAll(withoutSingle.remove(parameter));
                    }
                }
                if(lineSubGroup.size() > 1) {
                    lineSubGroups.add(lineSubGroup);
                }
            }
        }

        for (Set<Line> lineSubGroup : lineSubGroups) {
            ArrayList<Group> v = sortedGroups.getOrDefault(lineSubGroup.size(), new ArrayList<>());
            Group group = new Group(new ArrayList<>(lineSubGroup));
            v.add(group);
            sortedGroups.put(group.size(), v);
            countGroup++;
        }
    }

    private static void writeToFile() {
        int groupNumber = 1;
        StringBuilder builder = new StringBuilder();
        builder.append("Всего групп: ").append(countGroup).append("\n");
        for(List<Group> groups : sortedGroups.values()) {
            builder.append("Группа ").append(groupNumber).append("\n");
            for(Group group : groups) {
                builder.append(group).append("\n");
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
}