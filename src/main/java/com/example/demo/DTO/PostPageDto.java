package com.example.demo.DTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.Sort;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PostPageDto {

    // 내용
    private List<PostDto> contents;
    // 총 페이지 수
    private int totalPages;
    // 한 페이지의 출력 개수
    private int size;
    // 현재 페이지 출력 개수
    private int numberOfElements;
    // 정렬 상태
    private Sort sorted;

    public Map<String, Object> getAttr() {
        Map<String, Object> Attr = new HashMap<>();
        Attr.put("contents", this.contents);
        Attr.put("totalPages", this.totalPages);
        Attr.put("size", this.size);
        Attr.put("numberOfElements", this.numberOfElements);
        Attr.put("sorted", this.sorted);
        return Attr;
    }
}
