package competition.samsung.gotrash.service;

import competition.samsung.gotrash.entity.Item;

import java.util.List;
import java.util.Optional;

public interface ItemService {
    List<Item> findAll();
    Optional<Item> findById(String id);
    Item save(Item item);
    String delete(String id);
}
