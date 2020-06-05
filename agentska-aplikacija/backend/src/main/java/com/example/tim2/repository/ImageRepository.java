package com.example.tim2.repository;

import com.example.tim2.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {

    Image save(Image image);

}
