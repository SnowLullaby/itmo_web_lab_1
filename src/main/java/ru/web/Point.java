package ru.web;

public class Point {
    private final double x;
    private final double y;
    private final double r;
    private final boolean hit;

    public Point(double x, double y, double r, boolean hit) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.hit = hit;
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public double getR() { return r; }
    public boolean getHit() { return hit; }

    @Override
    public String toString() {
        return String.format("{\"x\":%.2f,\"y\":%.2f,\"r\":%.2f,\"hit\":%b}",
                x, y, r, hit);
    }
}