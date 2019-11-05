package edu.stonybrook.cse308.gerrybackend.enums.converters;

import edu.stonybrook.cse308.gerrybackend.enums.types.DistrictType;
import edu.stonybrook.cse308.gerrybackend.enums.types.IntEnumInterface;

import java.util.stream.Stream;

public class DistrictTypeConverter extends IntEnumConverter<DistrictType> {
    @Override
    public IntEnumInterface convertToEntityAttribute(Integer integer) {
        if (integer == null){
            return null;
        }
        return Stream.of(DistrictType.values())
                .filter(v -> integer.equals(v.getValue()))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
