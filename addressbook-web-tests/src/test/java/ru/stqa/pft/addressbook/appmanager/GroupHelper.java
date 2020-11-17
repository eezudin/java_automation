package ru.stqa.pft.addressbook.appmanager;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import ru.stqa.pft.addressbook.model.GroupData;
import ru.stqa.pft.addressbook.model.Groups;

import java.util.*;

public class GroupHelper extends HelperBase {

  public GroupHelper(WebDriver wd) {
    super(wd);
  }

  public void returnToGroupPage() {
    click(By.linkText("group page"));
  }

  public void submitGroupCreation() {
    click(By.name("submit"));
  }

  public void initGroupCreation() {
    click(By.name("new"));
  }

  public void fillGroupForm(GroupData groupData) {
    type(By.name("group_name"), groupData.getName());
    type(By.name("group_header"), groupData.getHeader());
    type(By.name("group_footer"), groupData.getFooter());
  }

  public void deleteSelectedGroups() {
    click(By.name("delete"));
  }

  public void initGroupModification() {
    click(By.name("edit"));
  }

  public void markGroupById(int id) {
    wd.findElement(By.cssSelector("input[value='" + id + "']")).click();
  }

  public void create(GroupData groupData) {
    initGroupCreation();
    fillGroupForm(groupData);
    submitGroupCreation();
    cashedGroups = null;
    returnToGroupPage();
  }

  public void modify(GroupData group) {
    markGroupById(group.getId());
    initGroupModification();
    fillGroupForm(group);
    submitModification();
    cashedGroups = null;
    returnToGroupPage();
  }

  public void delete(GroupData group) {
    markGroupById(group.getId());
    deleteSelectedGroups();
    cashedGroups = null;
    returnToGroupPage();
  }

  public boolean isGroupExist() {
    return isItemExist();
  }

  public int count() {
    return wd.findElements(By.name("selected[]")).size();
  }

  private Groups cashedGroups = null;

  public Groups all() {
    if (cashedGroups != null) {
      return new Groups(cashedGroups);
    }
    Groups cashedGroups = new Groups();
    List<WebElement> elements = wd.findElements(By.cssSelector("span.group"));
    for (WebElement element : elements) {
      int id = Integer.parseInt(element.findElement(By.tagName("input")).getAttribute("value"));
      cashedGroups.add(new GroupData().withId(id).withName(element.getText()));
    }
    return new Groups(cashedGroups);
  }

  public void selectGroupByName(String group) {
    new Select(wd.findElement(By.xpath("//select[@name='group']"))).selectByVisibleText(group);
  }

  public void removeFromGroup() {
    wd.findElement(By.xpath("//input[@name='remove']")).click();
  }


}
