/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.parker.mips.assembler_old.data;

/**
 *
 * @author parke
 */
public class AbstractArgumentList {

    public String[] args;
    public char[] dividers;

    public AbstractArgumentList(String string) {
        abstractifyString(string, new char[]{' ', ',', '(', ')', '[', ']', '{', '}'});
    }

    public AbstractArgumentList(String string, char[] dividingSymbols) {
        abstractifyString(string, dividingSymbols);
    }

    private void abstractifyString(String string, char[] dividingSymbols) {

        int dvCount = 0;
        for (int s = 0; s < string.length(); s++) {
            for (int c = 0; c < dividingSymbols.length; c++) {
                if (string.charAt(s) == dividingSymbols[c]) {
                    dvCount++;
                }
            }
        }
        args = new String[dvCount + 1];
        dividers = new char[dvCount];

        int lastDivide = 0;
        dvCount = 0;
        for (int s = 0; s < string.length(); s++) {
            for (int c = 0; c < dividingSymbols.length; c++) {
                if (string.charAt(s) == dividingSymbols[c]) {
                    args[dvCount] = string.substring(lastDivide, s);
                    dividers[dvCount] = string.charAt(s);
                    lastDivide = s + 1;
                    dvCount++;
                }
            }
        }
        args[dividers.length] = string.substring(lastDivide, string.length());
    }

    /**
     *
     * @param s1
     * @param s2
     *
     * replaces any argument that is equal to s1 with s2
     */
    public void replaceAllFull(String s1, String s2) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals(s1)) {
                args[i] = s2;
            }
        }
    }

    public String buildString() {
        String temp = "";
        for (int i = 0; i < dividers.length; i++) {
            temp += args[i] + dividers[i];
        }
        temp += args[dividers.length];
        return temp;
    }

//    /**
//     *
//     * @param open
//     * @param close
//     * @return
//     *
//     * Warning open and closing chars must be already be used by the dividers
//     * used in the constructor
//     *
//     * if the arguments are not properly enclosed in the specified chars an
//     * error will be thrown
//     */
//    //everything below this point does not work
//    
//        public static void main(String[] args) {
//            String temp = "hello(this(is((a)test))to)see(if)something(works(right))";
//            treeToString(treeFromString(temp, 0, temp.length() - 1));
//        }
//    
//    public String[][] encloseArgsWithChars(char open, char close) {
//
//        Node<AbstractArgumentList> test = new Node();
//        
//        return null;
//    }
//
//  
//// function to construct tree from string 
//public static Node treeFromString(String str, int si, int ei) 
//{ 
//    // Base case 
//    if (si > ei) 
//        return null; 
//  
//    // new root 
//    Node root = new Node();
//    root.data = str.substring(si, str.indexOf("(", si));
//    int index = -1; 
//  
//    // if next char is '(' find the index of 
//    // its complement ')' 
//    if (si + 1 <= ei && str.contains("(")) {
//        //index = findIndex(str, si + 1, ei); 
//        int count = 1;
//        
//        while(count != 0){
//            
//        }
//        index = str.substring(si).indexOf(")");
//    }
//    
//    // if index found 
//    if (index != -1) { 
//  
//        // call for left subtree 
//        root.left = treeFromString(str, si + str.substring(si, str.indexOf("(", si)).length() + 1, index - 1); 
//  
//        // call for right subtree 
//        root.right = treeFromString(str, index + 2, ei - 1); 
//    } 
//    return root; 
//} 
//
//    static String str = "";
//    static void treeToString(Node<String> root) {
//        
//        // bases case 
//        if (root == null) {
//            return;
//        }
//
//        // push the root data as character 
//        str += root.data;
//
//        // if leaf node, then return 
//        if (root.left == null && root.right == null) {
//            return;
//        }
//
//        // for left subtree 
//        str += ('(');
//        treeToString(root.left);
//        //str += (')'); 
//
//        // only if right child is present to 
//        // avoid extra parenthesis 
//        if (root.right != null) {
//            str += (')');
//            treeToString(root.right);
//            //str += (')'); 
//        }
//    }


}



//
//class Node<T> {
//
//    public T data;
//    public Node left, right;
//
//}
