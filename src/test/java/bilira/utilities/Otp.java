package bilira.utilities;

import com.mailosaur.MailosaurClient;
import com.mailosaur.MailosaurException;
import com.mailosaur.models.Message;
import com.mailosaur.models.MessageSearchParams;
import com.mailosaur.models.SearchCriteria;
import org.junit.Test;

import java.io.IOException;

public class Otp {

    String apiKey = "xecsQqtJWcU7cjSTxG7F6sjq8L4dk5DO";
    String serverId = "sgqlycjp";
    String serverDomain = "sgqlycjp.mailosaur.net";

    public String getRandomEmail() {
        return "user" + System.currentTimeMillis() + "@" + serverDomain;
    }


    public String getSubJect() throws MailosaurException, IOException {
        String emailId = getRandomEmail();
        System.out.println("emailId = " + emailId);
        MailosaurClient mailosaur = new MailosaurClient(apiKey);

        MessageSearchParams params = new MessageSearchParams();
        params.withServer(serverId);

        SearchCriteria criteria = new SearchCriteria();
        criteria.withSentTo(emailId);

        Message message = mailosaur.messages().get(params, criteria);
        String subject = message.subject();

        System.out.println("message.subject() = " + message.subject());
        return subject;

    }

}



