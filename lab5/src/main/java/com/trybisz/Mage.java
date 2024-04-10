package com.trybisz;
public class Mage {
    private final String name;
    private final int level;

    public Mage(String name, int level) {
        this.name = name;
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    @Override
    public String toString() {
        return "Mage{" +
                "name='" + name + '\'' +
                ", level=" + level +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Mage mage = (Mage) o;

        if (level != mage.level) return false;
        return name.equals(mage.name);
    }
}
