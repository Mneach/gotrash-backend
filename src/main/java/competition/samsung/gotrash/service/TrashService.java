package competition.samsung.gotrash.service;

import competition.samsung.gotrash.entity.Trash;

import java.util.List;

public interface TrashService {
    List<Trash> findAll();
    Trash findById(String id);
    Trash save(Trash trash);
    String delete(String id);
}
