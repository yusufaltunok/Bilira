package bilira.utilities;

import javax.mail.MessagingException;
import java.io.IOException;
import java.security.GeneralSecurityException;

public class ManuelMail {

    public static void main(String[] args) throws MessagingException, GeneralSecurityException, IOException {
        GmailQuickstart gmailQuickstart = new GmailQuickstart(
                "testquality1tester@gmail.com",
                "Tokens/Mustafa",
                "src/test/resources/mustafa.json"
        );

        // Mesajlardan 6 haneli kodu çek
        gmailQuickstart.fetchDigitFromGmail();
        // Şimdi digit değerini alabiliriz
        String digitValue = gmailQuickstart.getDigit();
        System.out.println("digitValue = " + digitValue);
    }
}
