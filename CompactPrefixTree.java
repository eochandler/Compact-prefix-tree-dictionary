package dictionary;
import java.io.*;

/** CompactPrefixTree class, implements Dictionary ADT and
 *  several additional methods. Can be used as a spell checker.
 *  Fill in code and feel free to add additional methods as needed.
 *  S19 */
public class CompactPrefixTree implements Dictionary {

    private Node root; // the root of the tree

    /** Default constructor  */
    public CompactPrefixTree() { }

    /**
     * Creates a dictionary ("compact prefix tree")
     * using words from the given file.
     * @param filename the name of the file with words
     */
    public CompactPrefixTree(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Adds a given word to the dictionary.
     * @param word the word to add to the dictionary
     */
    public void add(String word) {
        root = add(word.toLowerCase(), root); // Calling private add method
    }

    /**
     * Checks if a given word is in the dictionary
     * @param word the word to check
     * @return true if the word is in the dictionary, false otherwise
     */
    public boolean check(String word) {
        return check(word.toLowerCase(), root); // Calling private check method
    }

    /**
     * Checks if a given prefix is stored in the dictionary
     * @param prefix The prefix of a word
     * @return true if this prefix is a prefix of any word in the dictionary,
     * and false otherwise
     */
    public boolean checkPrefix(String prefix) {
        return checkPrefix(prefix.toLowerCase(), root); // Calling private checkPrefix method
    }

    /**
     * Returns a human-readable string representation of the compact prefix tree;
     * contains nodes listed using pre-order traversal and uses indentations to show the level of the node.
     * An asterisk after the node means the node's boolean flag is set to true.
     * The root is at the current indentation level (followed by * if the node's valid bit is set to true),
     * then there are children of the node at a higher indentation level.
     */
    public String toString() {
        StringBuilder tree = treeToString(root, 0);
        return tree.toString();
    }

    /**
     * Print out the nodes of the tree to a file, using indentations to specify the level
     * of the node.
     * @param filename the name of the file where to output the tree
     */
    public void printTree(String filename) {
        String tree = toString();

        try (PrintWriter pw = new PrintWriter(filename)) {
                    pw.println(tree);
                    pw.flush();
        }
        catch (IOException e) {
            System.out.println("Could not read from the file: " + filename);
        }
    }

    private StringBuilder treeToString(Node node, int numIndentations){

        StringBuilder sb = new StringBuilder();

        if(node == null){
            return sb;
        }

        for(int i = 0; i < numIndentations; i ++){
            sb.append(" ");
        }

        sb.append(node.prefix);

        if(node.isWord == true){
            sb.append("*");
        }
        sb.append("\n");
            numIndentations = numIndentations + 1;
            for (Node i : node.children) {
                sb.append(treeToString(i, numIndentations));
            }
        return sb;
    }

    /**
     * Return an array of the entries in the dictionary that are as close as possible to
     * the parameter word.  If the word passed in is in the dictionary, then
     * return an array of length 1 that contains only that word.  If the word is
     * not in the dictionary, then return an array of numSuggestions different words
     * that are in the dictionary, that are as close as possible to the target word.
     * Implementation details are up to you, but you are required to make it efficient
     * and make good use ot the compact prefix tree.
     *
     * @param word The word to check
     * @param numSuggestions The length of the array to return.  Note that if the word is
     * in the dictionary, this parameter will be ignored, and the array will contain a
     * single world.
     * @return An array of the closest entries in the dictionary to the target word
     */

    public String[] suggest(String word, int numSuggestions) {
        // FILL IN CODE
        // Note: you need to create a private suggest method in this class
        // (like we did for methods add, check, checkPrefix)
        return null; // don't forget to change it
    }

    // ---------- Private helper methods ---------------


    /**
     *  A private add method that adds a given string to the tree
     * @param s the string to add
     * @param node the root of a tree where we want to add a new string
     * @return a reference to the root of the tree that contains s
     */
    private Node add(String s, Node node) {
        if(node == null){

            node = new Node();
            node.prefix = s;
            node.isWord = true;
            return node;
        }

        if(node.prefix.equals(s) & node.isWord == false){
            node.isWord = true;
            return node;
        }

        if(node.prefix.equals(s) & node.isWord == true){
            return node;
        }

        String commonPrefix = inCommonPrefix(node.prefix, s);

            if(commonPrefix.equals(node.prefix)){

                String stringSuffix = findSuffix(s, commonPrefix);

                node.children[whatNumOfAlphabet(firstLetter(stringSuffix))] = add(stringSuffix, node.children[whatNumOfAlphabet(firstLetter(stringSuffix))]);

                return node;
            }


            if(commonPrefix.equals(s)){

                Node newNode3 = new Node();
                newNode3.prefix = commonPrefix;
                node.prefix = findSuffix(node.prefix, commonPrefix);
                newNode3.children[whatNumOfAlphabet(firstLetter(node.prefix))] = node;
                newNode3.isWord = true;
                return newNode3;
            }

            Node newNode = new Node();

            newNode.prefix = commonPrefix;

            newNode.isWord = false;

            String nodeSuffix = findSuffix(node.prefix, commonPrefix);

            node.prefix = nodeSuffix;

            newNode.children[whatNumOfAlphabet(firstLetter(nodeSuffix))] = node;

            String sSuffix = findSuffix(s, commonPrefix);

            newNode.children[whatNumOfAlphabet(firstLetter(sSuffix))] = add(sSuffix, newNode.children[whatNumOfAlphabet(firstLetter(sSuffix))]);

            return newNode;


    }



    /** A private method to check whether a given string is stored in the tree.
     *
     * @param s the string to check
     * @param node the root of a tree
     * @return true if the prefix is in the dictionary, false otherwise
     */
    private boolean check(String s, Node node) {

        if(node == null){
            return false;
        }
        
        if(node.prefix.equals(s) & node.isWord == false){
            return false;
        }

        if(node.prefix.equals(s) & node.isWord == true){
            return true;
        }

        String suffix = findSuffix(s,node.prefix);

        return check(suffix, node.children[whatNumOfAlphabet(firstLetter(suffix))]);
    }

    /**
     * A private recursive method to check whether a given prefix is in the tree
     *
     * @param prefix the prefix
     * @param node the root of the tree
     * @return true if the prefix is in the dictionary, false otherwise
     */
    private boolean checkPrefix(String prefix, Node node) {
        //Base case 1, the tree is empty
        if(node == null){
            System.out.println("null tree");
            return false;
        }


        //check if word starts with string
        if(node.prefix.startsWith(prefix)){
            return true;
        }

        //just looking for prefix not boolean true in this case??? DOUBLE CHECK
        if((node.prefix.equals(prefix))){
            return true;
        }

        //i remember you saying to find the suffix and keep passing it to this function until it works out ok
        String suffix = findSuffix(prefix, node.prefix);

        if(suffix.equals("")){
            return false;
        }
        return checkPrefix(suffix, node.children[whatNumOfAlphabet(firstLetter(suffix))]);

    }

    // Add a private suggest method. Decide which parameters it should have

    // --------- Private class Node ------------
    // Represents a node in a compact prefix tree
    private class Node {
        String prefix; // prefix stored in the node
        Node children[]; // array of children (26 children)
        boolean isWord; // true if by concatenating all prefixes on the path from the root to this node, we get a valid word

        Node() {
            isWord = false;
            prefix = "";
            children = new Node[26]; // initialize the array of children
        }

    }
    /**
     * Finds the suffix of a words after a given prefix
     *
     * @param prefix the prefix
     * @param word the word to have a prefix removed from it
     * @return String the suffix of the word
     */
    public String findSuffix(String word, String prefix){
        if (word != null && prefix != null && word.startsWith(prefix)){
            return word.substring(prefix.length());
            }
        return "prefix is not inside that word";
    }

    /**
     * Finds what numeric equivalent of a letter in the alphabet
     *
     * @return int letter of the alphabet
     */
    public int whatNumOfAlphabet(char c){
        int index = (int) c - (int) 'a';
        return index;
    }

    /**
     * A method that find the first letter of a word
     *
     * @param word the word
     * @return char
     */
    public char firstLetter(String word){
        return word.charAt(0);
    }

    /**
     * A private recursive method to check whether a given prefix is in the tree
     *
     * @param word1 string word
     * @param word2 string word to compare to
     * @return string of letters two words have in common
     */
    public String inCommonPrefix(String word1, String word2){

        char[] w1 = word1.toCharArray();
        char[] w2 = word2.toCharArray();

        int count = 0;

        int w1length = w1.length;
        int w2length = w2.length;

        int smallLength;

        if(w1length <= w2length){
            smallLength = w1length;
        }
        else{
            smallLength = w2length;
        }
        int i = 0;
        String lettersInCommon = "";
        while(i < smallLength){
            if(w1[i] == w2[i]){
                lettersInCommon = lettersInCommon + Character.toString(w1[i]);
                i ++;
            }
            else{
                break;
            }
        }

        return lettersInCommon;
    }

}
