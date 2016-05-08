package de.konqi.fitapi.common.fit;

/**
 * Created by konqi on 30.04.2016.
 */
public enum FitFileIdType {
    TYPE(0),
    MANUFACTURER(1),
    PRODUCT(2),
    SERIALNO(3),
    TIMECREATED(4),
    NUMBER(5),
    PRODUCTNAME(8),
    OTHER(255);

    private final int type;

    FitFileIdType(int i) {
        this.type = i;
    }

    public static FitFileIdType get(int i) {
        for (FitFileIdType fitBaseType : FitFileIdType.values()) {
            if (fitBaseType.type == i) return fitBaseType;
        }

        return OTHER;
    }
}
