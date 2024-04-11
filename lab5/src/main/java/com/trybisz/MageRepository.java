package com.trybisz;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
public class MageRepository {
    private final Collection<Mage> collection;

    public MageRepository(Collection<Mage> collection) {
        this.collection = new ArrayList<>(collection);
    }
    public Optional<Mage> find(String name) {
        return this.collection.stream()
            .filter(mage -> mage.name().equals(name))
            .findFirst().or(Optional::empty);
    }
    public void delete(String name) {
        boolean success = this.collection.removeIf(mage -> mage.name().equals(name));
        if (!success) {
            throw new IllegalArgumentException("Mage not found: " + name);
        }
    }
    public void save(Mage mage) {
        if(this.collection.stream().anyMatch(m -> m.name().equals(mage.name()))) {
            throw new IllegalArgumentException("Mage already exists: " + mage.name());
        }
        this.collection.add(mage);
    }
}
