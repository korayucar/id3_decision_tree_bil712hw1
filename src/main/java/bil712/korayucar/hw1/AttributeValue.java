package bil712.korayucar.hw1;

/**
 * Created by koray on 13/11/16.
 */
public interface AttributeValue {
    String getDescription();

    boolean matchesSample(Value sample);
}
