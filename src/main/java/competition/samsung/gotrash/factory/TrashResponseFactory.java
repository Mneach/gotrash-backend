package competition.samsung.gotrash.factory;

import competition.samsung.gotrash.entity.Trash;
import competition.samsung.gotrash.response.TrashResponse;

public class TrashResponseFactory {

    public static TrashResponse createUserResponse(Trash trash) {
        return TrashResponse.builder()
                .trashId(trash.getId())
                .build();
    }

}
