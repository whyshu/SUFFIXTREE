import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;

class Node{

    public Node(){
    }
    //total character limit
    public static final int maxChar = 256;
    Node[] child = new Node[maxChar];

    //Every node will have a start and final position
    //for every suffix in the string
    int start;
    FinalPos finalPos;

    //To indicate the character position
    int charPos;

    //the link of every suffix node
    Node nodeSL;
    //Create the node
    public static Node createNode(int start, FinalPos finalPos){
        Node node = new Node();
        node.start = start;
        node.finalPos = finalPos;
        return node;
    }
}

class CurrentEdge{
    //This refers to the current active edeg
    //every time we traverse
    CurrentEdge(Node node){
        //the length of the suffix
        currActiveLen = 0;
        //the present node from where start is
        currActiveNode = node;
        //the suffix edge that is active
        currActiveEdge = -1;
    }

    Node currActiveNode;
    int currActiveEdge;
    int currActiveLen;
}

public class SuffixTree {
    //root node refers to the first pos
    public Node rootNode;
    //number of suffix count
    public int numOfSuffixCnt;
    //current edge of suffix
    public CurrentEdge current;
    //final position of the suffix
    public FinalPos finalPos;
    //the given input text characer array
    public char textToCheck[];
    //leaf character taken as S
    public char leafchar = '$';

    //Suffix tree class to build and traverse through it
    public SuffixTree(char textToCheck[]){
        this.textToCheck = new char[textToCheck.length+1];
        for(int i=0; i < textToCheck.length; i++){
            this.textToCheck[i] = textToCheck[i];
        }
        this.textToCheck[textToCheck.length] = leafchar;
    }
    //building a suffix tree-Reference http://www.geeksforgeeks.org/ukkonens-suffix-tree-construction-part-6/
    public void construct(){
        //create the given node
        rootNode = Node.createNode(1, new FinalPos(0));
        //character position for starting character from the root node
        rootNode.charPos = -1;
        //current edge of the rootnode
        current = new CurrentEdge(rootNode);
        //final position of the present suffix
        this.finalPos = new FinalPos(-1);
        //loop through string to start new phase
        for(int i=0; i < textToCheck.length; i++){
            //start checking each character
            startCharCheck(i);
        }
        
       //assign the appropriate position of the suffix
        assignPos(rootNode, 0, textToCheck.length);
    }

    //starts the character check from first to the end
    public void startCharCheck(int i){
        //set the 
        Node internalNodePrev = null;
        //walk till end and add the new character
        finalPos.finalPos++;

        //increment the suffix count to be created
        numOfSuffixCnt++;
        //until it is greater than 0
        while(numOfSuffixCnt > 0){
            //if current length is 0 then look for current character from rootNode.
            if(current.currActiveLen == 0){
                
                if(getNode(i) != null){
                    current.currActiveEdge = getNode(i).start;
                    current.currActiveLen++;
                    break;
                } //If there exists no path, create a new path
                else {
                    rootNode.child[textToCheck[i]] = Node.createNode(i, finalPos);
                    numOfSuffixCnt--;
                }
            } else{
                //If traversing in middle
                try {
                    //get the next character
                    char presentChar = getNextCharacter(i);
                   //check if there exists a character
                    if(presentChar == textToCheck[i]){
                        //Assign the new suffix link
                        if(internalNodePrev != null){
                            internalNodePrev.nodeSL = getNode();
                        }
                        //traverse further with the present character
                        traverseFurther(i);
                        break;
                    }
                    else {
                        //if not same, create a new node
                        Node node = getNode();
                        int oldStart = node.start;
                        node.start = node.start + current.currActiveLen;
                        Node newInternalNode = Node.createNode(oldStart, new FinalPos(oldStart + current.currActiveLen - 1));
                        //create a leaf node from the current node
                        Node newLeafNode = Node.createNode(i, this.finalPos);

                        //assign the child node for every character
                        newInternalNode.child[textToCheck[newInternalNode.start + current.currActiveLen]] = node;
                        newInternalNode.child[textToCheck[i]] = newLeafNode;
                        newInternalNode.charPos = -1;
                        current.currActiveNode.child[textToCheck[newInternalNode.start]] = newInternalNode;
                        if (internalNodePrev != null) {
                            internalNodePrev.nodeSL = newInternalNode;
                        }
                        //set the previous internal node
                        internalNodePrev = newInternalNode;
                        newInternalNode.nodeSL = rootNode;
                        if(current.currActiveNode != rootNode){
                            current.currActiveNode = current.currActiveNode.nodeSL;
                        }
                        else{
                            current.currActiveEdge = current.currActiveEdge  + 1;
                            current.currActiveLen--;
                        }
                        numOfSuffixCnt--;
                    }

                } catch (ExceptionEdge e) {

                    //new character in the final position
                    Node node = getNode();
                    node.child[textToCheck[i]] = Node.createNode(i, finalPos);
                    if (internalNodePrev != null) {
                        internalNodePrev.nodeSL = node;
                    }
                    internalNodePrev = node;
                    //follow the suffix link
                    if(current.currActiveNode != rootNode){
                        current.currActiveNode = current.currActiveNode.nodeSL;
                    }
                    //assign the active ege and length
                    else{
                        current.currActiveEdge = current.currActiveEdge + 1;
                        current.currActiveLen--;
                    }
                    //reduce the number of suffixes to be created
                    numOfSuffixCnt--;
                }
            }
        }
    }

