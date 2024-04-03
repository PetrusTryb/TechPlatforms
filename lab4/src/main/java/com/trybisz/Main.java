package com.trybisz;

import com.trybisz.entities.Mage;
import com.trybisz.entities.Tower;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.concurrent.ThreadLocalRandom;

public class Main {
    static final int MAGES_COUNT = 64;
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Logger.getLogger("org.hibernate").setLevel(Level.WARNING);
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("lab4_db");
        EntityManager em = emf.createEntityManager();
        createTestData(em);
        System.out.println("Pomyślnie utworzono testowe dane");
        int choice = -1;
        while(choice!=0){
            showMenu();
            choice = input.nextInt();
            switch(choice){
                case 1:
                    input.skip("\n");
                    System.out.println(">Nazwa wieży: ");
                    String name = input.nextLine();
                    System.out.println(">Wysokość wieży: ");
                    int height = input.nextInt();
                    addTower(em, name, height);
                    break;
                case 2:
                    input.skip("\n");
                    System.out.println(">Nazwa maga: ");
                    String mageName = input.nextLine();
                    System.out.println(">Poziom maga: ");
                    int mageLevel = input.nextInt();
                    addMage(em, mageName, mageLevel);
                    break;
                case 3:
                    input.skip("\n");
                    System.out.println(">Nazwa wieży do usunięcia: ");
                    String towerName = input.nextLine();
                    deleteTower(em, towerName);
                    break;
                case 4:
                    input.skip("\n");
                    System.out.println(">Nazwa maga do usunięcia: ");
                    String mageNameToDelete = input.nextLine();
                    deleteMage(em, mageNameToDelete);
                    break;
                case 5:
                    printData(em);
                    break;
                case 6:
                    input.skip("\n");
                    System.out.println(">Nazwa maga: ");
                    String mageNameToBind = input.nextLine();
                    System.out.println(">Nazwa wieży: ");
                    String towerNameToBind = input.nextLine();
                    bindMageToTower(em, mageNameToBind, towerNameToBind);
                    break;
                case 7:
                    input.skip("\n");
                    System.out.println(">Nazwa maga: ");
                    String mageNameToUnbind = input.nextLine();
                    unbindMageFromTower(em, mageNameToUnbind);
                    break;
                case 8:
                    printHomelessMages(em);
                    break;
            }
        }
        em.close();
        emf.close();
    }
    static void createTestData(EntityManager em){
        Tower oldETI = new Tower("Stare ETI", 8);
        Tower newETI = new Tower("Nowe ETI", 3);
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
        System.out.println("1. Dodaj wieżę");
        System.out.println("2. Dodaj maga");
        System.out.println("3. Usuń wieżę");
        System.out.println("4. Usuń maga");
        System.out.println("5. Wyświetl wszystko");
        System.out.println("6. Przypisz maga do wieży");
        System.out.println("7. Wypisz maga z aktualnie zamieszkiwanej wieży");
        System.out.println("8. Wyświetl magów bez przypisanej wieży");
        System.out.println("0. Wyjdź");
        System.out.println("--------------------------------------------------------");
        System.out.print(">Wybierz opcję: ");
    }

    static void printData(EntityManager em){
        System.out.println("Wieże:");
        em.createQuery("SELECT t FROM Tower t", Tower.class).getResultList().forEach(System.out::println);
        System.out.println("Magowie:");
        em.createQuery("SELECT m FROM Mage m", Mage.class).getResultList().forEach(System.out::println);
    }

    static void printHomelessMages(EntityManager em){
        System.out.println("Magowie bez przypisanej wieży:");
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
            System.out.println("Nie ma takiej wieży");
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
            System.out.println("Nie ma takiej wieży");
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