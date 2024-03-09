package com.trybisz;

import java.util.*;

public class Main {
    private static Map<Mage, Integer> getDescendantsStats(Set<Mage> mageRepository, SortOrder sortOrder){
        Map<Mage,Integer> descCount;
        if(sortOrder == SortOrder.SORT_NONE){
            descCount = new HashMap<>();
        }
        else if(sortOrder == SortOrder.SORT_PRIMARY) {
            descCount = new TreeMap<>();
        }
        else{
            descCount = new TreeMap<>(new MageComparator());
        }
        for (Mage mage: mageRepository){
            descCount.put(mage,mage.countDescendants());
            Set<Mage> mDesc = mage.getApprentices();
            Map<Mage,Integer> dStats = getDescendantsStats(mDesc, sortOrder);
            descCount.putAll(dStats);
        }
        return descCount;
    }
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
                Mage apprentice = new Mage("Syn Magika", (int) (Math.random()*10), Math.random(), sortOrder);
                mage.addApprentice(apprentice);
                if(!hasThirdLevelApprentice){
                    apprentice.addApprentice(new Mage("Wnuk Magika", (int) (Math.random()*10), Math.random(), sortOrder));
                    hasThirdLevelApprentice = true;
                }
            }
            mageRepository.add(mage);
        }
        for (Mage mage : mageRepository) {
            System.out.println(mage.toRecursiveTree(0));
        }
        System.out.println("\nDescendants stats");
        Map<Mage,Integer> descStats = getDescendantsStats(mageRepository, sortOrder);
        for (Map.Entry<Mage,Integer> m: descStats.entrySet()){
            System.out.println(m.getKey()+": "+m.getValue());
        }
    }
}