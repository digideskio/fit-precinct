package de.konqi.fitapi.rest.openfitapi.resources;

/**
 * Created by konqi on 16.08.2015.
 */

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class LoginResponse {
    @XmlElement(name="session_name")
    String sessionName;
    @XmlElement(name="sessid")
    String sessionId;

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
