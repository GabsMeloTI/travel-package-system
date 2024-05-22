package br.com.fiap.tour.controller;

import br.com.fiap.tour.domain.Destino;
import br.com.fiap.tour.domain.Pacote;
import br.com.fiap.tour.dto.pacote.AtualizacaoPacoteDTO;
import br.com.fiap.tour.dto.pacote.CadastroPacoteDTO;
import br.com.fiap.tour.dto.pacote.DetalhesPacoteDTO;
import br.com.fiap.tour.dto.pacote.RetornoValorPorDestino;
import br.com.fiap.tour.repository.DestinoRepository;
import br.com.fiap.tour.repository.PacoteRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;

@RestController
@RequestMapping("/pacotes")
public class PacoteController {

    @Autowired
    private PacoteRepository pacoteRepository;

    @Autowired
    private DestinoRepository destinoRepository;




    @GetMapping("total-pacotes")
    public ResponseEntity<RetornoValorPorDestino> totalValor(@RequestParam("nome") String nome) {
        var quantidade  = pacoteRepository.valorTotal(nome);
        return ResponseEntity.ok(new RetornoValorPorDestino(nome, quantidade));
    }

    @GetMapping("por-data")
    public ResponseEntity<Page<DetalhesPacoteDTO>> buscarPorData(@RequestParam("inicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
                                                                 @RequestParam("fim") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim,
                                                                 Pageable page) {
        var lista = pacoteRepository.buscarPorDatas(inicio, fim, page).map(DetalhesPacoteDTO::new);
        return ResponseEntity.ok(lista);
    }

    @GetMapping("todos-destinos")
    public ResponseEntity<Page<DetalhesPacoteDTO>> buscarDestino(@RequestParam("id") Long id, Pageable page) {
        var lista = pacoteRepository.BuscarTodosDestinos(id, page).map(DetalhesPacoteDTO::new);
        return ResponseEntity.ok(lista);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<DetalhesPacoteDTO> cadastrar(@RequestBody @Valid CadastroPacoteDTO dto, UriComponentsBuilder builder) {
        var pacote = new Pacote(dto);
        var destino = destinoRepository.getReferenceById(dto.destinoId());
        pacote.setDestino(destino);
        pacote = pacoteRepository.save(pacote);
        var uri = builder.path("/pacotes/{id}").buildAndExpand(pacote.getId()).toUri();
        return ResponseEntity.created(uri).body(new DetalhesPacoteDTO(pacote));
    }

    @GetMapping
    public ResponseEntity<Page<DetalhesPacoteDTO>> pesquisar(Pageable pageable){
        var page = pacoteRepository.findAll(pageable).map(DetalhesPacoteDTO::new);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetalhesPacoteDTO> pesquisar(@PathVariable("id") Long id){
        var pacote = new DetalhesPacoteDTO(pacoteRepository.getReferenceById(id));
        return ResponseEntity.ok(pacote);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<DetalhesPacoteDTO> atualizar(@PathVariable("id") Long id, @RequestBody @Valid AtualizacaoPacoteDTO dto){
        var pacote = pacoteRepository.getReferenceById(id);
        pacote.atualizar(dto);
        return ResponseEntity.ok(new DetalhesPacoteDTO(pacote));
    }

}
