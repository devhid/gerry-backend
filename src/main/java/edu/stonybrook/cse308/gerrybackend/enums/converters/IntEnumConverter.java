package edu.stonybrook.cse308.gerrybackend.enums.converters;

import edu.stonybrook.cse308.gerrybackend.enums.IntEnumInterface;

import javax.persistence.AttributeConverter;

public abstract class IntEnumConverter<T> implements AttributeConverter<IntEnumInterface, Integer> {

    @Override
    public Integer convertToDatabaseColumn(IntEnumInterface intEnumInterface) {
        if (intEnumInterface == null) {
            return null;
        }
        return intEnumInterface.getValue();
    }

    public abstract IntEnumInterface convertToEntityAttribute(Integer integer);
}
