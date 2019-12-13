package edu.stonybrook.cse308.gerrybackend.enums.converters;

import edu.stonybrook.cse308.gerrybackend.enums.IntEnumInterface;
import edu.stonybrook.cse308.gerrybackend.enums.measures.Measures;

import java.util.stream.Stream;

public class MeasuresConverter extends IntEnumConverter<Measures> {
    @Override
    public IntEnumInterface convertToEntityAttribute(Integer integer) {
        if (integer == null) {
            return null;
        }
        return Stream.of(Measures.values())
                .filter(v -> integer.equals(v.getValue()))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
