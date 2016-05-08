package de.konqi.fitapi.common.fit;

/**
 * Created by konqi on 30.04.2016.
 */
public enum FitEventType {
    TIMESTAMP(253),
    EVENTFIELD(0),
    EVENTTYPE(1),
    DATA1(2),
    DATA2(3),
    OTHER(255);

    private final int type;

    FitEventType(int i) {
        this.type = i;
    }

    public static FitEventType get(int i) {
        for (FitEventType fitBaseType : FitEventType.values()) {
            if (fitBaseType.type == i) return fitBaseType;
        }

        return OTHER;
    }
}
