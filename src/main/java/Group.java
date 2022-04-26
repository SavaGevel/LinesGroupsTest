import java.util.List;
import java.util.Objects;

public class Group {

    private List<Line> group;

    public Group(List<Line> group) {
        this.group = group;
    }

    public List<Line> getGroup() {
        return group;
    }

    public int size() {
        return group.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group group1 = (Group) o;
        return group.equals(group1.group);
    }

    @Override
    public int hashCode() {
        return Objects.hash(group);
    }

    @Override
    public String toString() {
        StringBuilder g = new StringBuilder();
        for(Line line : group) {
            g.append(line).append("\n");
        }
        return g.toString();
    }
}
