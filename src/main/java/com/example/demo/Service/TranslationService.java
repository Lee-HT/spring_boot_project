package com.example.demo.Service;

import com.example.demo.DTO.ContentsDto;

public interface TranslationService {
    ContentsDto getTranslationContents(String language,String targetLanguage) throws Exception;

}
