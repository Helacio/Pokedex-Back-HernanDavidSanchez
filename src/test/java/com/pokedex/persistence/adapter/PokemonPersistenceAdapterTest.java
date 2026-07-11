package com.pokedex.persistence.adapter;

import com.pokedex.core.model.Pokemon;
import com.pokedex.core.model.PokemonStats;
import com.pokedex.core.service.interfaces.PokemonFilterCriteria;
import com.pokedex.persistence.entity.relational.PokemonEntity;
import com.pokedex.persistence.mapper.PokemonPersistenceMapper;
import com.pokedex.persistence.repository.relational.PokemonJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("PokemonPersistenceAdapter")
class PokemonPersistenceAdapterTest {

    @Mock private PokemonJpaRepository repository;
    @Mock private PokemonPersistenceMapper mapper;

    @InjectMocks private PokemonPersistenceAdapter adapter;

    private Pokemon pikachu;
    private PokemonEntity pikachuEntity;

    @BeforeEach
    void setUp() {
        pikachu = Pokemon.builder()
                .id(1L)
                .nationalNumber(25)
                .name("Pikachu")
                .types(List.of("Electric"))
                .region("Kanto")
                .generation(1)
                .hasMega(false)
                .stats(PokemonStats.builder()
                        .hp(35).attack(55).defense(40)
                        .specialAttack(50).specialDefense(50).speed(90)
                        .build())
                .build();

        pikachuEntity = PokemonEntity.builder()
                .id(1L)
                .nationalNumber(25)
                .name("Pikachu")
                .build();
    }

    @Nested
    @DisplayName("findById")
    class FindById {

        @Test
        @DisplayName("Dado un id existente, cuando se busca, entonces retorna el Pokémon mapeado")
        void givenExistingId_whenFindById_thenReturnsPokemon() {
            when(repository.findByIdWithTypesAndStats(1L)).thenReturn(Optional.of(pikachuEntity));
            when(mapper.toDomain(pikachuEntity)).thenReturn(pikachu);

            Optional<Pokemon> result = adapter.findById(1L);

            assertThat(result).isPresent();
            assertThat(result.get().getName()).isEqualTo("Pikachu");
        }

        @Test
        @DisplayName("Dado un id inexistente, cuando se busca, entonces retorna vacío")
        void givenMissingId_whenFindById_thenReturnsEmpty() {
            when(repository.findByIdWithTypesAndStats(99L)).thenReturn(Optional.empty());

            Optional<Pokemon> result = adapter.findById(99L);

            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("findAll")
    class FindAll {

        @Test
        @DisplayName("Dado que existen Pokémon, cuando se listan, entonces retorna la página mapeada")
        void givenPokemonExist_whenFindAll_thenReturnsMappedPage() {
            Pageable pageable = PageRequest.of(0, 20);
            Page<PokemonEntity> entityPage = new PageImpl<>(List.of(pikachuEntity));
            when(repository.findAllWithTypes(pageable)).thenReturn(entityPage);
            when(mapper.toDomain(pikachuEntity)).thenReturn(pikachu);

            Page<Pokemon> result = adapter.findAll(pageable);

            assertThat(result.getContent()).hasSize(1);
            assertThat(result.getContent().get(0).getNationalNumber()).isEqualTo(25);
        }
    }

    @Nested
    @DisplayName("findByNationalNumber")
    class FindByNationalNumber {

        @Test
        @DisplayName("Dado un número nacional existente, cuando se busca, entonces retorna el Pokémon")
        void givenExistingNumber_whenFindByNationalNumber_thenReturnsPokemon() {
            when(repository.findByNationalNumberWithDetails(25)).thenReturn(Optional.of(pikachuEntity));
            when(mapper.toDomain(pikachuEntity)).thenReturn(pikachu);

            Optional<Pokemon> result = adapter.findByNationalNumber(25);

            assertThat(result).isPresent();
            assertThat(result.get().getName()).isEqualTo("Pikachu");
        }
    }

    @Nested
    @DisplayName("existsByNationalNumber")
    class ExistsByNationalNumber {

        @Test
        @DisplayName("Dado un número nacional registrado, cuando se verifica, entonces retorna true")
        void givenExistingNumber_whenExists_thenReturnsTrue() {
            when(repository.existsByNationalNumber(25)).thenReturn(true);

            assertThat(adapter.existsByNationalNumber(25)).isTrue();
        }

        @Test
        @DisplayName("Dado un número nacional no registrado, cuando se verifica, entonces retorna false")
        void givenMissingNumber_whenExists_thenReturnsFalse() {
            when(repository.existsByNationalNumber(999)).thenReturn(false);

            assertThat(adapter.existsByNationalNumber(999)).isFalse();
        }
    }

    @Nested
    @DisplayName("save")
    class Save {

        @Test
        @DisplayName("Dado un Pokémon, cuando se guarda, entonces mapea y persiste correctamente")
        void givenPokemon_whenSave_thenMapsAndPersists() {
            when(mapper.toEntity(pikachu)).thenReturn(pikachuEntity);
            when(repository.save(pikachuEntity)).thenReturn(pikachuEntity);
            when(mapper.toDomain(pikachuEntity)).thenReturn(pikachu);

            Pokemon result = adapter.save(pikachu);

            assertThat(result.getName()).isEqualTo("Pikachu");
            verify(mapper).toEntity(pikachu);
            verify(repository).save(pikachuEntity);
            verify(mapper).toDomain(pikachuEntity);
        }
    }

    @Nested
    @DisplayName("deleteById")
    class DeleteById {

        @Test
        @DisplayName("Dado un id, cuando se elimina, entonces delega al repositorio")
        void givenId_whenDelete_thenDelegatesToRepository() {
            adapter.deleteById(1L);

            verify(repository).deleteById(1L);
        }
    }

    @Nested
    @DisplayName("findByCriteria")
    class FindByCriteria {

        @Test
        @DisplayName("Dado criterios de filtro, cuando se busca, entonces retorna la lista mapeada")
        void givenCriteria_whenFindByCriteria_thenReturnsMappedList() {
            PokemonFilterCriteria criteria = new PokemonFilterCriteria(
                    List.of("Electric"), "Kanto", 1, false, 200, 600);

            when(repository.findAll(any(Specification.class))).thenReturn(List.of(pikachuEntity));
            when(mapper.toDomain(pikachuEntity)).thenReturn(pikachu);

            List<Pokemon> result = adapter.findByCriteria(criteria);

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getTypes()).containsExactly("Electric");
            verify(repository).findAll(any(Specification.class));
        }
    }
}
