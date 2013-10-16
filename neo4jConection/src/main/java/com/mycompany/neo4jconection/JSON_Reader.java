/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.neo4jconection;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import java.net.URI;

/**
 *
 * @author ragutierrez
 */
public class JSON_Reader {

    public URI getURINodeifExists(String jsonTxt) {

        URI uriNode = null;

        JSONObject json = (JSONObject) JSONSerializer.toJSON(jsonTxt);

        JSONArray arrayNodes = json.getJSONArray("data");
        for (Object object : arrayNodes) {
            JSONObject nodes = (JSONObject) ((JSONArray) object).get(0);
//            String nodeData = nodes.getJSONObject("data").toString();
            String nodeSelf = nodes.getString("self");

            uriNode = URI.create(nodeSelf);

//            System.out.println(nodeData);
//            System.out.println(uriNode);
        }


        return uriNode;
    }
}
