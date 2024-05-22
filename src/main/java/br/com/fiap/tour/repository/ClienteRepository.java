package br.com.fiap.tour.repository;

import br.com.fiap.tour.domain.Cliente;
import br.com.fiap.tour.domain.Pacote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    @Query("select cliente from Reserva r where r.pacote.valor > :valorPacote")
    Page<Cliente> buscarPorValorPacote(@Param("valorPacote") Double valor, Pageable pageable);

    //Criar a pesquisa de cliente por data de nascimento
    @Query("select c from Cliente c where c.dataNascimento = :data")
    Page<Cliente> buscarPorDataNascimento(@Param("data") LocalDate dataNascimento, Pageable page);

    @Query("from Cliente c where lower(c.nome) like %:nome%")
    Page<Cliente> buscarPorNome(@Param("nome") String nome, Pageable page);

    @Query("from Cliente c where c.endereco.cidade.uf = :uf")
    Page<Cliente> buscarPorEstado(@Param("uf") String uf, Pageable page);

    @Query("from Cliente c where lower(c.nome) like lower(concat('%', :nome, '%')) and lower(c.endereco.cidade.nome) like lower(concat('%', :cidade, '%'))")
    Page<Cliente> buscarPorNomeCidade(@Param("nome") String nome, @Param("cidade") String cidade, Pageable page);

    @Query("from Cliente c where c.endereco.cidade.uf in :estados")
    List<Cliente> buscarPorEstados(@Param("estados") List<String> estados);

    @Query("select count(c) from Cliente c where c.endereco.cidade.uf = :estado")
    Long totalClientePorEstado(@Param("estado") String estado);
}
