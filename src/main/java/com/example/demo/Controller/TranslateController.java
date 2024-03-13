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
    public ResponseEntity<?> getTranslateText(@Valid @RequestBody TranslateTextDto dto, Errors errors)
            throws Exception {
        if (errors.hasErrors()){
            return ResponseEntity.badRequest().body("내용이 형식과 맞지 않습니다");
        }
        ContentsDto response = translateService.getTranslationContents(dto.getText(), dto.getTargetLanguage());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
