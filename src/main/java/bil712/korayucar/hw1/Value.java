package bil712.korayucar.hw1;

/**
 * Created by koray on 13/11/16.
 */
public class Value {
    String val;

    public Value(String param) {
        val = param;
    }

    String getDescription(){
        return val.toString();
    }

}
