package competition.samsung.gotrash.service;

import competition.samsung.gotrash.entity.Trash;
import competition.samsung.gotrash.repository.TrashRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TrashServiceImpl implements TrashService {

    private TrashRepository trashRepository;

    @Override
    public List<Trash> findAll() {
        return trashRepository.findAll();
    }

    @Override
    public Optional<Trash> findById(String id) {
        return trashRepository.findById(id);
    }

    @Override
    public Trash save(Trash trash) {
        return trashRepository.save(trash);
    }

    @Override
    public String delete(String id) {
        trashRepository.deleteById(id);
        return "Successfully delete item with id : " + id;
    }
}
