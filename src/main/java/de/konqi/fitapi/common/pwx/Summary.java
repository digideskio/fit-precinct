package de.konqi.fitapi.common.pwx;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.xml.bind.DatatypeConverter;
import java.lang.reflect.Field;
import java.util.Date;

@Data
@Slf4j
public class Summary {
    /**
     * workout type
     */
    String sportType;

    Date time;

    /**
     * beggining is seconds offset from beginning of wkt
     */
    Double beginning;

    /**
     * duration in seconds
     */
    Double duration;
    /**
     * duration of time when stopped in seconds
     */
    String durationstopped;

    /**
     * work in kJ
     */
    String work;

    String tss;
    /**
     * power in watts
     */
    String normalizedPower;
    /**
     * heart rate in bpm
     */
    Double hrAvg;
    Double hrMin;
    Double hrMax;
    /**
     * speed in meters per second
     */
    Double spdAvg;
    Double spdMin;
    Double spdMax;

    /**
     * power in watts
     */
    Double pwr;
    /**
     * torque in nM
     */
    Double torq;
    /**
     * cadence in rpm
     */
    Double cad;
    /**
     * distance in meters
     */
    Double dist;
    /**
     * altitude in meters
     */
    Double alt;
    /**
     * temperature in celcius
     */
    Double temp;
    String variabilityIndex;
    /**
     * elevation climbed in meters
     */
    Double climbingelevation;
    /**
     * elevation descended in meters
     */
    Double descendingelevation;
    String extension;

    public void set(String name, Object value) throws NoSuchFieldException, IllegalAccessException {
        if (value.getClass() == String.class) {
            String str = ((String) value).trim();
            if (str.isEmpty()) {
                return;
            }

            Field declaredField = Summary.class.getDeclaredField(name);
            if (value.getClass() != declaredField.getType()) {
                if (value.getClass() == String.class) {
                    if (declaredField.getType() == Double.class) {
                        value = Double.parseDouble(str);
                    } else if (declaredField.getType() == Long.class) {
                        value = Long.parseLong(str);
                    } else if (declaredField.getType() == Date.class) {
                        value = DatatypeConverter.parseDateTime(str).getTime();
                    } else {
                        log.warn("Unhandled conversion between types.");
                    }
                }
            }

            declaredField.set(this, value);
        }
    }
}