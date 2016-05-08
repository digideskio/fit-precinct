package de.konqi.fitapi.common.fit;

/**
 * Created by konqi on 30.04.2016.
 */
public enum FitSessionType {
    TIMESTAMP(253),
    EVENT(0),
    EVENTTYPE(1),
    STARTTIME(2),
    STARTPOSLAT(3),
    STARTPOSLNG(4),
    SPORT(5),
    SUBSPORT(6),
    TOTALELAPSEDTIME(7),
    TOTALTIMERTIME(8),
    TOTALDISTANCE(9),
    TOTALCALORIES(11),
    AVGSPEED(14),
    MAXSPEED(15),
    AVGHR(16),
    MAXHR(17),
    AVGCADENCE(18),
    MAXCADENCE(19),
    AVGPOWER(20),
    MAXPOWER(21),
    ASCENT(22),
    DECENT(23),
    LAPSCOUNT(26),
    AVGALT(49),
    MAXALT(50),
    AVG_GPS_GRADE(52),
    MAX_GPS_GRADE(55),
    MIN_GPS_GRADE(56),
    AVGTEMP(57),
    MAXTEMP(58),
    MINHR(64),
    MINALT(71),
    SPORTINDEX(111),
    TIMESTANDING(112),
    OTHER(255);

    private final int type;

    FitSessionType(int i) {
        this.type = i;
    }

    public static FitSessionType get(int i) {
        for (FitSessionType fitBaseType : FitSessionType.values()) {
            if (fitBaseType.type == i) return fitBaseType;
        }

        return OTHER;
    }
}
