package com.trybisz;

import java.util.*;

public class Mage implements Comparable<Mage>{
    private final String name;
    private final int level;
    private final double power;
    private final Set<Mage> apprentices;

    public Mage(String name, int level, double power, SortOrder apprenticeSortOrder) {
        this.name = name;
        this.level = level;
        this.power = power;
        if(apprenticeSortOrder == SortOrder.SORT_NONE){
            apprentices = new HashSet<>();
        }
        else if(apprenticeSortOrder == SortOrder.SORT_PRIMARY) {
            apprentices = new TreeSet<>();
        }
        else{
            apprentices = new TreeSet<>(new MageComparator());
        }
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public double getPower() {
        return power;
    }

    public Set<Mage> getApprentices() {
        return Collections.unmodifiableSet(apprentices);
    }

    public void addApprentice(Mage apprentice) {
        apprentices.add(apprentice);
    }

    public int countDescendants(){
        int descendantsCount = apprentices.size();
        for (Mage apprentice : apprentices){
            descendantsCount+=apprentice.countDescendants();
        }
        return descendantsCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mage mage = (Mage) o;
        return level == mage.level && Double.compare(power, mage.power) == 0 && name.equals(mage.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, level, power);
    }

    @Override
    public String toString() {
        return "Mage{" +
                "name='" + name + '\'' +
                ", level=" + level +
                ", power=" + Math.round(power*100) +
                "%}";
    }

    public String toRecursiveTree(int level) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this);
        for (Mage apprentice : apprentices) {
            stringBuilder.append("\n").append("\t".repeat(level+1)).append(apprentice.toRecursiveTree(level+1));
        }
        return stringBuilder.toString();
    }

    @Override
    public int compareTo(Mage o) {
        //name, level, power
        if(this.name.compareTo(o.name) != 0) return this.name.compareTo(o.name);
        if(this.level != o.level) return Integer.compare(this.level, o.level);
        return Double.compare(this.power, o.power);
    }
}
