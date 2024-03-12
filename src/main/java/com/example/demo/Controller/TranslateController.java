package com.example.demo.Controller;

import com.example.demo.DTO.ContentsDto;
import com.example.demo.DTO.TranslateTextDto;
import com.example.demo.Service.TranslateService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("translate")
public class TranslateController {

    private final TranslateService translateService;

    public TranslateController(TranslateService translateService) {
        this.translateService = translateService;
    }

    @PostMapping()
    public ResponseEntity<ContentsDto> getTranslateText(@Valid @RequestBody TranslateTextDto dto, Errors errors)
            throws Exception {
        String text = dto.getText();
        if (errors.hasErrors()){
            return new ResponseEntity<>(ContentsDto.builder().contents(text).build(),HttpStatus.BAD_REQUEST);
        }
        HttpStatus status = HttpStatus.CREATED;
        ContentsDto response = translateService.getTranslationContents(text, dto.getTargetLanguage());

        return new ResponseEntity<>(response, status);
    }
}
