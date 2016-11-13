package bil712.korayucar.hw1;

/**
 * Created by koray on 13/11/16.
 */
public class RangeAttributeValue implements AttributeValue {


    double low;
    double high;
    RangeAttributeValue(String representation){
        String[] vals = representation.split("-");
        if(vals.length!=2)
            throw new IllegalArgumentException("attribute value must be a range of floating point numbers two endpoints seperated by '-'");
        low = Double.valueOf(vals[0]);
        high = Double.valueOf(vals[1]);
    }

    @Override
    public String getDescription() {
        return low+"-"+high;
    }

    @Override
    public boolean matchesSample(Value sample) {
        Double val = Double.valueOf(sample.val);
        return low <= val && high>val;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RangeAttributeValue that = (RangeAttributeValue) o;

        if (Double.compare(that.low, low) != 0) return false;
        return Double.compare(that.high, high) == 0;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(low);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(high);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
