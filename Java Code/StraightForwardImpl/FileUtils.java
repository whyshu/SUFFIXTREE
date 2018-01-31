import javax.xml.soap.Text;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class FileUtils {
    public String readText(String TextFileName) {
        String text;
        try (BufferedReader br = Files.newBufferedReader(Paths.get(TextFileName))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }
            //System.out.println("Given text :: "+sb.toString().toLowerCase());
            return sb.toString().toLowerCase();
        } catch (IOException e) {
            System.out.println("Exception in reading the string from string.txt :: "
                    + e.fillInStackTrace());
        }
        return "";
    }

    public HashMap<String, String> putInHashMap(String PatternFileName) {
        String cntOfPatterns = null;
        //Declare a linked hashmap to maintain the order
        HashMap<String, String> patternHashMap = new LinkedHashMap<>();
        //Read the file line by line
        try (BufferedReader br = Files.newBufferedReader(Paths.get(PatternFileName))) {
            int flag = 0;
            for (String line = null; (line = br.readLine()) != null; ) {
                if (flag == 1) {
                    //Populate the hashmap with the same key and value pair
                    patternHashMap.put(line.toLowerCase(), line.toLowerCase());
                } else {
                    cntOfPatterns = line;
                    flag = 1;
                }
            }
        } catch (IOException e) {
            System.out.println("Exception in populating the hashmap :: " + e.fillInStackTrace());
        }
        //System.out.println("Count of Patterns :: " + cntOfPatterns);
        //printMap(patternHashMap);
        //return hashmap
        return patternHashMap;
    }


    //To print the map given
    private void printMap(HashMap mapToPrint) {
        mapToPrint.forEach((key, value) -> {
            System.out.println(key.toString() + "    " + value.toString());
        });
    }

    //Create the misspelt words.txt file
    public BufferedWriter createFile(String FileName){
        try  {
            FileWriter fw = new FileWriter(FileName);
            BufferedWriter writer = new BufferedWriter(fw);
            return writer;
        }
        catch(IOException e){
            System.out.println("Exception in creating output file :: "+e.fillInStackTrace());
        }
        return null;
    }
    //Write to output file
    public void writeToOutputFile(BufferedWriter writer,int positionOfSubString)  {
        try {
            writer.write(String.valueOf(positionOfSubString));
            writer.newLine();
        }
        catch (IOException e) {
            System.out.println("Exception in writing to output file :: "+e.fillInStackTrace());
        }
    }
}