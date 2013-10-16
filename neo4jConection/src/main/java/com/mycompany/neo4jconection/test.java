/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.neo4jconection;

/**
 *
 * @author ragutierrez
 */
public class test {

    public static void main(String[] args) {
        TripleReader objTripleReader = new TripleReader();
        objTripleReader.leerArchivo("triples.txt");
    }
}
