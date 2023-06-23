package com.parkinglot.beans;
public class LotPlan {
    long Id;
    String name;
    String displayName;
    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return "LotPlan{" +
                "Id=" + Id +
                ", name='" + name + '\'' +
                '}';
    }
}
