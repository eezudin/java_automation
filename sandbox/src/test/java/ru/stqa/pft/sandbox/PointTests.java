package ru.stqa.pft.sandbox;

import org.testng.Assert;
import org.testng.annotations.Test;
import ru.stqa.pft.sandox.Point;

public class PointTests {

  @Test
  public void pointTest1() {
    Point p1 = new Point(3, 5);
    double expectedValue = 4.123105625617661;
    Assert.assertEquals(p1.distance(new Point(7, 4)), expectedValue);
  }

  @Test
  public void pointTest2() {
    Point p1 = new Point(2, 2);
    double expectedValue = 0.1;
    Assert.assertEquals(p1.distance(new Point(2, 2)), expectedValue);
  }

}
