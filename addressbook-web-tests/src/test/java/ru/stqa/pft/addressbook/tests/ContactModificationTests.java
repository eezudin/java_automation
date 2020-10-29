package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ContactModificationTests extends TestBase {

  @BeforeMethod
  public static void runPreconditions() {
    app.goTo().homePage();
    if (app.contact().all().size() == 0) {
      app.contact().create(
              new ContactData().withFirstName("Batman").withLastName("Batman")
                      .withMobilePhone("99025522208").withHomePhone("21348").withWorkPhone("54218465")
                      .withEmail("batman@test.com").withEmail3("btmn@test.com").withGroup("test1").withAddress("Gotham"));
    }
  }

  @Test
  public void testContactModification() {
    Contacts before = app.contact().all();
    ContactData modifiedContact = before.iterator().next();
    ContactData contact = new ContactData().withId(modifiedContact.getId()).withFirstName("Batman").withLastName("Batman")
            .withMobilePhone("99025522208").withHomePhone("35453").withWorkPhone("456345123").withEmail("batman@test.com")
            .withEmail2("batman@test.com").withAddress("Gotham");
    app.contact().modify(contact);
    assertThat(app.contact().count(), equalTo(before.size()));
    Contacts after = app.contact().all();
    assertThat(after.size(), equalTo(before.size()));
    assertThat(after, equalTo(before.withModified(contact)));
  }
}
