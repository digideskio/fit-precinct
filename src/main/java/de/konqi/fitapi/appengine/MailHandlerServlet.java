package de.konqi.fitapi.appengine;

import lombok.extern.slf4j.Slf4j;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by konqi on 08.04.2016.
 */
@Slf4j
public class MailHandlerServlet extends HttpServlet {


    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        while (config.getInitParameterNames().hasMoreElements()) {
            Object o = config.getInitParameterNames().nextElement();
            log.info(o.toString());
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("Mail Handler Servlet, request path:" + req.getServletPath());

        Properties properties = new Properties();
        Session session = Session.getDefaultInstance(properties, null);
        try {
            MimeMessage msg = new MimeMessage(session, req.getInputStream());
            if (msg.getFrom().length != 1) {
                throw new IllegalArgumentException("Message must have a single sender address.");
            }
            log.info(msg.getFrom()[0].toString());

            Object content = msg.getContent();
            if (content instanceof String) {
                log.info("Text message: " + content);
            } else if (content instanceof Multipart) {
                log.info("Multipart message.");

                Multipart multipart = (Multipart) content;
                for (int i = 0; i < multipart.getCount(); i++) {
                    BodyPart bodyPart = multipart.getBodyPart(i);
                    if (!bodyPart.getFileName().isEmpty()) {
                        log.info("Filename: " + bodyPart.getFileName());
                    }
                    
                    log.info(bodyPart.getContent().getClass().getName());
                }
            }
        } catch (MessagingException e) {
            log.error("Unable to parse message.", e);
        }
    }
}
