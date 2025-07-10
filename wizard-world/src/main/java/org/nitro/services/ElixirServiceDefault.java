package org.nitro.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.nitro.cache.WizardWorldCache;
import org.nitro.model.Elixir;

import java.util.List;

@ApplicationScoped
public class ElixirServiceDefault implements ElixirService {

    private final WizardWorldCache wizardWorldCache;

    @Inject
    public ElixirServiceDefault(WizardWorldCache wizardWorldCache) {
        this.wizardWorldCache = wizardWorldCache;
    }

    public List<Elixir> findMatchableElixirs(List<String> userIngredients) {
        return wizardWorldCache
                .getElixirs(userIngredients)
                .orElse(List.of())
                .stream()
                .filter(elixir -> !elixir.ingredients().isEmpty())
                .toList();
    }
}
