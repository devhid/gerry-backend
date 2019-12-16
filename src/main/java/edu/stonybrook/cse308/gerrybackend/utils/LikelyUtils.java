package edu.stonybrook.cse308.gerrybackend.utils;

import edu.stonybrook.cse308.gerrybackend.enums.types.LikelyType;

public class LikelyUtils {

    public static LikelyType getLikelyTypeFromJoinabilityWithoutMinority(double joinability) {
        if (joinability < 0.5) {
            return LikelyType.NOT;
        }

        if (joinability >= 0.5 && joinability < 1.5) {
            return LikelyType.KIND_OF;
        }

        return LikelyType.VERY;
//
//        if (joinability >= 3.0) {
//            return LikelyType.VERY;
//        }

//        // should never happen, because joinability without minority should be <= 0 and <= 5,
//        // but added as a sanity check.
//        throw new IllegalArgumentException("Replace string with text later! " + joinability);
    }

    public static LikelyType getLikelyTypeFromJoinability(double joinability) {
        if (joinability >= 0.0 && joinability < 1.0) {
            return LikelyType.NOT;
        }

        if (joinability >= 1.0 && joinability < 3.0) {
            return LikelyType.KIND_OF;
        }

        if (joinability >= 3.0 && joinability <= 6.0) {
            return LikelyType.VERY;
        }

        // should never happen, because joinability without minority should be <= 0 and <= 5,
        // but added as a sanity check.
        throw new IllegalArgumentException("Replace string with text later! " + joinability);
    }
}
