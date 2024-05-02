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
public class Element implements Comparable<Element> {

    int index;
    Double value;

    Element(int index, Double value){
        this.index = index;
        this.value = value;
    }

    @Override
    public int compareTo(Element e) {
        return Double.compare(this.value, e.value);
    }
}