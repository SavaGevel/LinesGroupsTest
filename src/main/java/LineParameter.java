import java.util.Objects;

public class LineParameter {

    private final ParameterPosition position;
    private final String value;
    private boolean notEmpty;

    public LineParameter(ParameterPosition position, String value) {
        this.position = position;
        this.value = value;
        notEmpty = !value.isBlank();
    }

    public boolean isNotEmpty() {
        return notEmpty;
    }

    public ParameterPosition getPosition() {
        return position;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LineParameter that = (LineParameter) o;
        return position == that.position && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, value);
    }
}
