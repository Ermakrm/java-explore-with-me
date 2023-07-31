package ru.practicum.compilation.service;

import ru.practicum.compilation.dto.UpdateCompilationRequest;
import ru.practicum.compilation.model.Compilation;

import java.util.List;

public interface CompilationService {
    Compilation findCompilationById(Long compId);

    Compilation saveCompilation(Compilation compilation);

    Compilation updateCompilation(UpdateCompilationRequest updateCompilationRequest, Long compId);

    void deleteCompilation(Long compId);

    List<Compilation> getAllCompilations(Boolean pinned, int from, int size);
}
