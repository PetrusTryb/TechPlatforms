package com.trybisz;

public record Mage(String name, int level) {

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
