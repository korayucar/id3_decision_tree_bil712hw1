package bil712.korayucar.hw1;

import java.util.Objects;

/**
 * Created by koray on 13/11/16.
 */
public class StringAttributeValue implements AttributeValue {

    String repr;

    StringAttributeValue(String attr){
        repr = attr;
    }

    @Override
    public String getDescription() {
        return repr;
    }

    @Override
    public boolean matchesSample(Value sample) {
        return sample.val.equals(repr);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StringAttributeValue that = (StringAttributeValue) o;
        return Objects.equals(repr, that.repr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(repr);
    }
}
