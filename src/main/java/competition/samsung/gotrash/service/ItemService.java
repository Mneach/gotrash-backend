package competition.samsung.gotrash.service;

import competition.samsung.gotrash.entity.Item;
import competition.samsung.gotrash.entity.Notification;
import competition.samsung.gotrash.entity.User;

import java.util.List;

public interface ItemService {
    List<Item> findAll();
    Item findById(String id);
    Item save(Item item);
    String delete(String id);
}
