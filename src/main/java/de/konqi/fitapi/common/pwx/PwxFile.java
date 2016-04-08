package de.konqi.fitapi.common.pwx;

import lombok.Data;

import java.util.Collection;

/**
 * Created by konqi on 08.04.2016.
 */
@Data
public class PwxFile {
    Summary summary;
    Collection<Sample> samples;
}
