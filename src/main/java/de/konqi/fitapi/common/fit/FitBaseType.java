package de.konqi.fitapi.common.fit;

/**
 * Created by konqi on 22.04.2016.
 */
public enum FitBaseType {
    ENUM(0),
    SINT8(1),
    UINT8(2),
    SINT16(3),
    UINT16(4),
    SINT32(5),
    UINT32(6),
    STRING(7),
    FLOAT32(8),
    FLOAT64(9),
    UINT8Z(10),
    UINT16Z(11),
    UINT32Z(12),
    BYTE(12);

    private final int type;

    FitBaseType(int i) {
        this.type = i;
    }

    public static FitBaseType get(int i) {
        for (FitBaseType fitBaseType : FitBaseType.values()) {
            if (fitBaseType.type == i) return fitBaseType;
        }

        return null;
    }


}
