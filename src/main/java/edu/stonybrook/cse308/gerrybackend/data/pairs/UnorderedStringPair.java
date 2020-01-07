package edu.stonybrook.cse308.gerrybackend.data.pairs;

import com.fasterxml.jackson.annotation.JsonValue;

public class UnorderedStringPair extends UnorderedPair<String> {

    public UnorderedStringPair() {
        super();
    }

    public UnorderedStringPair(String item1, String item2) {
        super(item1, item2);
    }

    @JsonValue
    @Override
    public String toString() {
        String pair = "";
        if (this.item1 != null) {
            pair += this.item1 + ",";
        }
        if (this.item2 != null) {
            pair += this.item2 + ",";
        }
        pair = pair.substring(0, pair.length() - 1);
        return pair;
    }
}
