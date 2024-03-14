package com.example.demo.Config.language;

import java.util.List;

public interface LanguageProperties {
    public List<String> languages = List.of("ko", "en", "ja");

    public default String languageRegex() {
        StringBuilder stringBuilder = new StringBuilder();

        for (String lang:languages) {
            stringBuilder.append("(" + lang + ")");
        }

        return stringBuilder.toString();
    }

}
