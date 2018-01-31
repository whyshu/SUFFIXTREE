import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class LinearSearch {
    //Check if the given pattern matches
    public boolean checkPattern(BufferedWriter writer, FileUtils fileUtilsObj, char[] patternArr, char[] textArr) {
        for (int i = 0; i < textArr.length - patternArr.length + 1; i++) {
            for (int j = 0; j < patternArr.length; j++) {
                if (textArr[i + j] == patternArr[j]) {
                    if (j == patternArr.length) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void traverseHashMap(HashMap patternHashMap, String text) throws IOException {
        FileUtils fileUtilsObj = new FileUtils();
        String opFileName = "Output.txt";
        BufferedWriter writer = fileUtilsObj.createFile(opFileName);
        //Read the input files and populate the wordlist and hashmap
        //For every pattern in the hashmap
        patternHashMap.forEach((wordKey, wordValue) -> {
            char[] textArr = text.toCharArray();
            char[] patternArr = wordValue.toString().toCharArray();
            if (checkPattern(writer, fileUtilsObj, patternArr, textArr)) {
                fileUtilsObj.writeToOutputFile(writer, getPos(text,wordValue.toString()));
            }else{
                fileUtilsObj.writeToOutputFile(writer, getPos(text,wordValue.toString()));
            }
        });
        writer.close();
    }

//Get the position of the substring
    public int getPos(String text, String pattern) {
        int j=0;
        if (pattern.length() >= 1) {
            for (int i = 0; i < text.length(); i++) {
                if (text.charAt(i) == pattern.toString().charAt(j)) {
                    j++;
                    if (j == pattern.length()) {
                       return i - pattern.length()+1;
                    }
                } else {
                    j = 0;
                }
            }
        }
        return -1;
    }
}