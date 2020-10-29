package ru.stqa.pft.addressbook.appmanager;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;

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
    type(By.name("home"), contactData.getHomePhone());
    type(By.name("mobile"), contactData.getMobilePhone());
    type(By.name("work"), contactData.getWorkPhone());
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

  public void create(ContactData contactData) {
    initContactCreation();
    fillContactForm(contactData, true);
    submitContactCreation();
    cashedContacts = null;
    returnToHomePage();
  }

  public void modify(ContactData contact) {
    editContactById(contact.getId());
    fillContactForm(contact, false);
    submitModification();
    cashedContacts = null;
    returnToHomePage();
  }

  public void delete(ContactData contact) {
    selectContactById(contact.getId());
    deleteSelectedContacts();
    acceptAlert();
    cashedContacts = null;
  }

  private void editContactById(int id) {
   // wd.findElement(By.xpath("//input[@value='" + id + "']/../following-sibling::td/a/img[@Title='Edit']")).click();
    wd.findElement(By.xpath(String.format("//input[@value='%s']/../following-sibling::td/a/img[@Title='Edit']", id))).click();
  }

  private void selectContactById(int id) {
    wd.findElement(By.cssSelector("input[value='" + id + "']")).click();
  }

  private Contacts cashedContacts = null;

  public Contacts all() {
    if (cashedContacts != null) {
      return new Contacts(cashedContacts);
    }
    Contacts cashedContacts = new Contacts();
    List<WebElement> rows = wd.findElements(By.cssSelector("tr[name = entry]"));
    for (WebElement row : rows) {
      int id = Integer.parseInt(row.findElement(By.tagName("input")).getAttribute("value"));
      List<WebElement> cells = row.findElements(By.tagName("td"));
      String[] phones = cells.get(5).getText().split("\n");
      cashedContacts.add(new ContactData().withId(id).withFirstName(cells.get(1).getText()).withLastName(cells.get(2).getText())
      .withHomePhone(phones[0]).withMobilePhone(phones[1]).withWorkPhone(phones[2]));
    }
    return new Contacts(cashedContacts);
  }

  public int count() {
    return wd.findElements(By.name("selected[]")).size();
  }

  public ContactData infoFromEditForm(ContactData contact) {
    editContactById(contact.getId());
    String firstName = wd.findElement(By.name("firstname")).getAttribute("value");
    String lastName = wd.findElement(By.name("lastname")).getAttribute("value");
    String home = wd.findElement(By.name("home")).getAttribute("value");
    String mobile = wd.findElement(By.name("mobile")).getAttribute("value");
    String work = wd.findElement(By.name("work")).getAttribute("value");
    wd.navigate().back();
    return new ContactData().withFirstName(firstName).withLastName(lastName).withHomePhone(home)
            .withMobilePhone(mobile).withWorkPhone(work);
  }
}
