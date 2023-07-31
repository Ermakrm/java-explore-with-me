package ru.practicum.compilation.service;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.compilation.dto.UpdateCompilationRequest;
import ru.practicum.compilation.mapper.CompilationMapper;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.repository.CompilationRepository;
import ru.practicum.exception.ObjectNotFoundException;

import java.util.List;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CompilationServiceImpl implements CompilationService {

    CompilationRepository compilationRepository;
    CompilationMapper compilationMapper;

    @Override
    public Compilation findCompilationById(Long compId) {
        return compilationRepository.findById(compId).orElseThrow(
                () -> new ObjectNotFoundException(String.format("Compilation with id=%d not found", compId)));
    }

    @Override
    public Compilation saveCompilation(Compilation compilation) {
        return compilationRepository.save(compilation);
    }

    @Override
    public Compilation updateCompilation(UpdateCompilationRequest updateCompilationRequest, Long compId) {
        Compilation compilation = findCompilationById(compId);

        compilationMapper.updateCompilation(updateCompilationRequest, compilation);

        return compilationRepository.save(compilation);
    }

    @Override
    public void deleteCompilation(Long compId) {
        if (!compilationRepository.existsById(compId)) {
            throw new ObjectNotFoundException(String.format("Compilation with id=%d not found", compId));
        }
        compilationRepository.deleteById(compId);
    }

    @Override
    public List<Compilation> getAllCompilations(Boolean pinned, int from, int size) {
        PageRequest pr = PageRequest.of(from / size, size);
        if (pinned != null) {
            return compilationRepository.findAllByPinned(pinned, pr);
        } else {
            return compilationRepository.findAll(pr).getContent();
        }
    }
}
