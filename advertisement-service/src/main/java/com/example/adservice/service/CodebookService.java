package com.example.adservice.service;

import com.example.adservice.datavalidation.RegularExpressions;
import com.example.adservice.dto.CodebookDTO;
import com.example.adservice.model.Advertisement;
import com.example.adservice.model.Codebook;
import com.example.adservice.repository.AdvertisementRepository;
import com.example.adservice.repository.CodebookRepository;
import org.owasp.encoder.Encode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CodebookService {

    @Autowired
    private CodebookRepository codebookRepository;

    @Autowired
    private AdvertisementRepository advertisementRepository;

    public boolean validateCodebook(CodebookDTO cb) {
        if (cb.getCode() == null || cb.getCode().equals("") || cb.getCodeType() == null  || cb.getCodeType().equals("")
                || cb.getName() == null || cb.getName().equals("")) {
            return false;
        }
        if (codebookRepository.findOneByNameAndCodeTypeAndDeletedAndCode(cb.getName(), cb.getCodeType(), false, cb.getCode()) != null) {
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


    public CodebookDTO forHtmlCd(CodebookDTO cb){
        cb.setCodeType(Encode.forHtml(cb.getCodeType()));
        cb.setCode(Encode.forHtml(cb.getCode()));
        cb.setName(Encode.forHtml(cb.getName()));
        return  cb;
    }

    public CodebookDTO editCodebook(CodebookDTO cb, Long id){
        List<Advertisement>ads = advertisementRepository.findAll();
        boolean ok = true;
        for(Advertisement a : ads){
            if(a.getCarAd().getCarClass().getCode() == cb.getCode() || a.getCarAd().getGearbox().getCode() == cb.getCode() || a.getCarAd().getFuel().getCode() == cb.getCode() || a.getCarAd().getMake().getCode() == cb.getCode() || a.getCarAd().getModel().getCode() == cb.getCode()){
                ok = false;
            }
        }
        if(validateCodebook(cb) && ok){
            Codebook current = codebookRepository.findOneById(id);
            current.setCode(cb.getCode());
            current.setName(cb.getName());
            current.setCodeType(cb.getCodeType());
            current.setDeleted(false);
            return new CodebookDTO(codebookRepository.save(current));
        }
        return null;
    }

    public boolean deleteCodebook(Long id){
        List<Advertisement>ads = advertisementRepository.findAll();
        Codebook cb = codebookRepository.findOneById(id);
        boolean ok = true;
        for(Advertisement a : ads){
            if(a.getCarAd().getCarClass().getCode() == cb.getCode() || a.getCarAd().getGearbox().getCode() == cb.getCode() || a.getCarAd().getFuel().getCode() == cb.getCode() || a.getCarAd().getMake().getCode() == cb.getCode() || a.getCarAd().getModel().getCode() == cb.getCode()){
                ok = false;
            }
        }
        if(ok){
            cb.setDeleted(true);
            codebookRepository.save(cb);
            return true;
        }
        return false;
    }

    public CodebookDTO addCodebook(CodebookDTO c) {
        Codebook cb = codebookRepository.findOneByNameAndCodeTypeAndDeleted(c.getName(), c.getCodeType(), false);
        if (cb == null) {
            cb = new Codebook();
        }
        if (validateCodebook(c)) {
            CodebookDTO temp = forHtmlCd(c);
            cb.setName(temp.getName());
            cb.setCodeType(temp.getCodeType());
            cb.setCode(temp.getCode());
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
            if(!c.getDeleted()) {
                ret.add(new CodebookDTO(c));
            }
        }
        return ret;
    }

}
