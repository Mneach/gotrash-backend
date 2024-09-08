package competition.samsung.gotrash.service.implement;

import competition.samsung.gotrash.entity.Trash;
import competition.samsung.gotrash.repository.TrashRepository;
import competition.samsung.gotrash.service.TrashService;
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
    public Optional<Trash> findById(Integer id) {
        return trashRepository.findById(id);
    }

    @Override
    public Trash save(Trash trash) {
        return trashRepository.save(trash);
    }

    @Override
    public String delete(Integer id) {
        trashRepository.deleteById(id);
        return "Successfully delete item with id : " + id;
    }

    @Override
    public Optional<Trash> findFirstByCategory(String category) {
        return trashRepository.findFirstByCategory(category);
    }

    public Optional<Trash> findLastTrashById() {
        return trashRepository.findLastTrashById();
    }
}
