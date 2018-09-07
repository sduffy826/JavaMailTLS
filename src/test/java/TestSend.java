
public class TestSend {

  public static void main(String[] args) {
    System.out.println("In mainline");
    
    //SendTextEmail.sendEmail("corti.properties", "", "media3@us.ibm.com", "Test Subject", "Body of email text :)");
    
    System.out.println("After call to SendTextEmail.sendEmail");
    
   SendEmailWithAttachment.sendEmail("corti.properties", "", "remi_96@hotmail.com", "Test Subject", "Body of email text :)",
                            "c:\\cole_smaller.jpg");

  }

}
