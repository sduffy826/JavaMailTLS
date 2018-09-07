import java.util.Properties;

import javax.mail.Session;

public class SendTextEmail {

  public static void sendEmail(String propsFile, String overrideEmailPW, String destEmail, String subject, String bodyText) {
    Properties emailProps = PropertyHelper.getProperties(propsFile);  // Don't have to check null, getSession does
    Session mySession = EmailUtilities.getSession(emailProps, overrideEmailPW);
    if (mySession != null) {
      EmailUtilities.sendEmail(mySession, emailProps.getProperty("emailAddress"), destEmail, subject, bodyText);
    }    
  } 
}
