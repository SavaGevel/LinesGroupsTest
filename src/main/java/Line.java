import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Line {

    private List<LineParameter> parameters = new ArrayList<>();
    private final boolean isAllParametersEmpty;

    public Line(String[] l) {
        parameters.add(new LineParameter(ParameterPosition.FIRST, l[0]));
        parameters.add(new LineParameter(ParameterPosition.SECOND, l[1]));
        parameters.add(new LineParameter(ParameterPosition.THIRD, l[2]));
        isAllParametersEmpty = l[0].isBlank() && l[1].isBlank() && l[2].isBlank();
    }

    public boolean isAllParametersEmpty() {
        return isAllParametersEmpty;
    }

    public List<LineParameter> getParameters() {
        return parameters;
    }

    public List<LineParameter> getNotEmptyParameters() {
        return parameters.stream().filter(LineParameter::isNotEmpty).toList();
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
