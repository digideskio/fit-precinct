package de.konqi.fitapi.db.domain;

import java.io.Serializable;

/**
 * Created by konqi on 17.08.2015.
 */
public class DataSet implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long offset;
    private String[] data;

    public Long getOffset() {
        return offset;
    }

    public void setOffset(Long offset) {
        this.offset = offset;
    }

    public String[] getData() {
        return data;
    }

    public void setData(String[] data) {
        this.data = data;
    }
}
