package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.Test;

public class ContactDeletionTests extends TestBase {

  @Test
  public void testContactDeletion () {
    app.getNavigationHelper().goToHomePage();
    app.getContactHelper().selectItem();
    app.getContactHelper().deleteSelectedContacts();
    app.getContactHelper().acceptAlert();
  }
}
