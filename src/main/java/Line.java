import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Line {

    private List<LineParameter> parameters = new ArrayList<>();

    public Line(String[] l) {
        parameters.add(new LineParameter(ParameterPosition.FIRST, l[0]));
        parameters.add(new LineParameter(ParameterPosition.FIRST, l[1]));
        parameters.add(new LineParameter(ParameterPosition.FIRST, l[2]));
    }

    public List<LineParameter> getParameters() {
        return parameters;
    }

    public boolean isAllParametersEmpty() {
        for(LineParameter parameter : parameters) {
            if(!parameter.getValue().isBlank()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Line line = (Line) o;
        return Objects.equals(parameters, line.parameters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parameters);
    }

    @Override
    public String toString() {
        return parameters.get(0).getValue() + "; " + parameters.get(1).getValue() + "; " + parameters.get(2).getValue();
    }
}
