package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ContactDeletionTests extends TestBase {

  @BeforeMethod
  public static void runPreconditions() {
    app.goTo().homePage();
    if (app.contact().all().size() == 0) {
      app.contact().create( new ContactData().withFirstName("Batman").withLastName("Batman")
              .withMobilePhone("99025522208").withHomePhone("53513543").withWorkPhone("55112321345")
              .withEmail("batman@test.com").withEmail2("btman@test.com").withGroup("test1").withAddress("Gotham"));
    }
  }

  @Test
  public void testContactDeletion () {
    Contacts before = app.contact().all();
    ContactData deletedContact = before.iterator().next();
    app.contact().delete(deletedContact);
    assertThat(app.contact().count(), equalTo(before.size() - 1));
    Contacts after = app.contact().all();
    assertThat(after, equalTo(before.without(deletedContact)));
  }
}
