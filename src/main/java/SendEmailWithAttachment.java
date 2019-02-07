import java.util.Properties;

import javax.mail.Session;

import com.corti.PropertyHelper;

public class SendEmailWithAttachment {
 
  public static void sendEmail(String propsFile, String overrideEmailPW, String destEmail, String subject, String bodyText, 
                               String fileNameAndPath) {     
    Properties emailProps = PropertyHelper.getPropertyObject(propsFile);  // Don't have to check null, getSession does
    Session mySession = EmailUtilities.getSession(emailProps, overrideEmailPW);
    if (mySession != null) {
      EmailUtilities.sendAttachmentEmail(mySession, emailProps.getProperty("emailAddress"), 
                                         destEmail, subject, bodyText, fileNameAndPath);
    }
  }
}
