package com.pokedex.persistence.adapter;

import com.pokedex.core.model.Pokemon;
import com.pokedex.core.port.PokemonPersistencePort;
import com.pokedex.core.service.interfaces.PokemonFilterCriteria;
import com.pokedex.persistence.entity.relational.PokemonEntity;
import com.pokedex.persistence.mapper.PokemonPersistenceMapper;
import com.pokedex.persistence.repository.relational.PokemonJpaRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PokemonPersistenceAdapter implements PokemonPersistencePort {

    private final PokemonJpaRepository repository;
    private final PokemonPersistenceMapper mapper;

    @Override
    public Optional<Pokemon> findById(Long id) {
        return repository.findByIdWithTypesAndStats(id)
            .map(mapper::toDomain);
    }

    @Override
    public Page<Pokemon> findAll(Pageable pageable) {
        return repository.findAllWithTypes(pageable)
            .map(mapper::toDomain);
    }

    @Override
    public Optional<Pokemon> findByNationalNumber(Integer number) {
        return repository.findByNationalNumberWithDetails(number)
            .map(mapper::toDomain);
    }

    @Override
    public boolean existsByNationalNumber(Integer number) {
        return repository.existsByNationalNumber(number);
    }

    @Override
    public Pokemon save(Pokemon pokemon) {
        PokemonEntity entity = mapper.toEntity(pokemon);
        return mapper.toDomain(repository.save(entity));
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<Pokemon> findByCriteria(PokemonFilterCriteria criteria) {
        Specification<PokemonEntity> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (criteria.types() != null && !criteria.types().isEmpty()) {
                predicates.add(root.join("types").<String>get("name").in(criteria.types()));
            }
            if (criteria.region() != null) {
                predicates.add(cb.equal(root.get("region").get("name"), criteria.region()));
            }
            if (criteria.generation() != null) {
                predicates.add(cb.equal(root.get("generation"), criteria.generation()));
            }
            if (criteria.hasMega() != null) {
                predicates.add(cb.equal(root.get("hasMega"), criteria.hasMega()));
            }
            if (criteria.minTotalStats() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("stats").get("total"), criteria.minTotalStats()));
            }
            if (criteria.maxTotalStats() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("stats").get("total"), criteria.maxTotalStats()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        // findAll(Specification) tiene @EntityGraph en el repositorio — sin N+1
        return repository.findAll(spec).stream()
            .map(mapper::toDomain)
            .toList();
    }
}