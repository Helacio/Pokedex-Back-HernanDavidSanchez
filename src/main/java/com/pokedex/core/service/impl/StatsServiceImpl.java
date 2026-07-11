package com.pokedex.core.service.impl;

import com.pokedex.core.model.AdminMetrics;
import com.pokedex.core.model.PokemonView;
import com.pokedex.core.service.interfaces.StatsService;
import com.pokedex.persistence.entity.document.PokemonViewDocument;
import com.pokedex.persistence.repository.document.PokemonViewMongoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatsServiceImpl implements StatsService {

    private final PokemonViewMongoRepository viewRepository;

    @Override
    public void registerView(Long pokemonId, String pokemonName) {
        PokemonViewDocument view = viewRepository.findByPokemonId(pokemonId)
                .orElseGet(() -> PokemonViewDocument.builder()
                        .pokemonId(pokemonId)
                        .pokemonName(pokemonName)
                        .viewCount(0L)
                        .build());

        view.setViewCount(view.getViewCount() + 1);
        view.setLastViewed(LocalDateTime.now());
        viewRepository.save(view);
    }

    @Override
    public List<PokemonView> mostViewed(int limit) {
        return viewRepository.findAll(Sort.by(Sort.Direction.DESC, "viewCount"))
                .stream()
                .limit(limit)
                .map(this::toDomain)
                .toList();
    }

    @Override
    public AdminMetrics getAdminMetrics() {
        List<PokemonViewDocument> all = viewRepository.findAll();
        long totalConsultas = all.stream().mapToLong(PokemonViewDocument::getViewCount).sum();

        List<PokemonView> top5 = all.stream()
                .sorted((a, b) -> Long.compare(b.getViewCount(), a.getViewCount()))
                .limit(5)
                .map(this::toDomain)
                .toList();

        return AdminMetrics.builder()
                .totalConsultas(totalConsultas)
                .pokemonDistintosConsultados(all.size())
                .topPokemon(top5)
                .build();
    }

    private PokemonView toDomain(PokemonViewDocument doc) {
        return PokemonView.builder()
                .pokemonId(doc.getPokemonId())
                .pokemonName(doc.getPokemonName())
                .viewCount(doc.getViewCount())
                .lastViewed(doc.getLastViewed())
                .build();
    }
}