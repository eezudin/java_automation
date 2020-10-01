package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;

public class ContactCreationTests extends TestBase {

  @Test
  public void testContactCreation() throws Exception {
    app.goToCreateContactPage();
    app.fillContactForm(new ContactData("Batman", "Batman", "99025522208", "batman@test.com"));
    app.submitContactCreation();
    app.returnToHomePage();
  }

}
