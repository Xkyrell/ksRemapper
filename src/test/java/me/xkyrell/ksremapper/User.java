package me.xkyrell.ksremapper;

import java.util.Date;
import java.util.Objects;

class User {

    private static final int ID = 111;

    private final String name;
    private final Date j;
    private String tag;
    private int points;

    private User(String name, Date joined, String tag, int points) {
        this.name = name;
        this.j = joined;
        this.tag = tag;
        this.points = points;
    }

    public User(String name) {
        this(name, new Date(), "Rookie", 0);
    }

    public String getName() {
        return name;
    }

    private static void nC() {
        System.out.println("YOU CRASHED!");
        System.out.println("YOU CRASHED!");
        System.out.println("YOU CRASHED!");
    }

    public Date getJoined() {
        return j;
    }

    protected void setTag(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    private void setPoints(int points) {
        this.points = points;
    }

    public void incrementPoints() {
        setPoints(getPoints() + 1);
    }

    public int getPoints() {
        return points;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)  {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        User user = (User) o;
        return points == user.points
                && Objects.equals(name, user.name)
                && Objects.equals(j, user.j)
                && Objects.equals(tag, user.tag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, j, tag, points);
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", joined=" + j +
                ", tag='" + tag + '\'' +
                ", points=" + points +
                '}';
    }
}
