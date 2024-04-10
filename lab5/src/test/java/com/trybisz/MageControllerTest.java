package com.trybisz;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
public class MageControllerTest {
    private MageRepository repository;
    private MageController controller;

    @BeforeEach
    public void setUp() {
        this.repository = mock(MageRepository.class);
        this.controller = new MageController(repository);
    }

    @Test
    public void removeExisting() {
        var response = controller.delete("Magister");
        verify(repository).delete("Magister");
        assertThat(response, is("done"));
    }

    @Test
    public void removeNonExisting() {
        doThrow(new IllegalArgumentException("Mage not found: NonExisting")).when(repository).delete("NonExisting");
        var response = controller.delete("NonExisting");
        assertThat(response, is("not found"));
    }

    @Test
    public void fetchNonExisting() {
        when(repository.find("NonExisting")).thenReturn(Optional.empty());
        var response = controller.find("NonExisting");
        assertThat(response, is("not found"));
    }

    @Test
    public void fetchExisting() {
        when(repository.find("Magister")).thenReturn(Optional.of(new Mage("Magister", 40)));
        var response = controller.find("Magister");
        assertThat(response, is("Mage{name='Magister', level=40}"));
    }

    @Test
    public void saveNonExisting() {
        var response = controller.save("Docent", "1");
        verify(repository).save(new Mage("Docent", 1));
        assertThat(response, is("done"));
    }

    @Test
    public void saveExisting() {
        doThrow(new IllegalArgumentException("Mage already exists: Magister")).when(repository).save(new Mage("Magister", 0));
        var response = controller.save("Magister", "0");
        assertThat(response, is("bad request"));
    }
}
