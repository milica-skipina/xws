package com.example.tim2.service;

import com.example.tim2.dto.CodebookDTO;
import com.example.tim2.datavalidation.RegularExpressions;
import com.example.tim2.model.Codebook;
import com.example.tim2.repository.CodebookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CodebookService {

    @Autowired
    private CodebookRepository codebookRepository;

    public boolean validateCodebook(CodebookDTO cb) {
        if (cb.getCode() == null || cb.getCode().equals("") || cb.getCodeType() == null  || cb.getCodeType().equals("")
                || cb.getName() == null || cb.getName().equals("")) {
            return false;
        }
        if (codebookRepository.findOneByNameAndCodeTypeAndDeleted(cb.getName(), cb.getCodeType(), false) != null) {
            return false;
        }
        RegularExpressions regularExpressions = new RegularExpressions();
        if(!regularExpressions.isValidCodeType(cb.getCodeType())){
            return false;
        }
        else {
            return true;
        }
    }

    public CodebookDTO addCodebook(CodebookDTO c) {
        Codebook cb = codebookRepository.findOneByNameAndCodeTypeAndDeleted(c.getName(), c.getCodeType(), false);
        if (cb == null) {
            cb = new Codebook();
        }
        if (validateCodebook(c)) {
            cb.setCode(c.getCode());
            cb.setCodeType(c.getCodeType());
            cb.setName(c.getName());
            cb.setDeleted(false);
            return new CodebookDTO(codebookRepository.save(cb));
        } else {
            return null;
        }
    }

    public Codebook findOneByNameAndCodeType(String name, String codeType) {
        return codebookRepository.findOneByNameAndCodeTypeAndDeleted(name, codeType, false);
    }

    public List<CodebookDTO> getAll() {
        List<Codebook> codebook = codebookRepository.findAllByDeleted(false);
        List<CodebookDTO> ret = new ArrayList<>();
        for (Codebook c : codebook) {
            ret.add(new CodebookDTO(c));
        }
        return ret;
    }

}
