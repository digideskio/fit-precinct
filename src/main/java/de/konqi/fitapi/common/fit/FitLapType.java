package de.konqi.fitapi.common.fit;

/**
 * Created by konqi on 30.04.2016.
 */
public enum FitLapType {
    TIMESTAMP(253),
    INDEX(254),
    EVENT(0),
    EVENT_TYPE(1),
    START_TIME(2),
    START_POS_LAT(3),
    START_POS_LNG(4),
    END_POS_LAT(5),
    END_POS_LNG(6),
    TOTAL_ELAPSED_TIME(7),
    TOTAL_TIMER_TIME(8),
    TOTAL_DISTANCE(9),
    TOTAL_CALORIES(11),
    AVG_SPEED(13),
    MAX_SPEED(14),
    AVG_HR(15),
    MAX_HR(16),
    AVG_ALT(42),
    MAX_ALT(43),
    AVG_GPS_GRADE(45),
    MAX_GPS_GRADE(48),
    MIN_GPS_GRADE(49),
    MIN_ALT(62),
    MIN_HR(63),
    OTHER(255);

    private final int type;

    FitLapType(int i) {
        this.type = i;
    }

    public static FitLapType get(int i) {
        for (FitLapType fitBaseType : FitLapType.values()) {
            if (fitBaseType.type == i) return fitBaseType;
        }

        return OTHER;
    }
}
