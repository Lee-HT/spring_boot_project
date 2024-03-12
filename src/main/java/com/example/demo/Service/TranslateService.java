package com.example.demo.Service;

import com.example.demo.DTO.ContentsDto;

public interface TranslateService {
    ContentsDto getTranslationContents(String language,String targetLanguage) throws Exception;

}
