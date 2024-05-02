/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg;

import java.util.Scanner;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Map;

/**
 *
 * @author wael Mahfouz
 */

public class Driver {
    public static void main(String[] args) throws IOException {
        String inputFileName, outputFileName, pureName, newName, txtFolderPath = "src\\pkg\\CSCI512\\txtFiles\\", stpFolderPath = "src\\pkg\\CSCI512\\stpFiles\\", sfxFolderPath = "src\\pkg\\CSCI512\\sfxFiles\\", invertedFilePath = "src\\pkg\\CSCI512\\";
        
        File file = new File(txtFolderPath);
        String[] fileList = file.list(new FilenameFilter() {
            public boolean accept(File file, String name) {
                return name.endsWith(".txt") && name.toLowerCase().startsWith("doc"); // process only files of type txt and starts with doc
            }
        });
        
        // Project 1
        for(String name:fileList){
            inputFileName = txtFolderPath+name;
            pureName = name.split(".txt")[0];
            outputFileName = stpFolderPath+pureName+".stp";

            StopList sl = new StopList(inputFileName, outputFileName);
            sl.run();
        }
        //System.out.println("Project 1: Remove stopwords is done.");
        
        // Project 2
        for(String name:fileList){
            newName = name.split(".txt")[0]+".stp";
            inputFileName = stpFolderPath+newName;
            pureName = newName.split(".stp")[0];
            outputFileName = sfxFolderPath+pureName+".sfx";

            Stemming s = new Stemming(inputFileName, outputFileName);
            s.run();
        }
        //System.out.println("Project 2: Remove suffixes is done.");
        
        // Project 3
        InvertedFileCreator invFile = new InvertedFileCreator(sfxFolderPath, invertedFilePath);
        //System.out.println("Project 3: Create inverted file is done.");
        
        //Project 4
        //System.out.println("");
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter your query: ");
        String query = scan.nextLine();
        Cosine cos = new Cosine(query);
        cos.calculateCosine();
        //System.out.println("Project 4: Querying process is done.");
    }
}
