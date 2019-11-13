//package edu.stonybrook.cse308.gerrybackend.serializers;
//
//import com.fasterxml.jackson.core.JsonGenerator;
//import com.fasterxml.jackson.core.JsonParser;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.DeserializationContext;
//import com.fasterxml.jackson.databind.JsonDeserializer;
//import com.fasterxml.jackson.databind.JsonSerializer;
//import com.fasterxml.jackson.databind.SerializerProvider;
//import edu.stonybrook.cse308.gerrybackend.graph.nodes.StateNode;
//import org.springframework.boot.jackson.JsonComponent;
//
//import java.io.IOException;
//
//@JsonComponent
//public class StateNodeSerializer {
//
//    public static class StateNodeJsonSerializer extends JsonSerializer<StateNode> {
//
//        @Override
//        public void serialize(StateNode stateNode, JsonGenerator jsonGenerator,
//                              SerializerProvider serializerProvider) throws IOException {
//            jsonGenerator.writeStartObject();
//            jsonGenerator.writeEndObject();
//        }
//    }
//
//    public static class StateNodeJsonDeserializer extends JsonDeserializer<StateNode> {
//
//        @Override
//        public StateNode deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
//                throws IOException, JsonProcessingException {
//            return null;
//        }
//
//    }
//
//}
