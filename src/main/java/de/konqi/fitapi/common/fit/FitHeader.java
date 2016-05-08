package de.konqi.fitapi.common.fit;

import lombok.Data;

/**
 * Created by konqi on 22.04.2016.
 */
@Data
public class FitHeader {
    int size;
    int protocolVersion;
    int profileVersion;
    long dataSize;
    String dataType;
    int crc;
}
