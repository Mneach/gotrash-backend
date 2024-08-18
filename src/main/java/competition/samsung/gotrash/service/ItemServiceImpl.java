package competition.samsung.gotrash.service;

import competition.samsung.gotrash.entity.Item;
import competition.samsung.gotrash.repository.ItemRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService{

    private ItemRepository itemRepository;

    @Override
    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    @Override
    public Item findById(String id) {
        return itemRepository.findById(id).get();
    }

    @Override
    public Item save(Item item) {
        return itemRepository.save(item);
    }

    @Override
    public String delete(String id) {
        itemRepository.deleteById(id);
        return "Successfully delete item with id : " + id;
    }
}
