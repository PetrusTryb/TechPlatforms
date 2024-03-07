package com.trybisz;
import java.util.Comparator;

public class MageComparator implements Comparator<Mage> {

    @Override
    public int compare(Mage o1, Mage o2) {

        if(o1.getLevel() != o2.getLevel()) return Integer.compare(o1.getLevel(), o2.getLevel());
        if(o1.getName().compareTo(o2.getName()) != 0) return o1.getName().compareTo(o2.getName());

        return Double.compare(o1.getPower(), o2.getPower());
    }

}
//level, name, power