    public void traverseFurther(int charPos){
        Node node = getNode();
      
        if(calcLen(node) < current.currActiveLen){
            current.currActiveNode = node;
            current.currActiveLen = current.currActiveLen - calcLen(node);
            current.currActiveEdge = node.child[textToCheck[charPos]].start;
        }else{
            current.currActiveLen++;
        }
    }
    public Node getNode(){
        return current.currActiveNode.child[textToCheck[current.currActiveEdge]];
    }

    public Node getNode(int charPos){
        return current.currActiveNode.child[textToCheck[charPos]];
    }

    //get the next character given the current character
    public char getNextCharacter(int i) throws ExceptionEdge{
        Node node = getNode();
        //calculate the length of the suffix with the curren len
        if(calcLen(node) >= current.currActiveLen){
            return textToCheck[current.currActiveNode.child[textToCheck[current.currActiveEdge]].start + current.currActiveLen];
        }
        if(calcLen(node) + 1 == current.currActiveLen ){
            if(node.child[textToCheck[i]] != null){
                return textToCheck[i];
            }
        }
        else{
            current.currActiveNode = node;
            current.currActiveLen = current.currActiveLen - calcLen(node) -1;
            current.currActiveEdge = current.currActiveEdge + calcLen(node)  +1;
            return getNextCharacter(i);
        }

        throw new ExceptionEdge();
    }

    public static class ExceptionEdge extends Exception{

    }



    public int calcLen(Node node){
        return node.finalPos.finalPos - node.start;
    }

    //assign the position of the character
    public void assignPos(Node rootNode,int val, int size){
        if(rootNode == null){
            return;
        }

        val += rootNode.finalPos.finalPos - rootNode.start + 1;
        if(rootNode.charPos != -1){
            rootNode.charPos = size - val;
            return;
        }

        for(Node node : rootNode.child){
            assignPos(node, val, size);
        }
    }


    //Traverse the suffix tree
    public void traverseAfterBuild(LinkedHashMap patternHashMap) {
        List<Character> result = new ArrayList<>();
        for (Node node : rootNode.child) {
            traverseAfterBuild(patternHashMap,node, result);
        }
        //sortedMapPrint();
    }

