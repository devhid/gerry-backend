package edu.stonybrook.cse308.gerrybackend.enums.converters;

import edu.stonybrook.cse308.gerrybackend.enums.types.DemographicType;
import edu.stonybrook.cse308.gerrybackend.enums.IntEnumInterface;

import java.util.stream.Stream;

public class DemographicTypeConverter extends IntEnumConverter<DemographicType> {
    @Override
    public IntEnumInterface convertToEntityAttribute(Integer integer) {
        if (integer == null){
            return null;
        }
        return Stream.of(DemographicType.values())
                .filter(v -> integer.equals(v.getValue()))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
