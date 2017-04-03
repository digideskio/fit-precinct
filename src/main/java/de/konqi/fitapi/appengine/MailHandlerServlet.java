package de.konqi.fitapi.appengine;

import de.konqi.fitapi.common.importer.PwxImporter;
import lombok.extern.slf4j.Slf4j;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
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
        String requestURI = URLDecoder.decode(req.getRequestURI(), "UTF-8");
        log.info("Mail Handler Servlet, request path:" + requestURI);
        int from = requestURI.lastIndexOf('/') + 1;
        int to = requestURI.indexOf('@');
        String localPart = requestURI.substring(from, to);
        log.info("local part:" + localPart);

        MailTypes mailType = Enum.valueOf(MailTypes.class, localPart.toUpperCase());

        Properties properties = new Properties();
        Session session = Session.getDefaultInstance(properties, null);
        try {
            MimeMessage msg = new MimeMessage(session, req.getInputStream());
            if (msg.getFrom().length != 1) {
                throw new IllegalArgumentException("Message must have a single sender address.");
            }

            Address address = msg.getFrom()[0];
            String senderEmail = toString();
            if (address instanceof InternetAddress) {
                senderEmail = ((InternetAddress) address).getAddress();
            }
            log.info("Email from: " + senderEmail);

            Object content = msg.getContent();
            if (content instanceof String) {
                log.info("Text message: " + content);
            } else if (content instanceof Multipart) {
                log.info("Multipart message.");

                Multipart multipart = (Multipart) content;
                for (int i = 0; i < multipart.getCount(); i++) {
                    BodyPart bodyPart = multipart.getBodyPart(i);
                    String fileName = bodyPart.getFileName();
                    if (fileName != null && !fileName.isEmpty()) {
                        log.info("Filename: " + fileName);
                        if (fileName.toLowerCase().matches("^.*\\." + mailType.getExtension())) {
                            log.info("Found PWX attachment. Attempting to import file for user with email '" + senderEmail + "'.");
                            if (bodyPart.getContent() instanceof InputStream) {
                                InputStream is = (InputStream) bodyPart.getContent();
                                mailType.getImporter().importFromStream(senderEmail, is);
                            }
                        }
                    }

                    log.info(bodyPart.getContent().getClass().getName());
                }
            }
        } catch (MessagingException e) {
            log.error("Unable to parse message.", e);
        }
    }
}
