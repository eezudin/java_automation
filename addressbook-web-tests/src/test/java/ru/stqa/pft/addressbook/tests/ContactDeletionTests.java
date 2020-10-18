package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;

public class ContactDeletionTests extends TestBase {

  @Test
  public void testContactDeletion () {
    app.getNavigationHelper().goToHomePage();
    if (!app.getContactHelper().isContactExist()) {
      app.getContactHelper().createContact( new ContactData("Batman", "Batman", "99025522208", "batman@test.com", "test1"));
    }
    app.getContactHelper().selectItem(0);
    app.getContactHelper().deleteSelectedContacts();
    app.getContactHelper().acceptAlert();
  }
}
