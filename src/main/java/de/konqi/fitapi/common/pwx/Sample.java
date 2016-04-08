package de.konqi.fitapi.common.pwx;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.xml.bind.DatatypeConverter;
import java.lang.reflect.Field;
import java.util.Date;

@Data
@Slf4j
public class Sample {
    Long timeoffset;
    Integer hr;
    Double spd;
    Integer pwr;
    Double torq;
    Integer cad;
    Double dist;
    Double lat;
    Double lon;
    Double alt;
    Double temp;
    Date time;
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
                } else if (declaredField.getType() == Double.class) {
                    value = Double.parseDouble(str);
                } else if (declaredField.getType() == Integer.class) {
                    value = Integer.parseInt(str);
                } else if (declaredField.getType() == Date.class) {
                    value = DatatypeConverter.parseDateTime(str).getTime();
                } else {
                    log.warn("Unhandled conversion between types.");
                }
            }

            declaredField.set(this, value);
        }
    }
}