package com.racoder.myjpa.controller;

import java.util.List;

import com.racoder.myjpa.model.Board;
import com.racoder.myjpa.repository.BoardRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
class BoardApiController {

    private BoardRepository repository;

    // Aggregate root
    @GetMapping("/boards")
    List<Board> all(@RequestParam(required = false, defaultValue = "") String title, @RequestParam(required = false, defaultValue = "") String content) {

        if (StringUtils.isEmpty(title) && StringUtils.isEmpty((content))) {
            return repository.findAll();
        } else {
            return repository.findByTitleOrContent(title, content);
        }

    }
    // end::get-aggregate-root[]

    @PostMapping("/boards")
    Board newBoard(@RequestBody Board newBoard) {
        return repository.save(newBoard);
    }

    // Single item

    @GetMapping("/boards/{id}")
    Board one(@PathVariable Long id) {
        return repository.findById(id).orElse(null);
    }

    @PutMapping("/boards/{id}")
    Board replaceBoard(@RequestBody Board newBoard, @PathVariable Long id) {

        return repository.findById(id)
                .map(Board -> {
                    Board.setTitle(newBoard.getTitle());
                    Board.setContent(newBoard.getContent());
                    return repository.save(Board);
                })
                .orElseGet(() -> {
                    newBoard.setId(id);
                    return repository.save(newBoard);
                });
    }

    @DeleteMapping("/boards/{id}")
    void deleteBoard(@PathVariable Long id) {
        repository.deleteById(id);
    }
}