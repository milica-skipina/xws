package com.example.adservice.repository;

import com.example.adservice.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {

    Image save(Image image);

    boolean deleteImageById(Long id);

}
