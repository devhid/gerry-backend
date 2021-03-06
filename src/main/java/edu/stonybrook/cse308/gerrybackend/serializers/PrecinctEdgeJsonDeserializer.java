package edu.stonybrook.cse308.gerrybackend.serializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.TextNode;
import edu.stonybrook.cse308.gerrybackend.graph.edges.PrecinctEdge;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

@JsonComponent
public class PrecinctEdgeJsonDeserializer extends JsonDeserializer<PrecinctEdge> {

    @Override
    public PrecinctEdge deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        TreeNode treeNode = jsonParser.getCodec().readTree(jsonParser);
        TextNode id = (TextNode) treeNode;
        return new PrecinctEdge(id.asText());
    }
}
