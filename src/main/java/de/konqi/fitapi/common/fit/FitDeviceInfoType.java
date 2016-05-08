package de.konqi.fitapi.common.fit;

/**
 * Created by konqi on 30.04.2016.
 */
public enum FitDeviceInfoType {
    TIMESTAMP(253),
    DEVINDEX(0),
    DEVTYPE(1),
    MANUFACTURER(2),
    SERIALNO(3),
    PRODUCT(4),
    SOFTWARE_VERSION(5),
    DESCRIPTION(19),
    PRODUCT_NAME(27),
    OTHER(255);

    private final int type;

    FitDeviceInfoType(int i) {
        this.type = i;
    }

    public static FitDeviceInfoType get(int i) {
        for (FitDeviceInfoType fitBaseType : FitDeviceInfoType.values()) {
            if (fitBaseType.type == i) return fitBaseType;
        }

        return OTHER;
    }
}
