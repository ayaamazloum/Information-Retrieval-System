/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg;

/**
 *
 * @author Dell
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class InvertedFileCreator {

    private ArrayList<Double[]> tfidf;

    public InvertedFileCreator(String folderPath, String invertedFilePath) throws IOException {
        tfidf = new ArrayList<>();
        processDocuments(folderPath, invertedFilePath);
    }
    
    private void processDocuments(String folderPath, String invertedFilePath)  throws IOException {
        File folder = new File(folderPath);
        File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".sfx"));
        
        int num_docs = files.length;
        ArrayList<String> terms = new ArrayList<>();
        ArrayList<String> docs = new ArrayList<>();
        ArrayList<Integer> docFreq = new ArrayList<>();
        Double[] tf = new Double[num_docs];
        int i = 0;
        
        for (File file : files) {
            docs.add(file.getName().split(".sfx")[0]);
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] words = line.split("\\s+");
                    for (String word : words) {
                        if(terms.contains(word)){
                            tf = tfidf.get(terms.indexOf(word));
                            if(tf[i] != null)
                                tf[i]++;
                            else {
                                tf[i] = 1.0;
                                int lastDocFreqValue = docFreq.get(terms.indexOf(word));
                                docFreq.set(terms.indexOf(word), lastDocFreqValue + 1);
                            }
                            tfidf.set(terms.indexOf(word), tf);
                        }
                        else {
                            terms.add(word);
                            tf = new Double[num_docs];
                            tf[i] = 1.0;
                            tfidf.add(tf);
                            docFreq.add(1);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            i++;
        }
        
        /*// Print term frequency table
        for(int x=0; x<terms.size(); x++)
            System.out.print(terms.get(x) + " ");
        System.out.println();
        for(int x=0; x<tf.length; x++) {
            System.out.print(docs.get(x) + "     ");
            for(int y=0; y<tfidf.size(); y++){
                System.out.print(tfidf.get(y)[x] + " ");
            }
            System.out.println();
        }
        System.out.print("DocFreq ");
        for(int x=0; x<docFreq.size(); x++)
            System.out.print(docFreq.get(x) + " ");
        System.out.println();*/
        
        // Create Boolean and TFIDF Inverted Files
        DecimalFormat df = new DecimalFormat("0.000");
        
        Double[] tabletfidf;
        String s;
        for(int x=0; x<tfidf.size(); x++) {
            //System.out.print(terms.get(x) + "     ");
            tabletfidf = new Double[num_docs];
            for(int y=0; y<tabletfidf.length; y++){
                //tfidf.set(x, tf)
                if(tfidf.get(x)[y] == null)
                    tabletfidf[y] = 0.0;
                else
                    tabletfidf[y] = tfidf.get(x)[y] * Math.log10((double)num_docs/docFreq.get(x));
                //s = df.format(tabletfidf[y]);
                //tabletfidf[y] = Double.parseDouble(s);
                //System.out.print(tabletfidf[y]);
            }
            tfidf.set(x, tabletfidf);
            //System.out.println("   docFreq: " + docFreq.get(x));
            
            // save the results in a file
            String boolInvFileName = invertedFilePath + "BooleanInvertedFile.txt";
            String TFIDFInvFileName = invertedFilePath + "TFIDFInvertedFile.txt";
            File inputFile = new File(boolInvFileName);
            inputFile = new File(TFIDFInvFileName);
            PrintWriter writer1 = new PrintWriter(boolInvFileName);
            PrintWriter writer2 = new PrintWriter(TFIDFInvFileName);
            for (String term : terms) {
                writer1.print(term + " ");
                writer2.print(term + " ");
            }
            writer1.print("\n\n");
            writer2.print("\n\n");
            for (String doc : docs) {
                writer1.print(doc + " ");
                writer2.print(doc + " ");
            }
            writer1.print("\n\n");
            writer2.print("\n\n");
            for (int dFreq : docFreq) {
                writer1.print(dFreq + " ");
                writer2.print(dFreq + " ");
            }
            writer1.print("\n\n");
            writer2.print("\n\n");
            
            for(int j=0; j<tf.length; j++) {
                for(int k=0; k<tfidf.size(); k++){
                    if(tfidf.get(k)[j] == null || tfidf.get(k)[j] == 0)
                        writer1.print("0 ");
                    else
                        writer1.print("1 ");
                    writer2.print(tfidf.get(k)[j] + " ");
                }
                writer1.print("\n");
                writer2.print("\n");
            }
            writer1.close();
            writer2.close();
        }
        
        // Print inverted file in boolean
        /*System.out.println();
        System.out.printf("--------------------------------%n");
        System.out.printf("      Boolean Inverted File     %n");
        System.out.printf("--------------------------------%n");
        System.out.print("|            ");
        for(int x=0; x<terms.size(); x++)
            System.out.printf("| %-10s ", terms.get(x));
        System.out.println("|");
        for(int x=0; x<tf.length; x++) {
            System.out.printf("| %-10s ", docs.get(x));
            for(int y=0; y<tfidf.size(); y++){
                if(tfidf.get(y)[x] == null || tfidf.get(y)[x] == 0)
                    System.out.printf("| 0          ");
                else
                    System.out.printf("| 1          ");
            }
            System.out.println("|");
        }
        System.out.print("| DocFreq    ");
        for(int x=0; x<docFreq.size(); x++)
            System.out.printf("| %d          ", docFreq.get(x));
        System.out.println("|");*/
        
        
        // Print inverted file in tfidf
        /*System.out.println();
        System.out.printf("--------------------------------%n");
        System.out.printf("       TFIDF Inverted File      %n");
        System.out.printf("--------------------------------%n");
        System.out.print("|            ");
        for(int x=0; x<terms.size(); x++)
            System.out.printf("| %-10s ", terms.get(x));
        System.out.println("|");
        for(int x=0; x<tf.length; x++) {
            System.out.printf("| %-10s ", docs.get(x));
            for(int y=0; y<tfidf.size(); y++){
                System.out.printf("| %.3f      ", tfidf.get(y)[x]);
            }
            System.out.println("|");
        }
        System.out.print("| DocFreq    ");
        for(int x=0; x<docFreq.size(); x++)
            System.out.printf("| %d          ", docFreq.get(x));
        System.out.println("|\n");*/
    }
}
