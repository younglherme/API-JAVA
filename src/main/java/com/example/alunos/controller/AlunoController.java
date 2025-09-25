package com.example.alunos.controller;

import com.example.alunos.dto.AlunoResponse;
import com.example.alunos.service.AlunoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/alunos")
public class AlunoController {

    private final AlunoService alunoService;

    public AlunoController(AlunoService alunoService) {
        this.alunoService = alunoService;
    }

    @PostMapping
    public ResponseEntity<AlunoResponse> criar (@RequestBody AlunoResponse request){
        return  ResponseEntity.status(HttpStatus.CREATED).body(AlunoService.salvar(request));
    }
    @GetMapping
    public List<AlunoResponse> listarTodos(){
        return alunoService.listarTodos();
    }
}