    public void traverseSuffixTree(LinkedHashMap patternHashMap,String textIp) throws IOException {
        FileUtils fileUtilsObj=new FileUtils();
        String opFileName="Output.txt";
        BufferedWriter writer=fileUtilsObj.createFile(opFileName);
        patternHashMap.forEach((key, value) -> {
            List<Character> result = new ArrayList<>();
            //Passing the index in tree edge
            int indexInTreeEdge=0;
            for (Node node : rootNode.child) {
                traverseSuffixTree(node, 0, value.toString(), textIp,indexInTreeEdge);
            }
            //Get the result of traversed edge and the string
            String res = traverseSuffixTree(rootNode, 0, value.toString(), textIp,indexInTreeEdge);
            //Get the traversed edge suffix and match the pattern
            //with the index in the edge
            //Add both to get the final index
            if (res.contains(value.toString())) {
                //Get the index of the string
                fileUtilsObj.writeToOutputFile(writer,indexInTreeEdge+res.indexOf(value.toString()));
            }else{
                fileUtilsObj.writeToOutputFile(writer,-1);
            }
        });
        writer.close();
    }

    int prevPos,currPos;
    LinkedHashMap<Integer,String> suffixHashMap = new LinkedHashMap<>();

    public int edgeLength(Node node) {
        if(node==rootNode)
            return 0;
        return node.finalPos.finalPos - node.start + 1;
    }


    public String traverseEdge(String pattern,Integer idx,Integer start,Integer end,String textIp){

        int k = 0,patternMatchCnt=0;
        for(k=start; k<=end; k++, idx++) {
            if (idx != pattern.length()) {
                if (textToCheck[k] != pattern.charAt(idx)) {
                    // mo match
                    return "";
                }
            }
        }
        if(idx>=pattern.length())
            // complete match
            return textIp;
        // more characters yet to match or check other edge
        //with newly formed string
        String newText = textIp+textToCheck[k] + pattern.charAt(idx);
        return newText;
    }


    public String traverseSuffixTree(Node rootNode,int idx,String pattern,String textIp,Integer indexInTree) {

        if (rootNode == null) {
            return "";
        } else {
            if (rootNode.start != -1) {
                String res = traverseEdge(pattern,idx,rootNode.start,rootNode.finalPos.finalPos,textIp);
                if (res!="") {
                    return res;
                }
                //Get the character index to search
                idx = idx + edgeLength(rootNode);
                for (Node node : rootNode.child) {
                    return traverseSuffixTree(node, idx,pattern,textIp,indexInTree);
                }
            }
        }
        return "";
    }
    //traverse for each character in the tree
    public void traverseAfterBuild(LinkedHashMap patternHashMap,Node rootNode, List<Character> output) {
        //If root node is null, no traversal
        //empty string in tree
        if (rootNode == null) {
            return;
        }
        //if current position is not equal to -1
        if (rootNode.charPos != -1) {
            StringBuilder builder = new StringBuilder(textToCheck.length);
            //from start to end
            for (int i = rootNode.start; i <= rootNode.finalPos.finalPos; i++) {
                output.add(textToCheck[i]);
                builder.append(textToCheck[i]);
            }
            suffixHashMap.put(rootNode.charPos,builder.toString());

            for (int i = rootNode.start; i <= rootNode.finalPos.finalPos; i++) {
                output.remove(output.size() - 1);
            }
            return;
        }

        for (int i = rootNode.start; i <= rootNode.finalPos.finalPos; i++) {
            output.add(textToCheck[i]);
        }
        for (Node node : rootNode.child) {
            traverseAfterBuild(patternHashMap,node, output);
        }
        for (int i = rootNode.start; i <= rootNode.finalPos.finalPos; i++) {
            output.remove(output.size() - 1);
        }
    }
    public void sortedMapPrint(){
        //Create a Treemap of unsortedMap to get it sorted
        Map<Integer,String> sortedMap = new TreeMap<Integer,String>(suffixHashMap);
        sortedMap.forEach((key, value) -> {
            System.out.println(key.toString() + "    " + value.toString());
        });
    }
}


class FinalPos{
    public FinalPos(int finalPos){
        this.finalPos = finalPos;
    }
    int finalPos;
}


