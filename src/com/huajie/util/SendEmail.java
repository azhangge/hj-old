package com.huajie.util;

import com.sun.net.ssl.internal.ssl.Provider;
import java.security.Security;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class SendEmail {

    private MimeMessage mimeMsg;
    private Session session;
    private Properties props;
    private String username = "";
    private String password = "";
    private Multipart mp;

    public SendEmail(String protocol, String host, String port, boolean ssl, boolean needAuth) {
        setSmtpHost("smtp", host, port);
        setNeedAuth(needAuth);
        setSSL(ssl, port);
        createMimeMessage();
    }

    public void setSmtpHost(String protocol, String hostName, String port) {
        if (this.props == null) {
            this.props = System.getProperties();
        }
        this.props.setProperty("mail.transport.protocol", protocol);
        this.props.setProperty("mail.smtp.port", port);
        this.props.put("mail.smtp.host", hostName);
    }

    public boolean createMimeMessage() {
        try {
            this.session = Session.getDefaultInstance(this.props, null);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        try {
            this.mimeMsg = new MimeMessage(this.session);
            this.mp = new MimeMultipart();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void setNeedAuth(boolean need) {
        if (this.props == null) {
            this.props = System.getProperties();
        }
        if (need) {
            this.props.put("mail.smtp.auth", "true");
        } else {
            this.props.put("mail.smtp.auth", "false");
        }
    }

    public void setSSL(boolean ssl, String port) {
        if (ssl) {
            Security.addProvider(new Provider());
            String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
            this.props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
            this.props.setProperty("mail.smtp.socketFactory.fallback", "true");
            this.props.setProperty("mail.smtp.socketFactory.port", port);
        }
    }

    public void setNamePass(String name, String pass) {
        this.username = name;
        this.password = pass;
    }

    public boolean setSubject(String mailSubject) {
        try {
            this.mimeMsg.setSubject(mailSubject);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean setBody(String mailBody) {
        BodyPart bp = new MimeBodyPart();
        try {
            bp.setContent(
                    "<html><head><meta http-equiv=Content-Type content=text/html; charset=utf-8></head><body>"
                    + mailBody + "</body></html>",
                    "text/html;charset=utf-8");
            this.mp.addBodyPart(bp);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean addFileAffix(String filename) {
        try {
            BodyPart bp = new MimeBodyPart();
            FileDataSource fileds = new FileDataSource(filename);
            bp.setDataHandler(new DataHandler(fileds));
            bp.setFileName(fileds.getName());

            this.mp.addBodyPart(bp);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean setFrom(String from, String alias) {
        try {
            this.mimeMsg.setFrom(new InternetAddress(from, alias, "UTF-8"));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean setTo(String to) {
        if (to == null) {
            return false;
        }
        try {
            this.mimeMsg.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to));
            return true;
        } catch (Exception e) {
        }
        return false;
    }

    public boolean setCopyTo(String copyto) {
        if (copyto == null) {
            return false;
        }
        try {
            this.mimeMsg.setRecipients(Message.RecipientType.CC,
                    InternetAddress.parse(copyto));
            return true;
        } catch (Exception e) {
        }
        return false;
    }

    public boolean sendout() {
        try {
            this.mimeMsg.setContent(this.mp);
            this.mimeMsg.saveChanges();

            Session mailSession = Session.getInstance(this.props, null);

            Transport transport = mailSession.getTransport("smtp");
            transport.connect((String) this.props.get("mail.smtp.host"), this.username,
                    this.password);
            transport.sendMessage(this.mimeMsg,
                    this.mimeMsg.getRecipients(Message.RecipientType.TO));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean sendEmail(String protocol, String smtpHost, String port, boolean needAuth, boolean ssl, String urn, String pwd, String fromAddr, String alias, String toAddr, String title, String content) {
        SendEmail themail = new SendEmail(protocol, smtpHost, port, needAuth, ssl);

        if (!themail.setSubject(title)) {
            return false;
        }
        if (!themail.setBody(content)) {
            return false;
        }
        if (!themail.setTo(toAddr)) {
            return false;
        }
        if (!themail.setFrom(fromAddr, alias)) {
            return false;
        }
        themail.setNamePass(urn, pwd);

        return themail.sendout();
    }

    public static void main(String[] args) {
        String mailbody = "<div align=center><a href=\"https://www.163.com\">this is a test</a></div>";

//     boolean ifs = sendEmail("smtp", "smtp.gmail.com", "465", 
//       true, true, "sxskw.admin", "litielitie", "sxskw.admin@gmail.com", "邮件主题", "lteb2002@163.com", 
//       "注册成功", mailbody);
        boolean ifs = sendEmail("smtp", "smtp.ym.163.com", "25",
                true, false, "poster@huajie.com", "123456", "poster@huajie.com", "邮件主题", "lteb2002@163.com",
                "注册成功", mailbody);

        System.out.println(ifs);
    }
}
