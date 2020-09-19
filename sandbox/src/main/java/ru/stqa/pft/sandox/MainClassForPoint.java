package ru.stqa.pft.sandox;

public class MainClassForPoint {

  public static void main(String[] args) {

    Point p1 = new Point(2, 4);
    Point p2 = new Point(4, 1);

    System.out.println("Растояние между точками равно " + distance(p1, p2));

    System.out.println("Растояние между точками равно " + p1.distance(p2));
    System.out.println("Растояние между точками равно " + p2.distance(p1));

  }

  public static double distance(Point p1, Point p2) {
    return Math.sqrt(((p2.x - p1.x) * (p2.x - p1.x)) + ((p2.y - p1.y) * (p2.y - p1.y)));
  }
}
