package de.konqi.fitapi.common.importer;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by konqi on 08.05.2016.
 */
public interface Importer {
    void importFromStream(String userEmail, InputStream is) throws IOException;
}
