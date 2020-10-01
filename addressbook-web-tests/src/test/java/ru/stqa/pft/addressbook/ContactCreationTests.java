package ru.stqa.pft.addressbook;

import org.testng.annotations.Test;

public class ContactCreationTests extends TestBase {

  @Test
  public void testContactCreation() throws Exception {
    goToCreateContactPage();
    fillContactForm(new ContactData("Batman", "Batman", "99025522208", "batman@test.com"));
    submitContactCreation();
    returnToHomePage();
  }

}
