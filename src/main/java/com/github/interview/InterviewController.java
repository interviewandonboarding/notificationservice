package com.github.interview;

import com.github.util.CalendarInvite;
import com.github.util.TriFunction;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.activation.MimetypesFileTypeMap;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

@RestController
@RequestMapping("/interviewprocess")
public class InterviewController {

    private static final DateTimeFormatter INPUT_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HHmm");
    private static final DateTimeFormatter CALENDAR_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmm'00'");
    private static final String CALENDAR_MIME_TYPE = "text/calendar ics ICS";
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String MAIL_SMTP_HOST = "mail.smtp.host";
    private static final String MAIL_SMTP_START_TLS = "mail.smtp.starttls.enable";
    private static final String MAIL_SMTP_AUTH = "mail.smtp.auth";
    private static final String MAIL_SMTP_SOCKET_FACTORY = "mail.smtp.socketFactory.port";
    private static final String MAIL_SMTP_SOCKET_FACTORY_PORT = "465";
    private static final String MAIL_SMTP_PORT = "mail.smtp.port";

    private static final String user = "asdalidl76@gmail.com";
    private static final String password = "newasda123";

    @RequestMapping(value = "/sendCalendarInvite",method = RequestMethod.POST)
    public void sendCalendarInvite(@RequestParam("startDate") String start, @RequestParam("endDate") String end,@RequestParam("to") String to,@RequestParam("location") String location){
        MimetypesFileTypeMap mimetypes = (MimetypesFileTypeMap)MimetypesFileTypeMap.getDefaultFileTypeMap();
        mimetypes.addMimeTypes(CALENDAR_MIME_TYPE);

        String from = "asdalidl76@gmail.com";
        Session session = configureSession();
        MimeMessage message = new MimeMessage(session);

        try{
            message.setFrom(new InternetAddress(from));
            message.setSubject("Interview Schedule");
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            Multipart multipart = new MimeMultipart("alternative");
            BodyPart messageBodyPart = prepareMessage("Invitation mail for interview");
            multipart.addBodyPart(messageBodyPart);

            String startDate = LocalDateTime.parse(start,INPUT_DATE_TIME_FORMATTER).format(CALENDAR_TIME_FORMAT);
            String endDate = LocalDateTime.parse(end,INPUT_DATE_TIME_FORMATTER).format(CALENDAR_TIME_FORMAT);

            BodyPart calendarPart = prepareEventInvite(startDate,endDate,location);
            multipart.addBodyPart(calendarPart);
            message.setContent(multipart);

            Transport transport = session.getTransport("smtp");
            transport.connect();
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        }catch(MessagingException e){
            e.printStackTrace();
        }
    }

    private Session configureSession(){

        Properties properties = System.getProperties();
        properties.put(MAIL_SMTP_HOST,SMTP_HOST);
        properties.put(MAIL_SMTP_START_TLS,true);
        properties.put(MAIL_SMTP_AUTH, "true");
        properties.put(MAIL_SMTP_SOCKET_FACTORY, MAIL_SMTP_SOCKET_FACTORY_PORT);
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put(MAIL_SMTP_PORT, "465");

        return Session.getDefaultInstance(properties,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(user,password);
                    }
                });
    }


    private BodyPart prepareMessage(String content) throws MessagingException {
        MimeBodyPart descriptionPart = new MimeBodyPart();
        descriptionPart.setContent(content, "text/html; charset=utf-8");
        return descriptionPart;
    }

    private BodyPart prepareEventInvite(String startDate, String endDate, String location) throws MessagingException {

        BodyPart calendarPart = new MimeBodyPart();

        TriFunction<String,String,String,CalendarInvite.Builder> function = CalendarInvite.Builder::new;
        CalendarInvite.Builder builder = function.apply(startDate,endDate,location);

        String calendarContent = builder.build().toString();
        calendarPart.addHeader("Content-Class", "urn:content-classes:calendarmessage");
        calendarPart.setContent(calendarContent, "text/calendar;method=CANCEL");

        return calendarPart;
    }
}
