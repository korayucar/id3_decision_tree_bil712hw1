package bil712.korayucar.hw1;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by koray on 13/11/16.
 */
public class ID3 {

    public static ID3Node  id3(List<Mapping> examples , Attribute targetAttribute , List<Attribute> attributes){
        ID3Node node  = new ID3Node();
        //if remaining logic is determined already create single node with the outcome
        for(AttributeValue attr : targetAttribute.attributeValues ){
            if(allMatches(examples , attr)) {
                node.simpleOutcome = attr;
                return node;
            }
        }
        //if there is inconsistency return arbitrary outcome
        if(attributes.size() ==0){
            node.simpleOutcome = targetAttribute.attributeValues.get(0);
            return node;
        }
        //find out the index with the biggest information gain
        double maxGain = -0.1;
        Attribute bestAttribute = null;

        List<Integer> outcomes  = new ArrayList<>();
        for(AttributeValue av : targetAttribute.attributeValues)
            outcomes.add(examples.stream().filter(e->av.equals(e.outcome)).collect(Collectors.toList()).size());
        double entropyOfSamples = -1*entropy(outcomes);


        for(Attribute a : attributes){
            double gain = entropyOfSamples + entropy(examples , targetAttribute,a);
            if(gain > maxGain){
                maxGain = gain;
                bestAttribute = a;
            }
        }
        node.attribute = bestAttribute;
        for(AttributeValue v : bestAttribute.attributeValues){
            final Attribute finalBestAttribute = bestAttribute;
            List<Mapping> ex = examples.stream().filter(sample -> v.matchesSample(sample.attributes.get(finalBestAttribute)))
                    .collect(Collectors.toList());
            if(!ex.isEmpty()) {
                List<Attribute> remainingAttributes = new ArrayList<>(attributes);
                remainingAttributes.remove(bestAttribute);
                node.childNodes.put(v, id3(ex, targetAttribute, remainingAttributes));
            }
        }
        return node;
    }

    private static boolean allMatches(List<Mapping> examples, AttributeValue attr) {
        for(Mapping m : examples)
            if(!attr.equals(m.outcome))
                return false;
        return true;
    }

    private static double entropy(List<Mapping> examples, Attribute targetAttribute,Attribute candidateAttribute){
        double entropy = 0 ;
        for(AttributeValue av : candidateAttribute.attributeValues){
            List<Mapping> ex = examples.stream().filter(e->av.matchesSample(e.attributes.get(candidateAttribute) ) )
                    .collect(Collectors.toList());
            if(ex.size() > 0)
                entropy+= entropy(ex,targetAttribute)*(ex.size())/examples.size();
        }
        return entropy;
    }


    private static double entropy(List<Mapping> examples, Attribute targetAttribute){
        List<Integer> elements = new ArrayList<>();
        for(AttributeValue a: targetAttribute.attributeValues){
            int occurence = 0;
            for(Mapping m : examples){
                if(m.outcome.equals(a))
                    occurence++;
            }
            elements.add(occurence);
        }
        return entropy(elements);
    }

    private static double entropy(List<Integer> elements){
        int sum = 0;
        for(int i :elements)sum+=i;
        if(sum == 0) return 0.0;
        double entr = 0.0;
        for(int i:elements){
            double p = (( (double)i)/sum);
            if(p>0)
                entr +=  p* Math.log(p)/Math.log(2);
        }
        if(entr ==Double.NaN)
            System.err.println(entr);
        return entr;
    }
}
