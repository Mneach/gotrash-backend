package competition.samsung.gotrash.service.implement;

import competition.samsung.gotrash.entity.TrashBin;
import competition.samsung.gotrash.repository.TrashBinRepository;
import competition.samsung.gotrash.service.TrashBinService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TrashBinServiceImpl implements TrashBinService {

    private TrashBinRepository trashBinRepository;

    @Override
    public List<TrashBin> findAll() {
        return trashBinRepository.findAll();
    }

    @Override
    public Optional<TrashBin> findById(String id) {
        return trashBinRepository.findById(id);
    }

    @Override
    public TrashBin save(TrashBin trash) {
        return trashBinRepository.save(trash);
    }

    @Override
    public String delete(String id) {
        trashBinRepository.deleteById(id);
        return "Successfully delete trash with id : " + id;
    }
}
