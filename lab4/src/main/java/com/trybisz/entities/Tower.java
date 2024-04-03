package com.trybisz.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class Tower {
    @Id
    private String name;
    private int height;
    @OneToMany
    private List<Mage> mages;

    public Tower(String name, int height) {
        this.name = name;
        this.height = height;
        this.mages = new ArrayList<>();
    }

    public Tower() {

    }

    public String getName() {
        return name;
    }

    public int getHeight() {
        return height;
    }

    public void bindMage(Mage mage) {
        if (mages.contains(mage))
            return;
        mages.add(mage);
    }

    public void unbindMage(Mage mage) {
        mages.remove(mage);
    }

    public List<Mage> getMages() {
        return List.copyOf(mages);
    }

    @Override
    public String toString() {
        return "Tower{" +
                "name='" + name + '\'' +
                ", height=" + height +
                ", mages=" + mages +
                '}';
    }
}
