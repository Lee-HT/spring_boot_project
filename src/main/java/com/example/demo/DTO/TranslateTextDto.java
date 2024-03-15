package com.example.demo.DTO;

import com.example.demo.Config.language.LanguageProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TranslateTextDto {
    @NotEmpty
    @Size(min = 1,max = 2000)
    private String text;
    @NotBlank
    @Pattern(regexp = LanguageProperties.langRegex)
    private String targetLanguage;

}
