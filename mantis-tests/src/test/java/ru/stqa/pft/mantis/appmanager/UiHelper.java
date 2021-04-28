package ru.stqa.pft.mantis.appmanager;

import org.openqa.selenium.By;

public class UiHelper extends HelperBase {

  public UiHelper(ApplicationManager app) {
    super(app);
  }

  public void login(String username, String password) {
    wd.get(app.getProperty("web.baseUrl") + "login_page.php");
    type(By.name("username"), username);
    click(By.cssSelector("input[type=submit]"));
    type(By.name("password"), password);
    click(By.cssSelector("input[type=submit]"));
  }

  public void goToManageUsers() {
    click(By.xpath("//a[@href='/mantisbt-2.24.2/manage_overview_page.php']"));
    click(By.xpath("//a[@href='/mantisbt-2.24.2/manage_user_page.php']"));
  }

  public void resetPasswordByUserName(String user) {
    click(By.xpath(String.format("//a[contains(text(), '%s')]", user)));
    click(By.xpath("//input[@value='Reset Password']"));
  }

  public void start(String userName) {
    login("administrator", "root");
    goToManageUsers();
    resetPasswordByUserName(userName);
  }

  public void finish(String confirmationLink, String password) {
    wd.get(confirmationLink);
    type(By.name("password"), password);
    type(By.name("password_confirm"), password);
    click(By.cssSelector("button[type=submit]"));
  }
}
