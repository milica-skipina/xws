package com.example.adservice.dto;

import com.example.adservice.model.Coords;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CoordsDTO {

    double x;
    double y;

    public CoordsDTO(Coords cord){
        this.x = cord.getX();
        this.y = cord.getY();
    }

}
