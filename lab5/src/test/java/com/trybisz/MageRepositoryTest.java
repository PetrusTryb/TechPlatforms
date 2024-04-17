package com.trybisz;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.github.npathai.hamcrestopt.OptionalMatchers.isEmpty;
import static com.github.npathai.hamcrestopt.OptionalMatchers.isPresent;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MageRepositoryTest {
    private MageRepository repository;

    @BeforeEach
    public void setUp() {
        this.repository = new MageRepository(List.of(
            new Mage("Student", 0),
            new Mage("Magister", 40),
            new Mage("Profesor", 66)
        ));
    }

    @Test
    public void removeExisting() {
        repository.delete("Magister");
        assertThat(repository.find("Magister"), isEmpty());
    }

    @Test
    public void removeNonExisting() {
        assertThrows(IllegalArgumentException.class, () -> repository.delete("NonExisting"));
    }

    @Test
    public void fetchNonExisting() {
        assertThat(repository.find("NonExisting"), isEmpty());
    }

    @Test
    public void fetchExisting() {
        assertThat(repository.find("Magister"), isPresent());
    }

    @Test
    public void saveExisting() {
        assertThrows(IllegalArgumentException.class, () -> repository.save(new Mage("Magister", 20)));
    }

    @Test
    public void saveNew() {
        repository.save(new Mage("Docent", 1));
        assertThat(repository.find("Docent"), isPresent());
    }
}
