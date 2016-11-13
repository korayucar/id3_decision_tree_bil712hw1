package bil712.korayucar.hw1;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Created by koray on 13/11/16.
 */
public class ID3Node {

    Attribute attribute;
    Map<AttributeValue, ID3Node> childNodes = new HashMap<>();
    AttributeValue simpleOutcome;

    boolean hasChild() {
        return childNodes.size() > 0;
    }


    public String toString() {
        if (childNodes.size() == 0)
            return " ──> the outcome is " + simpleOutcome.getDescription();
        else
            return " " +attribute.name.toUpperCase();
    }


    public void print(PrintStream out) {
        print(out, "", true, null);
    }

    private void print(PrintStream out, String prefix, boolean isTail, AttributeValue name) {
        out.println(prefix + (isTail ? "└───────── " : "├───────── ") + (name==null ? "" : name.getDescription())+ toString());
        int i = 0;
        for (Map.Entry<AttributeValue, ID3Node> entry : childNodes.entrySet()) {
            AttributeValue attributeValue = entry.getKey();
            if (i < childNodes.size() - 1)
                entry.getValue().print(out,prefix + (isTail ? "    " : "│   "), false, attributeValue);
            else
                entry.getValue().print(out,prefix + (isTail ? "    " : "│   "), true, attributeValue);
            i++;
        }
    }

    public AttributeValue evaluate(Mapping sample){
        if(childNodes.size() ==0)
            return simpleOutcome;
        Optional<AttributeValue> av =  childNodes.keySet().stream().filter(k -> k.matchesSample(sample.attributes.get(attribute))).findFirst();
        if(av.isPresent())
            return childNodes.get( av.get())
                .evaluate(sample);
        else return null;
    }
}
