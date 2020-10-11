package ru.stqa.pft.addressbook.appmanager;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import ru.stqa.pft.addressbook.model.ContactData;

public class ContactHelper extends HelperBase {

  public ContactHelper(WebDriver wd) {
    super(wd);
  }

  public void submitContactCreation() {
    click(By.xpath("(//input[@name='submit'])[2]"));
  }

  public void fillContactForm(ContactData contactData, boolean creation) {
    type(By.name("firstname"), contactData.getFirstName());
    type(By.name("lastname"), contactData.getLastName());
    type(By.name("mobile"), contactData.getPhoneNumber());
    type(By.name("email"), contactData.getEmail());

    if (creation) {
      try {
        new Select(wd.findElement(By.name("new_group"))).selectByVisibleText(contactData.getGroup());
      } catch (NoSuchElementException e) {
        System.out.println("No groups exist: " + e.getMessage());
        e.printStackTrace();
        System.out.println("Contact will be created without any group");
      }
    } else {
      Assert.assertFalse(isElementPresent(By.name("new_group")), "This select should not be present");
    }
  }

  public void returnToHomePage() {
    click(By.linkText("home page"));
  }

  public void editContact() {
    click(By.xpath("//img[@alt='Edit']"));
  }

  public void deleteSelectedContacts() {
    click(By.xpath(".//input[@value ='Delete']"));
  }

  public boolean isContactExist() {
    return isItemExist();
  }

  public void initContactCreation() {
    click(By.xpath(".//a[.='add new']"));
  }

  public void createContact(ContactData contactData) {
    initContactCreation();
    fillContactForm(contactData, true);
    submitContactCreation();
    returnToHomePage();
  }

}
