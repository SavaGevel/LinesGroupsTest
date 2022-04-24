import line.Line;
import line.LineParameter;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    private final static String INPUT_FILE_PATH = "src/main/resources/lng.csv";
    private final static String OUTPUT_FILE_PATH = "src/main/resources/output.txt";

    public static void main(String[] args) {

        long start = System.currentTimeMillis();

        List<Set<Line>> groups = getDisjointGroupsOfLines();
        writeGroupsToOutputFile(groups);

        double timeInSeconds =((double) (System.currentTimeMillis() - start) ) / 1000;
        System.out.println(timeInSeconds + " seconds");

    }

    private static void writeGroupsToOutputFile(List<Set<Line>> groups) {

        try {
            BufferedWriter br = new BufferedWriter(new FileWriter(OUTPUT_FILE_PATH));
            StringBuilder builder = new StringBuilder();

            builder.append("Всего групп: ").append(groups.size()).append("\n");

            for (int i = 0; i < groups.size(); i++) {
                builder.append("Группа ").append(i + 1).append("\n");
                groups.get(i).forEach(line -> builder.append(line).append("\n"));
            }

            br.write(builder.toString());
            br.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<Set<Line>> getDisjointGroupsOfLines() {

        Map<LineParameter, Set<Line>> groupsOfLinesGroupedByOneParam = getGroupsOfLinesGroupedByOneParam();
        Map<LineParameter, Set<Line>> groupsOfLinesWithoutSingleLineGroups = removeGroupsWithSingleLine(groupsOfLinesGroupedByOneParam);

        List<Set<Line>> groups = unionIntersectedGroups(groupsOfLinesWithoutSingleLineGroups);
        groups = groups.stream().sorted(Comparator.comparing(Set::size)).collect(Collectors.toList());
        Collections.reverse(groups);

        return groups;
    }

    private static List<Set<Line>> unionIntersectedGroups(Map<LineParameter, Set<Line>> groupsOfLinesWithoutSingle) {
        List<Set<Line>> groups = new ArrayList<>();
        boolean isAlreadyInGroup;

        for (Map.Entry<LineParameter, Set<Line>> e1 : groupsOfLinesWithoutSingle.entrySet()) {
            isAlreadyInGroup = false;
            for (Set<Line> g : groups) {
                if (isIntersect(g, e1.getValue())) {
                    g.addAll(e1.getValue());
                    isAlreadyInGroup = true;
                    break;
                }
            }
            if (!isAlreadyInGroup) {
                groups.add(e1.getValue());
            }
        }
        return groups;
    }

    private static Map<LineParameter, Set<Line>> removeGroupsWithSingleLine(Map<LineParameter, Set<Line>> groupsOfLinesGroupedByOneParam) {

        Map<LineParameter, Set<Line>> withoutSingle = new HashMap<>();

        for (LineParameter param : groupsOfLinesGroupedByOneParam.keySet()) {
            Set<Line> lines = groupsOfLinesGroupedByOneParam.get(param);
            if (lines.size() > 1) {
                withoutSingle.put(param, lines);
            }
        }
        return withoutSingle;
    }

    private static Map<LineParameter, Set<Line>> getGroupsOfLinesGroupedByOneParam() {

        Map<LineParameter, Set<Line>> uniqueParams = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(INPUT_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] l = line.replaceAll("\"", "").split(";");
                if (l.length == 3) {
                    Line lineObject = new Line(l);
                    for (LineParameter param : lineObject.getParams()) {
                        if (!param.getValue().isBlank()) {
                            Set<Line> lineList = uniqueParams.getOrDefault(param, new HashSet<>());
                            lineList.add(lineObject);
                            uniqueParams.put(param, lineList);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return uniqueParams;
    }

    private static boolean isIntersect(Set<Line> l1, Set<Line> l2) {
        for(Line line : l1) {
            if(l2.contains(line)) {
                return true;
            }
        }
        return false;
    }


}
