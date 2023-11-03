package com.example.demo.DTO;

import java.time.LocalDateTime;
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
public class PostDto {

    private Long pid;
    private Long uid;
    private String username;
    private String title;
    private String contents;
    private String category;
    private LocalDateTime updatedAt;
}
