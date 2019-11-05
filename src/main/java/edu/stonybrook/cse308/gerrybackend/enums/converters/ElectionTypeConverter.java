package edu.stonybrook.cse308.gerrybackend.enums.converters;

import edu.stonybrook.cse308.gerrybackend.enums.types.ElectionType;
import edu.stonybrook.cse308.gerrybackend.enums.types.IntEnumInterface;

import java.util.stream.Stream;

public class ElectionTypeConverter extends IntEnumConverter<ElectionType> {
    @Override
    public IntEnumInterface convertToEntityAttribute(Integer integer) {
        if (integer == null){
            return null;
        }
        return Stream.of(ElectionType.values())
                .filter(v -> integer.equals(v.getValue()))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
