import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class SuffixTreePatternMatchMain {
        public static void main(String[] args) {
            FileUtils fileUtilsObj=new FileUtils();
            String textToCheck=fileUtilsObj.readText("string.txt");
            SuffixTree stObj = new SuffixTree(textToCheck.toCharArray());
            LinkedHashMap patternHashMap=fileUtilsObj.putInHashMap("patterns.txt");
            stObj.construct();
            //traverse after build
            stObj.traverseAfterBuild(patternHashMap);
            long startTime = System.currentTimeMillis();
            try {
                //Traverse the hashmap to check patterns matching
                stObj.traverseSuffixTree(patternHashMap,textToCheck);
            }catch (IOException e){
                System.out.println("Exception in writing to output file :: "+e.fillInStackTrace());
            }
            long endTime   = System.currentTimeMillis();
            long totalTime = endTime - startTime;
            System.out.println("Time taken to execute :: "+totalTime+" ms");

        }
}
