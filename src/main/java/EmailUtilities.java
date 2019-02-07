//import java.io.UnsupportedEncodingException;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
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

public class EmailUtilities {
  public static final boolean DEBUGIT = true;
  /** 
   * Most of the logic to get the session is common, moved it here.
   */
  public static Session getSession(Properties emailProps, String overrideEmailPW) {
    Session session = null;
    if (emailProps != null) {      
      final String fromEmail = emailProps.getProperty("emailAddress","").trim();
      final String password = (overrideEmailPW.length() > 0 ? overrideEmailPW : emailProps.getProperty("emailPassword","")).trim();  
      if (DEBUGIT) System.out.println("email id: " + fromEmail + " pw: " + password );
      
      if (fromEmail.length() > 0 && password.length() > 0) {
        Properties props = new Properties();
        props.put("mail.smtp.host", emailProps.getProperty("smtpHost")); //SMTP Host
        props.put("mail.smtp.port", emailProps.getProperty("smtpPort")); //TLS Port
        props.put("mail.smtp.auth", "true"); //enable authentication
        props.put("mail.smtp.starttls.enable", "true"); // enable STARTTLS
        props.put("mail.debug", emailProps.getProperty("debugFlag","false")); // Debugging flag
            
        //create Authenticator object to pass in Session.getInstance argument
        Authenticator auth = new Authenticator() {
          // override the getPasswordAuthentication method
          protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(fromEmail,password);
          }
        };
        session = Session.getInstance(props, auth);
      }
    }    
    return session;
  } 
    
  /**
   * Utility method to send simple HTML email
   * 
   * @param session
   * @param toEmail
   * @param subject
   * @param body
   */
  public static boolean sendEmail(Session session, String fromEmail, String toEmail, String subject, String body) {
    boolean success = false;
    try {
      MimeMessage msg = new MimeMessage(session);
      // set message headers
      msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
      msg.addHeader("format", "flowed");
      msg.addHeader("Content-Transfer-Encoding", "8bit");
      
      // Set from
      msg.setFrom(new InternetAddress(fromEmail));
      msg.setReplyTo(InternetAddress.parse(fromEmail,false));

      // Set subject/body/date 
      msg.setSubject(subject, "UTF-8");
      msg.setText(body, "UTF-8");
      msg.setSentDate(new Date());

      // Set recipient
      msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
      
      Transport.send(msg);

      success = true;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return success;
  }

  /**
   * Send email with attachment, for attachment specify full path to file location
   * 
   * @param session
   * @param fromEmail
   * @param toEmail
   * @param subject
   * @param body
   * @param fileNameAndPath
   * @return
   */
  public static boolean sendAttachmentEmail(Session session, String fromEmail, String toEmail, 
                                            String subject, String body, String fileNameAndPath) {
    boolean success = false;
    
    try {      
      Path path = Paths.get(fileNameAndPath);
      
      if (Files.exists(path) == false ) throw new FileNotFoundException("Could not find: " + fileNameAndPath);  
      
      Path fileNameNoPath = path.getFileName();  // This is name shown in email (don't want path displayed)      
      
      MimeMessage msg = new MimeMessage(session);
      msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
      msg.addHeader("format", "flowed");
      msg.addHeader("Content-Transfer-Encoding", "8bit");

      msg.setFrom(new InternetAddress(fromEmail));
      msg.setReplyTo(InternetAddress.parse(fromEmail,false));

      msg.setSubject(subject, "UTF-8");

      msg.setText(body, "UTF-8");

      msg.setSentDate(new Date());
      
      msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));

      // Create the message body part
      BodyPart messageBodyPart = new MimeBodyPart();

      // Fill message
      messageBodyPart.setText(body);

      // Create multipart message for the attachment
      Multipart multipart = new MimeMultipart();

      // Add text body part to multipart
      multipart.addBodyPart(messageBodyPart);

      // Add an attachment
      messageBodyPart = new MimeBodyPart();      
      DataSource source = new FileDataSource(fileNameAndPath);
      messageBodyPart.setDataHandler(new DataHandler(source));

      // messageBodyPart.setFileName(fileNameAndPath); (this has path and file)
      messageBodyPart.setFileName(fileNameNoPath.toString());
      
      multipart.addBodyPart(messageBodyPart);  
     
      // Set message content to the multipart
      msg.setContent(multipart);

      // Send msg
      Transport.send(msg);
      success = true;
    } catch (Exception e) {
      e.printStackTrace();
    }   
    return success;
  }
}
