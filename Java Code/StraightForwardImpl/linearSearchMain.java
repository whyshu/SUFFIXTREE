import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.IOException;
import java.util.HashMap;

public class linearSearchMain {
    public static void main(String[] args){
        FileUtils fileUtilsObj=new FileUtils();
        String text=fileUtilsObj.readText("string.txt");
        //take all the words in pattern.txt and
        //put it in a dictionary hashmap
        HashMap patternHashMap=fileUtilsObj.putInHashMap("patterns.txt");
        //first line-number of patterns to be checked
        //for every pattern in the file, check if it is a substring
        LinearSearch linearSearchObj=new LinearSearch();
        long startTime = System.currentTimeMillis();
        try {
            //Traverse the hashmap to check patterns matching
            linearSearchObj.traverseHashMap(patternHashMap, text);
        }catch (IOException e){
            System.out.println("Exception in writing to output file :: "+e.fillInStackTrace());
        }
        long endTime   = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("Time taken to execute :: "+totalTime+" ms");

        //of the main string
        //Output the position of the start of the pattern
    }
}
