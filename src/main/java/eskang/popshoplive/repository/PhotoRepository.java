package eskang.popshoplive.repository;

import eskang.popshoplive.repository.model.Photo;

import java.util.List;

public interface PhotoRepository {
    List<Photo> getPhotos();
    Photo save(Photo photo);
}
