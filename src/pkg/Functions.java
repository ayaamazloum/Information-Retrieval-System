/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg;

import java.io.*;
import java.util.*;
import java.util.Arrays;

/**
 *
 * @author wael Mahfouz
 */
public interface Functions {
    static String readFile(String fileName) throws IOException {
        StringBuilder content = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader(fileName));

        String line;
        while ((line = reader.readLine()) != null) {
            content.append(line).append("\n");
        }

        reader.close();
        return content.toString();
    }
    
    // Save content to file
    static void saveSfxFile(String[] words, String fileName,PrintWriter writer) throws IOException {
        for (String word : words) {
            writer.print(word + "\n");
        }
    }
    
    static void saveStpFile(String[] words, ArrayList<String> stopWords, PrintWriter writer) throws IOException {
        for (String word : words) {
            int res = Arrays.binarySearch(stopWords.toArray(), word);
                if (res < 0)
                    writer.print(word + "\n");
        }       
    }
    
    static String[] removeStopwords(String text) {
        text = text.replaceAll("[\\p{Punct}&&[^.]]", "");//remove all punctuations except dot
        text = text.replaceAll("\\.\\s", " "); //remove dots followed by space only
        text = text.replaceAll("[0-9]",""); //remove numeric values
        String[] words = text.toLowerCase().split("\\s+"); //split white spaces
        return words;
    }
    
    // Apply Porter's stemming algorithm
    static String applyPorterStemming(String text) {
        Stemmer s = new Stemmer();
        for (char c : text.toCharArray()) {
            s.add(c);
        }
        s.stem();
        String stemmed = s.toString();
        return stemmed;
    }
}
