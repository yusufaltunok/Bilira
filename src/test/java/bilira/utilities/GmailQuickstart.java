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
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GmailQuickstart {

    private static final String APPLICATION_NAME = "MailProject";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private String tokensDirectoryPath;
    private String user;
    private String credentialsFilePath;

    private String digit; // 6 haneli sayıyı saklamak için sınıf değişkeni
    private String lastMessageId; // Son gelen mesajın ID'sini saklamak için sınıf değişkeni

    public GmailQuickstart(String user, String tokensDirectoryPath, String credentialsFilePath) {
        this.user = user;
        this.tokensDirectoryPath = tokensDirectoryPath;
        this.credentialsFilePath = credentialsFilePath;
        this.lastMessageId = ""; // Başlangıçta herhangi bir mesaj ID'si yok
    }

    public String getDigit() {
        return digit; // Getter metodu ile erişim sağlanır
    }

    // Yeni mesaj geldiğinde eski mesajları silip, yeni mesajı işleyen metot
    public void fetchAndProcessNewEmail() throws IOException, GeneralSecurityException, MessagingException, InterruptedException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Gmail service = new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();

        int maxAttempts = 10; // Maksimum deneme sayısı (yeni mesaj için)
        int attempt = 0;
        boolean newMessageFound = false;

        while (attempt < maxAttempts && !newMessageFound) {
            // Yeni mesajı kontrol et
            ListMessagesResponse messagesResponse = service.users().messages().list(user).setMaxResults(1L).execute();
            List<Message> messages = messagesResponse.getMessages();

            if (messages != null && !messages.isEmpty()) {
                String messageId = messages.get(0).getId();

                // Eğer son mesaj ile yeni gelen mesajın ID'si farklıysa işle
                if (!messageId.equals(lastMessageId)) {
                    // Yeni mesajı al
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

                    if (sixDigitCode != null) {
                        // OTP kodu bulundu
                        digit = sixDigitCode;
                        System.out.println("Yeni gelen 6 haneli kod: " + sixDigitCode);
                        newMessageFound = true;  // Yeni mesaj bulundu, döngüyü sonlandır

                        // Mesajı sil
                        deleteMessage(service, messageId);
                    } else {
                        System.out.println("Geçerli bir OTP kodu bulunamadı.");
                    }

                    // Son mesaj ID'sini güncelle
                    lastMessageId = messageId;
                } else {
                    System.out.println("Henüz yeni bir mesaj gelmedi.");
                }
            } else {
                System.out.println("Yeni mesaj yok.");
            }

            if (!newMessageFound) {
                // Yeni mesaj gelmediyse belirli bir süre bekle
                System.out.println("Yeni mesaj gelmedi, " + (attempt + 1) + " deneme yapıldı. Bekleniyor...");
                attempt++;
                Thread.sleep(5000);  // 5 saniye bekle
            }
        }

        if (!newMessageFound) {
            System.out.println("Max deneme sayısına ulaşıldı, yeni mesaj alınamadı.");
        }
    }

    // Mesajı silme metodu
    private void deleteMessage(Gmail service, String messageId) throws IOException {
        service.users().messages().delete(user, messageId).execute();
        System.out.println("Mesaj silindi: " + messageId);
    }

    private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        InputStream in = new FileInputStream(this.credentialsFilePath);
        if (in == null) {
            throw new FileNotFoundException("Credential file not found: " + this.credentialsFilePath);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, Collections.singletonList(GmailScopes.MAIL_GOOGLE_COM))
                .setDataStoreFactory(new FileDataStoreFactory(new File(this.tokensDirectoryPath)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
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

    public static void main(String[] args) throws IOException, GeneralSecurityException, MessagingException, InterruptedException {
        // Kullanıcı, token dizini ve credential dosyası bilgilerini sağlayın
        GmailQuickstart gmailQuickstart = new GmailQuickstart(
                ConfigReader.getProperty("email2"), // E-posta adresi
                ConfigReader.getProperty("tokenDirectoryPath"), // Tokenlerin saklandığı dizin
                ConfigReader.getProperty("jsonDirectoryPath") // Credential dosyasının yolu
        );

        // Yeni gelen mesajı işle
        gmailQuickstart.fetchAndProcessNewEmail();
        System.out.println("6 Haneli Kod: " + gmailQuickstart.getDigit());
    }
}
