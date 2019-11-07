package edu.stonybrook.cse308.gerrybackend.enums.converters;

import edu.stonybrook.cse308.gerrybackend.enums.types.NodeType;
import edu.stonybrook.cse308.gerrybackend.enums.types.IntEnumInterface;

import java.util.stream.Stream;

public class NodeTypeConverter extends IntEnumConverter<NodeType> {
    @Override
    public IntEnumInterface convertToEntityAttribute(Integer integer) {
        if (integer == null){
            return null;
        }
        return Stream.of(NodeType.values())
                .filter(v -> integer.equals(v.getValue()))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
