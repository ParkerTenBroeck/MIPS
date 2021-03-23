package org.parker.mips.assembler2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PreProcessor {

    private static final Logger LOGGER = Logger.getLogger(PreProcessor.class.getName());



    public static void main(String[] args){

        String regex = "\\b(?:(?<=\")[^\"]*(?=\")|\\w+)\\b";
        String line = "\"hello world\" Alexandros Alex \"I Am\" Something";
        line = "addi $4, $0, width";

        regex = "((\\w{1}[\\w|\\d]+)(\\s+))";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()) {

            System.out.println(matcher.start());
            System.out.println(matcher.group(0));
            System.out.println(matcher.group(1));
            System.out.println(matcher.end());
        }
        if(true)
        return;

        PreProcessFile(new File("C:/Users/parke/OneDrive/Documents/MIPS/Examples/Demos/Random Lines.asm"));
    }

    public static List<Line> PreProcessFile(File file){
        List<Line> loadedLines = PreProcessStage1(file);
        loadedLines = removeFullLineComments(loadedLines);
        loadedLines = removeEmptySpace(loadedLines);
        return loadedLines;
    }

    private static List<Line> PreProcessStage1(File file){

        //List<String> result = new ArrayList<>();
        BufferedReader br = null;

        Scanner s = null;
        ArrayList<Line> list = null;
        try {

            br = new BufferedReader(new FileReader(file));

            String line;

            list = new ArrayList<Line>();
            int index = 0;
            int lineNumber = 0;
            while ((line = br.readLine()) != null) {
                Line currentLine = new Line();
                currentLine.setStartingLine(lineNumber);
                currentLine.setStartingIndex(index);
                //String line = s.next();
                lineNumber++;
                index += line.length();
                currentLine.setLine(line);
                currentLine.setEndingLine(lineNumber);
                currentLine.setEndingIndex(index);
                list.add(currentLine);
            }

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to load file for PreProcessing", e);
            //e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            }catch(IOException e){

            }
        }

        return list;
    }

    private static List<Line> removeFullLineComments(List<Line> list){
        for(int i = list.size() - 1; i >= 0; i -- ){
            Line line = list.get(i);
            line.trim();
            if(line.getLine().startsWith(";")){
                list.remove(i);
            }
        }

        return list;
    }

    private static List<Line> removeEmptySpace(List<Line> list){
        for(int i = list.size() - 1; i >= 0; i -- ){
            list.get(i).trim();
            if(list.get(i).getLine().isEmpty()){
                list.remove(i);
            }
        }
        return list;
    }
}
