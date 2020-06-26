package com.example.adservice.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Codebook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "code_type", nullable = false)
    private String codeType;

    @Column(name = "deleted", nullable = false)
    private Boolean deleted;

    @JsonManagedReference(value = "car_make_mov")
    @OneToMany(mappedBy = "make", fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private Set<Car> carMake;

    @JsonManagedReference(value = "car_model_mov")
    @OneToMany(mappedBy = "model", fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private Set<Car> carModel;

    @JsonManagedReference(value = "car_fuel_mov")
    @OneToMany(mappedBy = "fuel", fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private Set<Car> carFuel;

    @JsonManagedReference(value = "car_gearbox_mov")
    @OneToMany(mappedBy = "gearbox", fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private Set<Car> carGearbox;

    @JsonManagedReference(value = "car_class_mov")
    @OneToMany(mappedBy = "carClass", fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private Set<Car> carClass;

    public Codebook() {
    }

    public Codebook(String code, String name, String codeType) {
        this.code = code;
        this.name = name;
        this.codeType = codeType;
        this.deleted = false;
    }

    public Codebook(rs.ac.uns.ftn.xws_tim2.Codebook c){
        this.code = c.getCode();
        this.name = c.getName();
        this.codeType = c.getCodeType();
        this.deleted = c.isDeleted();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCodeType() {
        return codeType;
    }

    public void setCodeType(String codeType) {
        this.codeType = codeType;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}