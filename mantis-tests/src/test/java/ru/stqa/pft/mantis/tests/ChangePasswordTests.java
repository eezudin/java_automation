package ru.stqa.pft.mantis.tests;

import org.testng.annotations.Test;
import ru.lanwen.verbalregex.VerbalExpression;
import ru.stqa.pft.mantis.model.MailMessage;
import ru.stqa.pft.mantis.model.UserData;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static org.testng.Assert.assertTrue;

public class ChangePasswordTests extends TestBase {


  @Test
  public void testChangePassword() throws IOException, MessagingException {
    UserData user = app.db().users().stream().filter(x -> !x.getName().equals("administrator"))
            .collect(Collectors.toList()).iterator().next();
    String newPassword = "P@zzvv0rcl";
    app.james().deleteUser(user.getName());
    app.james().createUser(user.getName(), user.getPassword());

    app.changePasswd().start(user.getName());
    List<MailMessage> mailMessages = app.james().waitForMail(user.getName(), user.getPassword(),  60000, 2);
    String confirmationLink = findConfirmationLink(mailMessages, user.getEmail());
    app.changePasswd().finish(confirmationLink, newPassword);
    assertTrue(app.newSession().login(user.getName(), newPassword));
  }

  private String findConfirmationLink(List<MailMessage> mailMessages, String email) {
    MailMessage mailMessage = mailMessages.stream().filter(m -> m.to.equals(email))
            .filter(m -> m.text.contains("Someone (presumably you) requested a password change")).findFirst().get();
    VerbalExpression regex = VerbalExpression.regex().find("http://").nonSpace().oneOrMore().build();
    return regex.getText(mailMessage.text);
  }
}
