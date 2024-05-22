package br.com.fiap.tour.repository;

import br.com.fiap.tour.domain.Destino;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DestinoRepository extends JpaRepository<Destino, Long> {
    Page<Destino> findByNomeContainingIgnoreCase(String parteDoNome, Pageable pageable);
}
