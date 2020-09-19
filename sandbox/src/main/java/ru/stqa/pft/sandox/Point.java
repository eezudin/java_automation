package ru.stqa.pft.sandox;

public class Point {

  public double x;
  public double y;

  public Point(double x, double y) {
    this.x = x;
    this.y = y;
  }

  public double distance(Point secondPoint) {
    return Math.sqrt(((secondPoint.x - this.x) * (secondPoint.x - this.x)) + ((secondPoint.y - this.y) * (secondPoint.y - this.y)));
  }

}
