package ru.stqa.pft.addressbook.appmanager;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import ru.stqa.pft.addressbook.model.ContactData;

import java.util.ArrayList;
import java.util.List;

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

  public void editContact(int index) {
    wd.findElements(By.xpath("//img[@alt='Edit']")).get(index).click();
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

  public List<ContactData> getContactList() {
    List<ContactData> contacts = new ArrayList<>();
    List<WebElement> rows = wd.findElements(By.cssSelector("tr[name = entry]"));
    for (WebElement row : rows) {
      int id = Integer.parseInt(row.findElement(By.tagName("input")).getAttribute("value"));
      List<WebElement> cells = row.findElements(By.tagName("td"));
      contacts.add(new ContactData(id, cells.get(1).getText(), cells.get(2).getText(), null, null, null));
    }

    return contacts;
  }
}
