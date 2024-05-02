/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg;
import java.io.*;

/**
 *
 * @author wael Mahfouz
 */

public class Stemming implements Functions{
    // Porter's stemming algorithm implementation
    // (You can find a Java implementation online)
    // For example, you can use the one provided by Martin Porter: https://tartarus.org/martin/PorterStemmer/java.txt
    private String inputFileName , outputFileName;
   
    public Stemming(String inputFileName , String outputFileName){
        this.inputFileName = inputFileName;
        this.outputFileName = outputFileName;
    }
    
    public void run(){
        try {
            String content = Functions.readFile(inputFileName);
            
            String input[] = content.split("\\r?\\n|\\r");
            PrintWriter writer = new PrintWriter(outputFileName);
            String stemmedText[] = new String[input.length];
            for(int i=0; i<input.length;i++)
            {
                // Apply Porter's stemming algorithm
                stemmedText[i] = Functions.applyPorterStemming(input[i]);
            }

            // Save the modified text in a .sfx file
            Functions.saveSfxFile(stemmedText, outputFileName, writer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
