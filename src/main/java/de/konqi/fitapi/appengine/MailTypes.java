package de.konqi.fitapi.appengine;

import de.konqi.fitapi.common.importer.FitImporter;
import de.konqi.fitapi.common.importer.Importer;
import de.konqi.fitapi.common.importer.PwxImporter;

/**
 * Created by konqi on 08.05.2016.
 */
public enum MailTypes {
    PWX("pwx", new PwxImporter()), FIT("fit", new FitImporter());

    private final String extension;
    private final Importer importer;

    MailTypes(String extension, Importer importer) {
        this.extension = extension;
        this.importer = importer;
    }

    public String getExtension() {
        return extension;
    }

    public Importer getImporter() {
        return importer;
    }
}
