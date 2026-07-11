package com.pokedex.core.service.impl;

import com.pokedex.core.model.AdminMetrics;
import com.pokedex.core.model.PokemonView;
import com.pokedex.persistence.entity.document.PokemonViewDocument;
import com.pokedex.persistence.repository.document.PokemonViewMongoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("StatsServiceImpl")
class StatsServiceImplTest {

    @Mock private PokemonViewMongoRepository viewRepository;

    @InjectMocks private StatsServiceImpl service;

    private static final Long PIKACHU_ID = 25L;
    private static final String PIKACHU_NAME = "Pikachu";

    @Nested
    @DisplayName("registerView")
    class RegisterView {

        @Test
        @DisplayName("Dado un Pokémon sin vistas previas, cuando se registra una vista, entonces crea el documento con viewCount=1")
        void givenNoPreviousView_whenRegister_thenCreatesDocumentWithCountOne() {
            when(viewRepository.findByPokemonId(PIKACHU_ID)).thenReturn(Optional.empty());

            service.registerView(PIKACHU_ID, PIKACHU_NAME);

            ArgumentCaptor<PokemonViewDocument> captor = ArgumentCaptor.forClass(PokemonViewDocument.class);
            verify(viewRepository).save(captor.capture());

            PokemonViewDocument saved = captor.getValue();
            assertThat(saved.getPokemonId()).isEqualTo(PIKACHU_ID);
            assertThat(saved.getPokemonName()).isEqualTo(PIKACHU_NAME);
            assertThat(saved.getViewCount()).isEqualTo(1L);
            assertThat(saved.getLastViewed()).isNotNull();
        }

        @Test
        @DisplayName("Dado un Pokémon con vistas previas, cuando se registra una vista, entonces incrementa el contador existente")
        void givenExistingView_whenRegister_thenIncrementsCount() {
            PokemonViewDocument existing = PokemonViewDocument.builder()
                    .id("view-1")
                    .pokemonId(PIKACHU_ID)
                    .pokemonName(PIKACHU_NAME)
                    .viewCount(4L)
                    .lastViewed(LocalDateTime.now().minusDays(1))
                    .build();
            when(viewRepository.findByPokemonId(PIKACHU_ID)).thenReturn(Optional.of(existing));

            service.registerView(PIKACHU_ID, PIKACHU_NAME);

            ArgumentCaptor<PokemonViewDocument> captor = ArgumentCaptor.forClass(PokemonViewDocument.class);
            verify(viewRepository).save(captor.capture());

            assertThat(captor.getValue().getViewCount()).isEqualTo(5L);
        }
    }

    @Nested
    @DisplayName("mostViewed")
    class MostViewed {

        @Test
        @DisplayName("Dada una lista de vistas, cuando se piden las más vistas, entonces retorna el top limitado y mapeado")
        void givenViews_whenMostViewed_thenReturnsLimitedTop() {
            PokemonViewDocument pikachu = viewOf(25L, "Pikachu", 50L);
            PokemonViewDocument charizard = viewOf(6L, "Charizard", 30L);
            PokemonViewDocument bulbasaur = viewOf(1L, "Bulbasaur", 10L);
            when(viewRepository.findAll(Sort.by(Sort.Direction.DESC, "viewCount")))
                    .thenReturn(List.of(pikachu, charizard, bulbasaur));

            List<PokemonView> result = service.mostViewed(2);

            assertThat(result).hasSize(2);
            assertThat(result.get(0).getPokemonName()).isEqualTo("Pikachu");
            assertThat(result.get(1).getPokemonName()).isEqualTo("Charizard");
        }

        @Test
        @DisplayName("Dado un límite mayor a la cantidad de vistas, cuando se piden las más vistas, entonces retorna todas")
        void givenLimitGreaterThanAvailable_whenMostViewed_thenReturnsAll() {
            PokemonViewDocument pikachu = viewOf(25L, "Pikachu", 50L);
            when(viewRepository.findAll(Sort.by(Sort.Direction.DESC, "viewCount")))
                    .thenReturn(List.of(pikachu));

            List<PokemonView> result = service.mostViewed(10);

            assertThat(result).hasSize(1);
        }
    }

    @Nested
    @DisplayName("getAdminMetrics")
    class GetAdminMetrics {

        @Test
        void givenMultipleViews_whenGetMetrics_thenReturnsAggregatedMetrics() {
            List<PokemonViewDocument> views = List.of(
                    viewOf(25L, "Pikachu", 50L),
                    viewOf(6L, "Charizard", 30L),
                    viewOf(1L, "Bulbasaur", 10L),
                    viewOf(4L, "Charmander", 8L),
                    viewOf(7L, "Squirtle", 5L),
                    viewOf(150L, "Mewtwo", 2L)
            );
            when(viewRepository.findAll()).thenReturn(views);

            AdminMetrics metrics = service.getAdminMetrics();

            assertThat(metrics.getTotalConsultas()).isEqualTo(105L); // 50+30+10+8+5+2
            assertThat(metrics.getPokemonDistintosConsultados()).isEqualTo(6);
            assertThat(metrics.getTopPokemon()).hasSize(5);
            assertThat(metrics.getTopPokemon().get(0).getPokemonName()).isEqualTo("Pikachu");
            assertThat(metrics.getTopPokemon()).extracting(PokemonView::getPokemonName)
                    .doesNotContain("Mewtwo"); // el 6to más visto queda fuera del top 5
        }

        @Test
        void givenNoViews_whenGetMetrics_thenReturnsZeroedMetrics() {
            when(viewRepository.findAll()).thenReturn(List.of());

            AdminMetrics metrics = service.getAdminMetrics();

            assertThat(metrics.getTotalConsultas()).isZero();
            assertThat(metrics.getPokemonDistintosConsultados()).isZero();
            assertThat(metrics.getTopPokemon()).isEmpty();
        }
    }

    private PokemonViewDocument viewOf(Long pokemonId, String name, Long viewCount) {
        return PokemonViewDocument.builder()
                .pokemonId(pokemonId)
                .pokemonName(name)
                .viewCount(viewCount)
                .lastViewed(LocalDateTime.now())
                .build();
    }
}