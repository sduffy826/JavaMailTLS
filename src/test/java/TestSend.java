public class TestSend {

  public static void main(String[] args) {
    System.out.println("In TestSend.main()");
    
    // SendTextEmail.sendEmail("remi96hotmail.properties", "", "media3@us.ibm.com", "Test Subject", "Body of email text :)");
    
    System.out.println("After call to SendTextEmail.sendEmail");
    
    SendEmailWithAttachment.sendEmail("remi96hotmail.properties", "", "media3@us.ibm.com", "Test Subject", "Body of email text :)",
                                      "c:\\seanduff\\workspace\\test.txt");
  }
}
