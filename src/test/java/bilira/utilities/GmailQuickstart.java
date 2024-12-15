package bilira.utilities;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import org.apache.commons.codec.binary.Base64;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import javax.mail.*;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GmailQuickstart {

    private static final String APPLICATION_NAME = "MailProject";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "Tokens/ReceiveTokens";
    private static final String user = "qatester1532@gmail.com";

    // 6 haneli sayıyı saklamak için sınıf değişkeni
    private String digit;

    public String getDigit() {
        return digit; // Getter metodu ile erişim sağlanır
    }

    public void setDigit(String digit) {
        this.digit = digit; // Setter metodu ile değer ataması yapılır
    }

    private static final List<String> SCOPES = Collections.singletonList(GmailScopes.GMAIL_READONLY);
    private static final String CREDENTIALS_FILE_PATH = "/test.json";

    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        InputStream in = GmailQuickstart.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    public void fetchDigitFromGmail() throws IOException, GeneralSecurityException, MessagingException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Gmail service = new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();

        // Kullanıcının mesajlarını listeleme
        ListMessagesResponse messagesResponse = service.users().messages().list(user).setMaxResults(1L).execute();
        List<Message> messages = messagesResponse.getMessages();

        if (messages == null || messages.isEmpty()) {
            System.out.println("Hiç mesaj bulunamadı.");
        } else {
            String messageId = messages.get(0).getId();
            Message message = service.users().messages().get(user, messageId).setFormat("raw").execute();

            // Mesaj içeriğini çözümleme
            byte[] emailBytes = Base64.decodeBase64(message.getRaw());
            Properties props = new Properties();
            Session session = Session.getDefaultInstance(props, null);
            MimeMessage email = new MimeMessage(session, new ByteArrayInputStream(emailBytes));

            // Mesajın içeriğini al
            String content = extractMessageContent(email);

            // 6 haneli sayıyı regex ile bul
            String sixDigitCode = extractSixDigitCodeFromHTML(content);

            // Sınıf değişkenine atama
            setDigit(sixDigitCode); // sınıf değişkenine atama
        }
    }

    private static String extractSixDigitCodeFromHTML(String content) {
        // HTML içeriği çözümleme
        Document doc = Jsoup.parse(content);

        // <span> veya <p> gibi etiketlerde sayıyı arıyoruz
        for (Element element : doc.select("span, p")) {
            String text = element.text();
            // Regex ile 6 haneli sayıyı arıyoruz
            Pattern pattern = Pattern.compile("\\b\\d{6}\\b");
            Matcher matcher = pattern.matcher(text);
            if (matcher.find()) {
                return matcher.group(); // Bulunan ilk 6 haneli sayıyı döndür
            }
        }
        return null;
    }

    private static String extractMessageContent(MimeMessage email) throws MessagingException, IOException {
        Object content = email.getContent();

        if (content instanceof String) {
            // Eğer içerik düz metinse doğrudan döner
            return (String) content;
        } else if (content instanceof MimeMultipart) {
            // Eğer içerik multipart (HTML) ise, düz metin kısmını çıkar
            MimeMultipart multipart = (MimeMultipart) content;
            return getTextFromMimeMultipart(multipart);
        }

        return ""; // İçerik alınamıyorsa boş string döner
    }

    private static String getTextFromMimeMultipart(MimeMultipart multipart) throws MessagingException, IOException {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < multipart.getCount(); i++) {
            BodyPart bodyPart = multipart.getBodyPart(i);
            if (bodyPart.isMimeType("text/plain")) {
                result.append(bodyPart.getContent().toString());
            } else if (bodyPart.isMimeType("text/html")) {
                // HTML içeriğini de işleyelim
                result.append(bodyPart.getContent().toString());
            }
        }
        return result.toString();
    }
}
