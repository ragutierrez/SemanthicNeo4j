function main() {
    // Paso 1. Creamos un objeto Graph.
    var graph = Viva.Graph.graph();

    // Paso 2. se añaden los nodos y aristas al grafo:
     // graph.addLink(1, 2);

    // Step 3. Personalizamos el dibujo del grafo.
    var graphics = Viva.Graph.View.svgGraphics();
    
    var renderer = Viva.Graph.View.renderer(graph,
      {
          layout:layout,
          graphics:graphics,
          container:document.getElementById('canvas'),
          renderLinks:true
      });
      
    renderer.run();
    
    loadData(graph,neoid);
}

function loadData(graph,id) {
    $.ajax(id ? "/edges/" + id : "/edges", {
        type:"GET",
        dataType:"json",
        success:function (res) {
            addNeo(graph, {edges:res});
        }
    })
}

function addNeo(graph, data) {
    function addNode(id, label) {
        if (!id || typeof id == "undefined") return null;
        var node = graph.getNode(id);
        if (!node) node = graph.addNode(id, label);
        return node;
    }

    for (n in data.edges) {
        if (data.edges[n].source) {
            addNode(data.edges[n].source, data.edges[n].source_data );
        }
        if (data.edges[n].target) {
            addNode(data.edges[n].target, data.edges[n].target_data);
        }
    }

    for (n in data.edges) {
        var edge=data.edges[n];
        var found=false;
        graph.forEachLinkedNode(edge.source, function (node, link) {
            if (node.id==edge.target) found=true;
        });
        if (!found && edge.source && edge.target) graph.addLink(edge.source, edge.target);
    }
}