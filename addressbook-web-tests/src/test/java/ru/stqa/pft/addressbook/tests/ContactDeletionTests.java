package ru.stqa.pft.addressbook.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;

import java.util.List;

public class ContactDeletionTests extends TestBase {

  @Test
  public void testContactDeletion () {
    app.getNavigationHelper().goToHomePage();
    if (!app.getContactHelper().isContactExist()) {
      app.getContactHelper().createContact( new ContactData("Batman", "Batman", "99025522208", "batman@test.com", "test1"));
    }
    List<ContactData> before = app.getContactHelper().getContactList();
    app.getContactHelper().selectItem(before.size() - 1);
    app.getContactHelper().deleteSelectedContacts();
    app.getContactHelper().acceptAlert();
    List<ContactData> after = app.getContactHelper().getContactList();
    Assert.assertEquals(after.size(), before.size() - 1);

    before.remove(before.size() - 1);
    Assert.assertEquals(after, before);
  }
}
