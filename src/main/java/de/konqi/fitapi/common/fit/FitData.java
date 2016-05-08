package de.konqi.fitapi.common.fit;

import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by konqi on 06.05.2016.
 */
@Data
public class FitData {
    Map<Enum, Object> header = new HashMap<>();
    Map<Enum, Map<Object, Map<Enum, Object>>> data = new HashMap<>();
    Map<Enum, Map<Enum, Object>> meta = new HashMap<>();

}
