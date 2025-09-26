package com.example.alunos.service;

import com.example.alunos.entity.Matricula;
import com.example.alunos.dto.AlunoRequest;
import com.example.alunos.dto.AlunoResponse;
import com.example.alunos.dto.MatriculaDTO;
import com.example.alunos.entity.Aluno;
import com.example.alunos.repository.AlunoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AlunoService {

    private final AlunoRepository alunoRepository;

    public AlunoService(AlunoRepository alunoRepository) {
        this.alunoRepository = alunoRepository;
    }

    public AlunoResponse salvar(AlunoRequest request) {
        // Converter AlunoRequest para entidade Aluno
        Aluno aluno = new Aluno();
        aluno.setNome(request.nome());
        aluno.setTelefone(request.telefone());
        aluno.setDataNascimento(request.dataNascimento());

        // Se precisar salvar as matrículas também
        // (depende da sua implementação de Matricula)

        Aluno alunoSalvo = alunoRepository.save(aluno);

        // Converter entidade Aluno para AlunoResponse
        return toAlunoResponse(alunoSalvo);
    }

    public List<AlunoResponse> listarTodos() {
        return alunoRepository.findAll()
                .stream()
                .map(this::toAlunoResponse)
                .collect(Collectors.toList());
    }

    public List<MatriculaDTO> listarMatriculas(Long id) {
        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));


        return aluno.getMatriculas()
                .stream()
                .map(Matricula -> new MatriculaDTO(
                        Matricula.getCodigoMatricula(),
                        Matricula.getNomeCurso(),
                        Matricula.getDataInicio()
                        // outros campos necessários
                ))
                .collect(Collectors.toList());
    }

    public AlunoResponse atualizar(Long id, AlunoRequest request) {
        Aluno alunoExistente = alunoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));

        // Atualizar os campos
        alunoExistente.setNome(request.nome());
        alunoExistente.setTelefone(request.telefone());
        alunoExistente.setDataNascimento(request.dataNascimento());

        Aluno alunoAtualizado = alunoRepository.save(alunoExistente);

        return toAlunoResponse(alunoAtualizado);
    }

    public void remover(Long id) {
        if (!alunoRepository.existsById(id)) {
            throw new RuntimeException("Aluno não encontrado");
        }
        alunoRepository.deleteById(id);
    }

    // Método auxiliar para converter Aluno para AlunoResponse
    private AlunoResponse toAlunoResponse(Aluno aluno) {
        return new AlunoResponse(
                aluno.getId(),
                aluno.getNome(),
                aluno.getTelefone(),
                aluno.getDataNascimento(),
                // Converter matrículas se necessário
                aluno.getMatriculas() != null ?
                        aluno.getMatriculas()
                                .stream()
                                .map(Matricula -> new MatriculaDTO(
                                        Matricula.getCodigoMatricula(),
                                        Matricula.getNomeCurso(),
                                        Matricula.getDataInicio()
                                        // outros campos
                                ))
                                .collect(Collectors.toList())
                        : List.of()
        );
    }
}