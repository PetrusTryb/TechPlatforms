package com.trybisz;

public class MageController {
    private final MageRepository repository;

    public MageController(MageRepository repository) {
        this.repository = repository;
    }

    public String find(String name) {
        var result = repository.find(name);
        if (result.isPresent()) {
            return result.get().toString();
        } else {
            return "not found";
        }
    }

    public String delete(String name) {
        try {
            repository.delete(name);
            return "done";
        } catch (IllegalArgumentException e) {
            return "not found";
        }
    }

    public String save(String name, String level) {
        try {
            repository.save(new Mage(name, Integer.parseInt(level)));
            return "done";
        } catch (IllegalArgumentException e) {
            return "bad request";
        }
    }
}
