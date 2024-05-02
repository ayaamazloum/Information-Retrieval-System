/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 *
 * @author Dell
 */
public class Cosine implements Functions {
    private String query;
    
    public Cosine(String query) {
        this.query = query;
    }
    
    public void calculateCosine() throws IOException {
        // Step 1: Remove Stopwords from the query
        String[] queryWords = Functions.removeStopwords(query);
        
        // Step 2: Stem the query words
        for(int i=0; i<queryWords.length;i++) {
            // Apply Porter's stemming algorithm
            queryWords[i] = Functions.applyPorterStemming(queryWords[i]);
        }
        
        // Step 3: Calculate TFIDF for query words
        // Read the TFIDFInvertedFile
        StringBuilder content = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader("src\\pkg\\CSCI512\\TFIDFInvertedFile.txt"));
        
        String tfidfLine;
        String[] words, docs, docFreqString, tfidfRow;
        Integer[] docFreq;
        Double[][] tfidf;
        Double[] queryTfidf;
        
        words = reader.readLine().split(" ");
        reader.readLine();
        docs = reader.readLine().split(" ");
        reader.readLine();
        docFreqString = reader.readLine().split(" ");
        docFreq = new Integer[docFreqString.length];
        for(int i=0; i<docFreqString.length; i++) {
            docFreq[i] = Integer.parseInt(docFreqString[i]);
        }
        reader.readLine();
        tfidf = new Double[docs.length][words.length];
        for(int i=0; i<docs.length; i++) {
            tfidfLine = reader.readLine();
            tfidfRow = tfidfLine.split(" ");
            for(int j=0; j<tfidfRow.length; j++){
                tfidf[i][j] = Double.parseDouble(tfidfRow[j]);
            }
        }
        
        // Calculate query TFIDF
        queryTfidf = new Double[words.length];
        Arrays.fill(queryTfidf, 0.0);
        for(int i=0; i<queryWords.length; i++) {
            for(int j=0; j<words.length; j++)
                if(queryWords[i].equals(words[j]))
                    queryTfidf[j]++;
        }
        
        for(int i=0; i<queryTfidf.length; i++)
            queryTfidf[i] = queryTfidf[i] * Math.log10((double)docs.length/docFreq[i]);
        
        // Calculate the cosine
        DecimalFormat df = new DecimalFormat("0.000");
        Double temp1, temp2 = 0.0, temp3;
        Double[] cosine = new Double[docs.length];
        for(int i=0; i<cosine.length; i++) {
            temp1 = temp3 = 0.0;
            for(int j=0; j<queryTfidf.length; j++) {
                temp1 = temp1 + (queryTfidf[j] * tfidf[i][j]);
                if(i==0)
                    temp2 = temp2 + (queryTfidf[j] * queryTfidf[j]);
                temp3 = temp3 + (tfidf[i][j] * tfidf[i][j]);
            }
            // In case the query has no words from words array
            if(temp2==0) {
                System.out.println("There are no relevant documents.");
                return;
            }
            if(temp3==0)
                cosine[i] = 0.0;
            else
                cosine[i] = temp1 / Math.sqrt(temp2 * temp3);
            String s = df.format(cosine[i]);
            cosine[i] = Double.parseDouble(s);
        }
        
        System.out.println("The retrieved documents in descending order are:");
        
        ArrayList<Element> elements = new ArrayList<>();
        for (int i = 0; i < cosine.length; i++) {
            elements.add(new Element(i, cosine[i]));
        }

        // Sort and print
        Collections.sort(elements);
        Collections.reverse(elements); // If you want reverse order
        for (Element element : elements) {
            if(element.value > 0.001)
                System.out.println(docs[element.index] + " (" + element.value + ")");
        }
    }
}
