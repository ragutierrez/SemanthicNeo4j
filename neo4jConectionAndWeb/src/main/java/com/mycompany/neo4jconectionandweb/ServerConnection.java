/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.neo4jconectionandweb;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import java.net.URI;
import java.net.URISyntaxException;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author ragutierrez
 */
public class ServerConnection {

    final String SERVER_ROOT_URI = "http://localhost:7474/db/data/";
    final String nodeEntryPointUri = SERVER_ROOT_URI + "node/";

    public void serverStatus() {
        WebResource resource = Client.create()
                .resource(SERVER_ROOT_URI);
        ClientResponse response = resource.get(ClientResponse.class);

//        System.out.println(String.format("GET on [%s], status code [%d]",
//                SERVER_ROOT_URI, response.getStatus()));
        response.close();
    }

    public void initServer() {
        String array[];
        String reqUri = SERVER_ROOT_URI + "index/auto/node/properties";

        WebResource resource = Client.create()
                .resource(reqUri);

        ClientResponse response = resource.accept(MediaType.APPLICATION_JSON)
                .type(MediaType.APPLICATION_JSON)
                .get(ClientResponse.class);

        String responseText = response.getEntity(String.class);

        if (responseText.length() > 3) {
            array = responseText
                    .substring(1, responseText.length() - 1)
                    .split(",");

            for (String indexPropertieName : array) {
                String indexPropertieNameTrimed = indexPropertieName.trim();
//                System.out.println(
//                        indexPropertieNameTrimed
//                        .substring(1, indexPropertieNameTrimed.length() - 1));
            }
        } else {
            reqUri = SERVER_ROOT_URI + "index/node/";
            resource = Client.create()
                    .resource(reqUri);

            String JSONReq = "{"
                    + "\"name\" : \"node_auto_index\","
                    + "\"config\" : {"
                    + "\"type\" : \"fulltext\","
                    + "\"provider\" : \"lucene\""
                    + "}"
                    + "}";

            response = resource.accept(MediaType.APPLICATION_JSON)
                    .type(MediaType.APPLICATION_JSON)
                    .entity(JSONReq)
                    .post(ClientResponse.class);
        }
        response.close();
    }

    public String query(String name) {
        String JSON;
        String cypherUri = SERVER_ROOT_URI + "cypher";
        String query = "start n=node:node_auto_index(name='" + name + "') return n AS nodes";

        String cypherStatement =
                "{"
                + "\"query\" : \"" + query + "\""
                + "}";

        WebResource resource = Client.create()
                .resource(cypherUri);

        ClientResponse response = resource.accept(MediaType.APPLICATION_JSON)
                .type(MediaType.APPLICATION_JSON)
                .entity(cypherStatement)
                .post(ClientResponse.class);

        JSON = response.getEntity(String.class);

        response.close();

        return JSON;

    }

    public String queryLike(String name) {
        String JSON;
        String cypherUri = SERVER_ROOT_URI + "cypher";
        String query = "start n=node:node_auto_index('name:*" + name + "*') retun n AS nodes";

        String cypherStatement =
                "{"
                + "\"query\" : \"" + query + "\""
                + "}";

        WebResource resource = Client.create()
                .resource(cypherUri);

        ClientResponse response = resource.accept(MediaType.APPLICATION_JSON)
                .type(MediaType.APPLICATION_JSON)
                .entity(cypherStatement)
                .post(ClientResponse.class);

        JSON = response.getEntity(String.class);

        response.close();

        return JSON;

    }

    public URI createNode(String propertyValue) {
        WebResource resource = Client.create()
                .resource(nodeEntryPointUri);

        String strEntity = "{"
                + "\"name\" : \"" + propertyValue + "\""
                + "}";

        ClientResponse response = resource.accept(MediaType.APPLICATION_JSON)
                .type(MediaType.APPLICATION_JSON)
                .entity(strEntity)
                .post(ClientResponse.class);

        final URI location = response.getLocation();
        response.close();
        return location;
    }

    /**
     *
     * NOT USED
     *
     * private void addMetadataToProperty(URI relationshipUri, String name,
     * String value) throws URISyntaxException { URI propertyUri = new
     * URI(relationshipUri.toString() + "/properties"); String entity =
     * toJsonNameValuePairCollection(name, value); WebResource resource =
     * Client.create() .resource(propertyUri); ClientResponse response =
     * resource.accept(MediaType.APPLICATION_JSON)
     * .type(MediaType.APPLICATION_JSON) .entity(entity)
     * .put(ClientResponse.class);
     *
     * System.out.println(String.format( "PUT [%s] to [%s], status code [%d]",
     * entity, propertyUri, response.getStatus())); response.close(); }
     *
     *
     * private String toJsonNameValuePairCollection(String name, String value) {
     * return String.format("{ \"%s\" : \"%s\" }", name, value); }
     *
     *
     *
     *
     *
     *
     * NOT USED
     *
     * public URI createNode() {
     *
     * WebResource resource = Client.create() .resource(nodeEntryPointUri); //
     * POST {} to the node entry point URI ClientResponse response =
     * resource.accept(MediaType.APPLICATION_JSON)
     * .type(MediaType.APPLICATION_JSON) .entity("{}")
     * .post(ClientResponse.class);
     *
     * final URI location = response.getLocation(); System.out.println("POST to
     * " + nodeEntryPointUri); System.out.println("status code " +
     * response.getStatus()); System.out.println("location header " +
     * location.toString()); response.close();
     *
     * return location; }
     *
     * private void addProperty(URI nodeUri, String propertyName, String
     * propertyValue) { String propertyUri = nodeUri.toString() + "/properties/"
     * + propertyName; //
     * http://localhost:7474/db/data/node/{node_id}/properties/{property_name}
     *
     * WebResource resource = Client.create() .resource(propertyUri);
     * ClientResponse response = resource.accept(MediaType.APPLICATION_JSON)
     * .type(MediaType.APPLICATION_JSON) .entity("\"" + propertyValue + "\"")
     * .put(ClientResponse.class);
     *
     * System.out.println(String.format("PUT to [%s], status code [%d]",
     * propertyUri, response.getStatus())); response.close(); }
     *
     *
     */
    public URI addRelationship(URI startNode, URI endNode,
            String relationshipType) throws URISyntaxException {

        URI fromUri = new URI(startNode.toString() + "/relationships");
        String relationshipJson = generateJsonRelationship(endNode,
                relationshipType);

        WebResource resource = Client.create()
                .resource(fromUri);
        // POST JSON to the relationships URI
        ClientResponse response = resource.accept(MediaType.APPLICATION_JSON)
                .type(MediaType.APPLICATION_JSON)
                .entity(relationshipJson)
                .post(ClientResponse.class);

        final URI location = response.getLocation();
//        System.out.println(String.format(
//                "POST to [%s], status code [%d], location header [%s]",
//                fromUri, response.getStatus(), location.toString()));

        response.close();
        return location;
    }

    private String generateJsonRelationship(URI endNode,
            String relationshipType) {
        StringBuilder sb = new StringBuilder();
        sb.append("{ \"to\" : \"");
        sb.append(endNode.toString());
        sb.append("\", ");

        sb.append("\"type\" : \"");
        sb.append(relationshipType);
        sb.append("\"");
        sb.append(" }");

//        System.out.println(sb.toString());
        return sb.toString();
    }

    public static void main(String[] args) {
        ServerConnection objConn = new ServerConnection();
        objConn.initServer();
        objConn.serverStatus();
//        String result = objConn.query("http://localhost/ontologies#personasPersonas");
        String result = objConn.queryLike("Universidad");

        System.out.println(result);
    }
}
