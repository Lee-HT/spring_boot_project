package com.example.demo.Controller;

import com.example.demo.DTO.ContentsDto;
import com.example.demo.DTO.TranslateTextDto;
import com.example.demo.Service.TranslateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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
    public ResponseEntity<ContentsDto> getTranslateText(@RequestBody TranslateTextDto dto)
            throws Exception {
        String text = dto.getText();
        if (text.isEmpty()) {
            return new ResponseEntity<>(ContentsDto.builder().build(),HttpStatus.NO_CONTENT);
        }
        HttpStatus status = HttpStatus.CREATED;
        ContentsDto response = translateService.getTranslationContents(text, dto.getTargetLanguage());
        if (response.getContents().isEmpty()){
            status = HttpStatus.BAD_REQUEST;
        }

        return new ResponseEntity<>(response, status);
    }
}
