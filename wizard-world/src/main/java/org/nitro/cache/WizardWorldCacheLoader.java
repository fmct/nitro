package org.nitro.cache;

import com.google.common.cache.CacheLoader;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.nitro.client.WizardWorldClient;
import org.nitro.model.Elixir;

import java.util.HashSet;
import java.util.List;

@ApplicationScoped
public class WizardWorldCacheLoader extends CacheLoader<List<String>, List<Elixir>> {

    private final WizardWorldClient wizardWorldClient;

    @Inject
    public WizardWorldCacheLoader(@RestClient WizardWorldClient wizardWorldClient) {
        this.wizardWorldClient = wizardWorldClient;
    }

    @Override
    public List<Elixir> load(List<String> ingredients) {
        return wizardWorldClient
                .getElixirs()
                .stream()
                .filter(elixir ->
                                new HashSet<>(ingredients).containsAll(
                                        elixir.ingredients().stream().map(i -> i.name().toLowerCase()).toList()
                                )
                )
                .toList();
    }
}
