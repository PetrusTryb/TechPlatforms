package com.trybisz;

import com.trybisz.entities.Mage;
import com.trybisz.entities.Tower;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    static final int MAGES_COUNT = 8;
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Logger.getLogger("org.hibernate").setLevel(Level.WARNING);
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("lab4_db");
        EntityManager em = emf.createEntityManager();
        createTestData(em);
        System.out.println("Pomyslnie utworzono testowe dane");
        int choice = -1;
        while(choice!=0){
            showMenu();
            try {
                choice = input.nextInt();
                switch (choice) {
                    case 1 -> {
                        input.skip("\n");
                        System.out.println(">Nazwa wiezy: ");
                        String name = input.nextLine();
                        System.out.println(">Wysokosc wiezy: ");
                        int height = input.nextInt();
                        addTower(em, name, height);
                    }
                    case 2 -> {
                        input.skip("\n");
                        System.out.println(">Nazwa maga: ");
                        String mageName = input.nextLine();
                        System.out.println(">Poziom maga: ");
                        int mageLevel = input.nextInt();
                        addMage(em, mageName, mageLevel);
                    }
                    case 3 -> {
                        input.skip("\n");
                        System.out.println(">Nazwa wiezy do usuniecia: ");
                        String towerName = input.nextLine();
                        deleteTower(em, towerName);
                    }
                    case 4 -> {
                        input.skip("\n");
                        System.out.println(">Nazwa maga do usuniecia: ");
                        String mageNameToDelete = input.nextLine();
                        deleteMage(em, mageNameToDelete);
                    }
                    case 5 -> printData(em);
                    case 6 -> {
                        input.skip("\n");
                        System.out.println(">Nazwa maga: ");
                        String mageNameToBind = input.nextLine();
                        System.out.println(">Nazwa wiezy: ");
                        String towerNameToBind = input.nextLine();
                        bindMageToTower(em, mageNameToBind, towerNameToBind);
                    }
                    case 7 -> {
                        input.skip("\n");
                        System.out.println(">Nazwa maga: ");
                        String mageNameToUnbind = input.nextLine();
                        unbindMageFromTower(em, mageNameToUnbind);
                    }
                    case 8 -> printHomelessMages(em);
                }
            }catch (InputMismatchException e){
                System.out.println("Nieprawidlowe dane wejsciowe");
                input.nextLine();
            }
        }
        em.close();
        emf.close();
    }
    static void createTestData(EntityManager em){
        Tower oldETI = new Tower("Wieza A", 8);
        Tower newETI = new Tower("Wieza B", 3);
        EntityTransaction et = em.getTransaction();
        et.begin();
        em.persist(oldETI);
        em.persist(newETI);
        for (int i = 1; i <= MAGES_COUNT; i++) {
            Mage mage = new Mage("Magik #" + i, ThreadLocalRandom.current().nextInt(1, 30));
            if(i % 2 == 0){
                oldETI.bindMage(mage);
                mage.setTower(oldETI);
            } else {
                newETI.bindMage(mage);
                mage.setTower(newETI);
            }
            em.persist(mage);
        }
        et.commit();
    }

    static void showMenu(){
        System.out.println("------------------------------------------------------");
        System.out.println("1. Dodaj wieze");
        System.out.println("2. Dodaj maga");
        System.out.println("3. Usun wieze");
        System.out.println("4. Usun maga");
        System.out.println("5. Wyswietl wszystko");
        System.out.println("6. Przypisz maga do wiezy");
        System.out.println("7. Wypisz maga z aktualnie zamieszkiwanej wiezy");
        System.out.println("8. Wyswietl magow bez przypisanej wiezy");
        System.out.println("0. Wyjdz");
        System.out.println("--------------------------------------------------------");
        System.out.print(">Wybierz opcje: ");
    }

    static void printData(EntityManager em){
        System.out.println("Wieze:");
        em.createQuery("SELECT t FROM Tower t", Tower.class).getResultList().forEach(System.out::println);
        System.out.println("Magowie:");
        em.createQuery("SELECT m FROM Mage m", Mage.class).getResultList().forEach(System.out::println);
    }

    static void printHomelessMages(EntityManager em){
        System.out.println("Magowie bez przypisanej wiezy:");
        em.createQuery("SELECT m FROM Mage m WHERE m.tower IS NULL", Mage.class).getResultList().forEach(System.out::println);
    }

    static void addTower(EntityManager em, String name, int height){
        Tower tower = new Tower(name, height);
        EntityTransaction et = em.getTransaction();
        et.begin();
        em.persist(tower);
        et.commit();
    }

    static void addMage(EntityManager em, String name, int level){
        Mage mage = new Mage(name, level);
        EntityTransaction et = em.getTransaction();
        et.begin();
        em.persist(mage);
        et.commit();
    }

    static void deleteMage(EntityManager em, String name){
        Mage mage = em.find(Mage.class, name);
        if(mage == null){
            System.out.println("Nie ma takiego maga");
            return;
        }
        var mageTower = mage.getTower();
        if(mageTower != null){
            unbindMageFromTower(em, name);
        }
        EntityTransaction et = em.getTransaction();
        et.begin();
        em.remove(mage);
        et.commit();
    }

    static void deleteTower(EntityManager em, String name){
        Tower tower = em.find(Tower.class, name);
        if(tower == null){
            System.out.println("Nie ma takiej wiezy");
            return;
        }
        EntityTransaction et = em.getTransaction();
        et.begin();
        var mages = tower.getMages();
        for (var mage : mages) {
            mage.setTower(null);
        }
        em.remove(tower);
        et.commit();
    }
    static void unbindMageFromTower(EntityManager em, String mageName){
        Mage mage = em.find(Mage.class, mageName);
        if(mage == null){
            System.out.println("Nie ma takiego maga");
            return;
        }
        EntityTransaction et = em.getTransaction();
        et.begin();
        var tower = mage.getTower();
        if(tower != null){
            tower.unbindMage(mage);
            mage.setTower(null);
        }
        et.commit();
    }

    static void bindMageToTower(EntityManager em, String mageName, String towerName){
        Mage mage = em.find(Mage.class, mageName);
        Tower tower = em.find(Tower.class, towerName);
        if(mage == null){
            System.out.println("Nie ma takiego maga");
            return;
        }
        if(tower == null){
            System.out.println("Nie ma takiej wiezy");
            return;
        }
        var previousTower = mage.getTower();
        if(tower == previousTower){
            return;
        }
        if(previousTower != null){
            unbindMageFromTower(em, mageName);
        }
        EntityTransaction et = em.getTransaction();
        et.begin();
        mage.setTower(tower);
        tower.bindMage(mage);
        et.commit();
    }
}