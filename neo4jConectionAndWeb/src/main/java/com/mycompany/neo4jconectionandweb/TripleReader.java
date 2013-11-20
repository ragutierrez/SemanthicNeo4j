package com.mycompany.neo4jconectionandweb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TripleReader {

    ServerConnection objServConn = new ServerConnection();
    JSON_Reader objJSON_Reader = new JSON_Reader();

//    public static void main(String[] args) {
//        TripleReader objTripleReader = new TripleReader();
////        objTripleReader.leerArchivo(new File("triples.txt"));
//        System.out.println(
//                objTripleReader.HacerNodo("http://localhost/ontologies#personasPersonas"));
//    }
    private URI hacerNodo(String name) {

        if (name.substring(name.length() - 1, name.length()).equals(".")) {
            name = name.substring(0, name.length() - 1);
        }

        if (name.substring(0, 1).equals("\"")
                && name.substring(name.length() - 1).equals("\"")) {
            name = name.substring(1, name.length() - 1);
        }

        String JSON = objServConn.query(name);

        URI exists = objJSON_Reader.getURINodeifExists(JSON);

        if (exists == null) {
            exists = objServConn.createNode(name);
            return exists;
        } else {
            return exists;
        }
    }

    private void hacerRelacion(URI startNode, URI endNode,
            String relationshipType) {
        try {
            objServConn.addRelationship(startNode, endNode, relationshipType);
        } catch (URISyntaxException ex) {
            Logger.getLogger(TripleReader.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("No se pudo hacer relacion entre:\n"
                    + startNode
                    + " y " + endNode
                    + "\nAtributos:\n" + relationshipType);
        }
    }

    public void leerArchivo(String filePath) {
        File archivo = new File(filePath);

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(archivo));
            try {
                String line = br.readLine();
                line = line.substring(1, line.length());

                while (line != null) {

                    construirNodosRelaciones(line);

                    line = br.readLine();
                    if (line != null) {
                        line = line.substring(1, line.length());
                    }
                }
            } finally {
                br.close();
            }

        } catch (Exception ex) {
            Logger.getLogger(TripleReader.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
                Logger.getLogger(TripleReader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void construirNodosRelaciones(String line) {
        String nameNodo1 = (line.split("> <|>."))[0];
        URI uriNodo1 = hacerNodo(nameNodo1);
        String nameNodo2 = (line.split("> <|>."))[2];
        URI uriNodo2 = hacerNodo(nameNodo2);
        String relationshipType = line.split("> <|>.")[1];

        System.out.println("sujeto: " + (line.split("> <|>."))[0]);
        System.out.println("predicado: " + (line.split("> <|>."))[1]);
        System.out.println("objeto: " + (line.split("> <|>."))[2]);

        hacerRelacion(uriNodo1, uriNodo2, relationshipType);
    }
}
