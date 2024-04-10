package com.trybisz.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table
public class Mage {
    @Id
    private String name;
    private int level;
    @ManyToOne
    private Tower tower;

    public Mage(String name, int level) {
        this.name = name;
        this.level = level;
        this.tower = null;
    }

    public Mage() {

    }
    public void setTower(Tower tower) {
        this.tower = tower;
    }

    public Tower getTower() {
        return tower;
    }

    @Override
    public String toString() {
        return "Mage{" +
                "name='" + name + '\'' +
                ", level=" + level +
                ", tower.name='" + (tower!=null?tower.getName():"NULL") +
                "'}";
    }
}
