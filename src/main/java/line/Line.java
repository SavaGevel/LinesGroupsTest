package line;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Line {
    private final List<LineParameter> params;

    public Line(String[] line) {
        params = new ArrayList<>();
        params.add(new LineParameter(ParameterPosition.FIRST, line[0]));
        params.add(new LineParameter(ParameterPosition.SECOND, line[1]));
        params.add(new LineParameter(ParameterPosition.THIRD, line[2]));
    }

    public List<LineParameter> getParams() {
        return params;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Line line = (Line) o;
        return Objects.equals(params, line.params);
    }

    @Override
    public int hashCode() {
        return Objects.hash(params);
    }

    @Override
    public String toString() {
        return params.get(0).getValue() + "; " + params.get(1).getValue() + "; " + params.get(2).getValue();
    }
}
