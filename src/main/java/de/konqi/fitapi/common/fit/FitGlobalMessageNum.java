package de.konqi.fitapi.common.fit;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by konqi on 30.04.2016.
 */
@Slf4j
public enum FitGlobalMessageNum {
    FILE_ID(0, FitFileIdType.class),
    SESSION(18, FitSessionType.class),
    LAP(19, FitLapType.class),
    RECORD(20, FitRecordType.class),
    EVENT(21, FitEventType.class),
    DEVICE_INFO(23, FitDeviceInfoType.class),
    ACTIVITY(34, FitActivityType.class),
    OTHER(255);

    private final int type;
    private final Class clazz;

    FitGlobalMessageNum(int i) {
        this.type = i;
        this.clazz = null;
    }

    FitGlobalMessageNum(int i, Class clazz) {
        this.type = i;
        this.clazz = clazz;
    }

    public static FitGlobalMessageNum get(int i) {
        for (FitGlobalMessageNum fitBaseType : FitGlobalMessageNum.values()) {
            if (fitBaseType.type == i) return fitBaseType;
        }

        return OTHER;
    }

    public Enum getSub(int i) {
        if (clazz != null) {
            try {
                @SuppressWarnings("unchecked")
                Method method = clazz.getMethod("get", int.class);
                return (Enum)method.invoke(null, i);
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                log.error("Reflection exception.", e);
            }
        }

        return null;
    }
}
