package com.trybisz;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
public class MageRepository {
    private final Collection<Mage> collection;

    public MageRepository() {
        this.collection = new ArrayList<>();
    }

    public MageRepository(Collection<Mage> collection) {
        this.collection = new ArrayList<>(collection);
    }
    public Optional<Mage> find(String name) {
        return this.collection.stream()
            .filter(mage -> mage.getName().equals(name))
            .findFirst().or(Optional::empty);
    }
    public void delete(String name) {
        boolean success = this.collection.removeIf(mage -> mage.getName().equals(name));
        if (!success) {
            throw new IllegalArgumentException("Mage not found: " + name);
        }
    }
    public void save(Mage mage) {
        if(this.collection.stream().anyMatch(m -> m.getName().equals(mage.getName()))) {
            throw new IllegalArgumentException("Mage already exists: " + mage.getName());
        }
        this.collection.add(mage);
    }
}
