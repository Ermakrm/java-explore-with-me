package ru.practicum.compilation.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.mapper.CompilationListMapper;
import ru.practicum.compilation.mapper.CompilationMapper;
import ru.practicum.compilation.service.CompilationService;

import java.util.List;

@RestController
@RequestMapping(path = "/compilations")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PublicCompilationController {
    CompilationService compilationService;
    CompilationMapper compilationMapper;
    CompilationListMapper compilationListMapper;

    @GetMapping
    public List<CompilationDto> getCompilations(
            @RequestParam(required = false) Boolean pinned,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size
    ) {
        log.info("getCompilations pinned={};from={};size={}", pinned, from, size);
        return compilationListMapper.toCompilationListDto(
                compilationService.getAllCompilations(pinned, from, size)
        );
    }

    @GetMapping("/{compId}")
    public CompilationDto getCompilationById(@PathVariable Long compId) {
        log.info("getCompilationById compId={}", compId);
        return compilationMapper.toCompilationDto(compilationService.findCompilationById(compId));
    }
}
