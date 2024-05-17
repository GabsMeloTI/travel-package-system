package br.com.fiap.tour.repository;

import br.com.fiap.tour.domain.Pacote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface PacoteRepository extends JpaRepository<Pacote, Long> {
    @Query("select p from Pacote p where p.destino.id = :id")
    Page<Pacote> BuscarTodosDestinos(@Param("id") Long id, Pageable page);

    @Query("from Pacote p where p.dataSaida between :inicio and :fim")
    Page<Pacote> buscarPorDatas(@Param("inicio")LocalDate inicio, @Param("fim") LocalDate fim, Pageable page);

    @Query("select sum(p.valor) from Pacote p where p.destino.nome = :nome")
    Integer valorTotal(@Param("nome") String nome);

}
