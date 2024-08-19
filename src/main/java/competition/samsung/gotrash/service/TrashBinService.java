package competition.samsung.gotrash.service;

import competition.samsung.gotrash.entity.TrashBin;

import java.util.List;
import java.util.Optional;

public interface TrashBinService {
    List<TrashBin> findAll();
    Optional<TrashBin> findById(String id);
    TrashBin save(TrashBin trash);
    String delete(String id);
}
