package edu.stonybrook.cse308.gerrybackend.enums.converters;

import edu.stonybrook.cse308.gerrybackend.enums.IntEnumInterface;
import edu.stonybrook.cse308.gerrybackend.enums.types.PoliticalParty;

import java.util.stream.Stream;

public class PoliticalPartyConverter extends IntEnumConverter<PoliticalParty> {
    @Override
    public IntEnumInterface convertToEntityAttribute(Integer integer) {
        if (integer == null) {
            return null;
        }
        return Stream.of(PoliticalParty.values())
                .filter(v -> integer.equals(v.getValue()))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
