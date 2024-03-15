package com.example.demo.Config.language;

import java.util.List;

public interface LanguageProperties {
    List<String> languages = List.of("ko", "en", "ja");

    String langRegex = "(ko)|(en)|(ja)";

}
