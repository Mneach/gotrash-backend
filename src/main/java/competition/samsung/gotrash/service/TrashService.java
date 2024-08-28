package competition.samsung.gotrash.service;

import competition.samsung.gotrash.entity.Trash;

import java.util.List;
import java.util.Optional;

public interface TrashService {
    List<Trash> findAll();
    Optional<Trash> findById(String id);
    Trash save(Trash trash);
    String delete(String id);
    Optional<Trash> findFirstByCategory(String category);
}
