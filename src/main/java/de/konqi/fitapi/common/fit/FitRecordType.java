package de.konqi.fitapi.common.fit;

/**
 * Created by konqi on 30.04.2016.
 */
public enum FitRecordType {
    TIMESTAMP(253),
    LATITUDE(0),
    LONGITUDE(1),
    ALTITUDE(2),
    HEARTRATE(3),
    CADENCE(4),
    DISTANCE(5),
    SPEED(6),
    POWER(7),
    GRADE(9),
    OTHER(255);


    private final int type;

    FitRecordType(int i) {
        this.type = i;
    }

    public static FitRecordType get(int i) {
        for (FitRecordType fitBaseType : FitRecordType.values()) {
            if (fitBaseType.type == i) return fitBaseType;
        }

        return OTHER;
    }
}
