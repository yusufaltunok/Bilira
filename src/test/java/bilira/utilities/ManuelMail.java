package bilira.utilities;

import javax.mail.MessagingException;
import java.io.IOException;
import java.security.GeneralSecurityException;

public class ManuelMail {

    public static void main(String[] args) throws MessagingException, GeneralSecurityException, IOException {
        GmailQuickstart gmailQuickstart = new GmailQuickstart();

        // Gmail'den digit değerini almak için öncelikle fetch metodunu çağır
        gmailQuickstart.fetchDigitFromGmail();
        String digitValue = gmailQuickstart.getDigit();

        // Değeri yazdır
        System.out.println("Alınan digit değeri: " + digitValue);
    }
}
