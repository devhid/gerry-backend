//package edu.stonybrook.cse308.gerrybackend.serializers;
//
//import com.fasterxml.jackson.core.JsonGenerator;
//import com.fasterxml.jackson.core.JsonParser;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.DeserializationContext;
//import com.fasterxml.jackson.databind.JsonDeserializer;
//import com.fasterxml.jackson.databind.JsonSerializer;
//import com.fasterxml.jackson.databind.SerializerProvider;
//import edu.stonybrook.cse308.gerrybackend.graph.nodes.DistrictNode;
//import edu.stonybrook.cse308.gerrybackend.graph.nodes.PrecinctNode;
//import org.springframework.boot.jackson.JsonComponent;
//
//import java.io.IOException;
//
//@JsonComponent
//public class PrecinctNodeSerializer {
//
//    public static class PrecinctNodeJsonSerializer extends JsonSerializer<PrecinctNode> {
//
//        @Override
//        public void serialize(PrecinctNode precinctNode, JsonGenerator jsonGenerator,
//                              SerializerProvider serializerProvider) throws IOException {
//            jsonGenerator.writeStartObject();
//            jsonGenerator.writeEndObject();
//        }
//    }
//
//    public static class PrecinctNodeJsonDeserializer extends JsonDeserializer<PrecinctNode> {
//
//        @Override
//        public PrecinctNode deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
//                throws IOException, JsonProcessingException {
//            return null;
//        }
//
//    }
//
//}
