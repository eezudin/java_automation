package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;
import ru.stqa.pft.addressbook.model.GroupData;

import java.io.File;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ContactDeletionTests extends TestBase {

  @BeforeMethod
  public static void runPreconditions() {
    if (app.db().groups().size() == 0) {
      app.goTo().groupPage();
      app.group().create(new GroupData().withName("test1"));
    }
    app.goTo().homePage();
    if (app.db().contacts().size() == 0) {
      app.contact().create( new ContactData().withFirstName("Batman").withLastName("Batman")
              .withMobilePhone("99025522208").withHomePhone("53513543").withWorkPhone("55112321345")
              .withPhoto(new File("src/test/resources/pic.jpg"))
              .withEmail("batman@test.com").withEmail2("btman@test.com").inGroup(app.db().groups().iterator().next())
              .withAddress("Gotham"));
    }
  }

  @Test
  public void testContactDeletion () {
    Contacts before = app.db().contacts();
    ContactData deletedContact = before.iterator().next();
    app.contact().delete(deletedContact);
    assertThat(app.contact().count(), equalTo(before.size() - 1));
    Contacts after = app.db().contacts();
    assertThat(after, equalTo(before.without(deletedContact)));
    verifyContactListInUI();
  }
}
