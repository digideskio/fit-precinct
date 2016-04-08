package de.konqi.fitapi.common.pwx;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;

@Data
@Slf4j
public class Sample {
    Long timeoffset;
    String hr;
    String spd;
    String pwr;
    String torq;
    String cad;
    String dist;
    String lat;
    String lon;
    String alt;
    String temp;
    String time;
    String extension;

    public void set(String name, Object value) throws NoSuchFieldException, IllegalAccessException {
        if (value.getClass() == String.class) {
            String str = ((String) value).trim();
            if (str.isEmpty()) {
                return;
            }
            Field declaredField = Sample.class.getDeclaredField(name);
            if (value.getClass() != declaredField.getType()) {
                if (value.getClass() == String.class && declaredField.getType() == Long.class) {
                    value = Long.parseLong((String) value);
                } else {
                    log.warn("Unhandled conversion between types.");
                }
            }

            declaredField.set(this, value);
        }
    }
}