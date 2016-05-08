package de.konqi.fitapi.common.fit;

/**
 * Created by konqi on 30.04.2016.
 */
public enum FitActivityType {
    TIMESTAMP(253),
    TOTAL_TIMER_TIME(0),
    NUM_SESSIONS(1),
    TYPE(2),
    EVENT(3),
    EVENT_TYPE(4),
    LOCAL_TIMESTAMP(5),
    EVENT_GROUP(6),
    OTHER(255);


    private final int type;

    FitActivityType(int i) {
        this.type = i;
    }

    public static FitActivityType get(int i) {
        for (FitActivityType fitBaseType : FitActivityType.values()) {
            if (fitBaseType.type == i) return fitBaseType;
        }

        return OTHER;
    }
}
