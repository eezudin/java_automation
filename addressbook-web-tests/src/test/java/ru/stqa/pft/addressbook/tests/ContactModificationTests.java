package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;

public class ContactModificationTests extends TestBase {

  @Test
  public void testContactModification() {
    app.getNavigationHelper().goToHomePage();
    if (!app.getContactHelper().isContactExist()) {
      app.getContactHelper().createContact(
              new ContactData("Batman", "Batman", "99025522208", "batman@test.com", "test1"));
    }
    app.getContactHelper().editContact();
    app.getContactHelper().fillContactForm(
            new ContactData(
                    "Batman", "Batman", "99025522208", "batman@test.com", null),
            false);
    app.getContactHelper().submitModification();
    app.getContactHelper().returnToHomePage();
  }
}
