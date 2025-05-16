package br.com.rasgamortalha.rm_backend.repository;

import java.util.List;
import java.util.UUID;

import br.com.rasgamortalha.rm_backend.dto.PublicProximosEventosResponseDTO;
import br.com.rasgamortalha.rm_backend.entity.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EventoRepository extends JpaRepository<Evento, UUID> {
    @Query("SELECT e FROM Evento e WHERE e.data >= CURRENT_DATE AND e.deletedAt IS NULL ORDER BY e.data ASC")
    List<Evento> findProximosEventos();

//    @Query("""
//        SELECT new br.com.rasgamortalha.rm_backend.dto.PublicProximosEventosResponseDTO(
//            e.id,
//            e.titulo,
//            TO_CHAR(e.data, 'YYYY-MM-DD'),
//            TO_CHAR(e.horarioInicio, 'HH24:MI'),
//            TO_CHAR(e.horarioFim, 'HH24:MI'),
//            e.localizacao,
//            e.gratuito,
//            e.preco,
//            e.qtdVagas,
//            COUNT(ue)
//        )
//        FROM Evento e
//        LEFT JOIN UsuarioEvento ue ON ue.evento.id = e.id
//        WHERE e.data >= CURRENT_DATE AND e.deletedAt IS NULL
//        GROUP BY e.id, e.titulo, e.data, e.horarioInicio, e.horarioFim, e.localizacao, e.gratuito, e.preco, e.qtdVagas
//        ORDER BY e.data ASC
//    """)
//    List<PublicProximosEventosResponseDTO> findProximosEventosPublicosComQtdPreenchidas();
}
