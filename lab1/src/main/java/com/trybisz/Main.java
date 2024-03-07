package com.trybisz;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class Main {
    public static void main(String[] args) {
        SortOrder sortOrder = args.length > 0 ? SortOrder.valueOf(args[0]) : SortOrder.SORT_NONE;
        Set<Mage> mageRepository;
        if(sortOrder == SortOrder.SORT_NONE){
            mageRepository = new HashSet<>();
        }
        else if(sortOrder == SortOrder.SORT_PRIMARY) {
            mageRepository = new TreeSet<>();
        }
        else{
            mageRepository = new TreeSet<>(new MageComparator());
        }
        boolean hasThirdLevelApprentice = false;
        for (int i = 0; i < 10; i++) {
            Mage mage = new Mage("Magik " + i, (int) (Math.random()*10), Math.random(), sortOrder);
            for (int j = 0; j < (int) (Math.random()*3); j++) {
                Mage apprentice = new Mage("Syn Magika " + j, (int) (Math.random()*10), Math.random(), sortOrder);
                mage.addApprentice(apprentice);
                if(!hasThirdLevelApprentice){
                    apprentice.addApprentice(new Mage("Wnuk Magika " + j, (int) (Math.random()*10), Math.random(), sortOrder));
                    hasThirdLevelApprentice = true;
                }
            }
            mageRepository.add(mage);
        }
        for (Mage mage : mageRepository) {
            System.out.println(mage.toRecursiveTree(0));
        }
    }
}