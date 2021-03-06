package bil712.korayucar.hw1;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by koray on 13/11/16.
 */
public class Main {


    private static String TRAINING_FILE_PATH = "trainingSet.txt";
    private static String FUNCTION_ATTRIBUTES_FILE= "functionAttributes.txt";
    private static String RULES_OUTPUT_FILE_NAME= "rules.txt";
    private static String EVALUATION_SAMPLES= "evaluationSet.txt";

    private static String dataDirectory = "data";

    public static void main(String[] args) throws IOException {
        if(args.length > 0)
            dataDirectory = args[0];
        List<Attribute> attributes = Files.lines(getData(FUNCTION_ATTRIBUTES_FILE)).map(Main::parseFunctionAttribute).collect(Collectors.toList());
        Attribute targetAttribute = attributes.get(0);
        attributes.remove(0);
        List<Mapping> trainningSet = Files.lines(getData(TRAINING_FILE_PATH)).map(s -> parseExample(s, attributes)).collect(Collectors.toList());
        ID3Node root = ID3.id3(trainningSet, targetAttribute, attributes);
        PrintStream writeRules = new PrintStream(new FileOutputStream(RULES_OUTPUT_FILE_NAME));
        root.print(writeRules);
        root.print(System.out);
        List<Mapping> evaluationSet = Files.lines(getData(EVALUATION_SAMPLES)).map(s -> parseExample(s, attributes)).collect(Collectors.toList());
        Long correct = evaluationSet.stream().filter(e -> e.outcome.equals(root.evaluate(e))).collect(Collectors.counting());
        System.out.println(correct + " correct decision out of " + evaluationSet.size() + " samples : %"+( 100 * ((double)correct)/evaluationSet.size()));
    }

    private static Path getData(String filename) {
        return Paths.get(dataDirectory , filename);
    }

    private static Mapping parseExample(String s, List<Attribute> attributes) {
        Mapping mapping = new Mapping();
        String [] params = s.split("\\s+");
        for(int i = 0 ; i < attributes.size() ; i++)
            mapping.attributes.put(attributes.get(i), new Value(params[i+1] ));
        mapping.outcome = new StringAttributeValue(params[0]);
        return mapping;
    }

    private static Attribute parseFunctionAttribute(String s) {
        String [] params = s.split("\\s+");
        int numberOfValues = Integer.valueOf(params[0]);
        Attribute attribute = new Attribute();
        if(s.contains("-")){
            for(int i = 1 ; i <= numberOfValues ; i++)
                attribute.attributeValues.add(new RangeAttributeValue(params[i]));
        }
        else{
            for(int i = 1 ; i <= numberOfValues ; i++)
                attribute.attributeValues.add(new StringAttributeValue(params[i]));
        }
        if(params.length > numberOfValues+1)
            attribute.name = params[numberOfValues+1];
        else
            attribute.name = "";
        return attribute;
    }
}
