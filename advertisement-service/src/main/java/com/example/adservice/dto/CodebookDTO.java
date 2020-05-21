package com.example.adservice.dto;

import com.example.adservice.model.Codebook;

public class CodebookDTO {

    private Long id;
    private String code;
    private String name;
    private String codeType;

    public CodebookDTO() {
    }

    public CodebookDTO(Long id, String code, String name, String codeType) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.codeType = codeType;
    }

    public CodebookDTO(Codebook c) {
        this(c.getId(),c.getCode(),c.getName(),c.getCodeType());
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
}
