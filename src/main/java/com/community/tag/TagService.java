package com.community.tag;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    public Tag findOrAdd(String title) {
        Tag tag = tagRepository.findByTitle(title);
        if (tag == null) {
            tag = tagRepository.save(Tag.builder().title(title).build());
        }
        return tag;
    }

    public Tag getTag(String tag) {
        Tag byTitle = tagRepository.findByTitle(tag);
        if (tag == null) {
            throw new AccessDeniedException("유효하지 않는 페이지입니다.");
        }
        return byTitle;

    }
}
