package com.example.adservice.service;

import com.example.adservice.repository.CodebookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CodebookService {

    @Autowired
    private CodebookRepository codebookRepository;

}
