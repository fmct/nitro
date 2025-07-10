package org.nitro.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.nitro.model.Elixir;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class WizardWorldCacheDefault implements WizardWorldCache {

    private final LoadingCache<List<String>, List<Elixir>> cache;

    @Inject
    public WizardWorldCacheDefault(WizardWorldCacheLoader loader) {
        this(
                CacheBuilder.newBuilder()
                        .expireAfterAccess(30, TimeUnit.MINUTES)
                        .maximumSize(300)
                        .build(loader)
        );
    }

    /**
     * Used only for unit tests.
     *
     * @param cache internal cache.
     */
    WizardWorldCacheDefault(LoadingCache<List<String>, List<Elixir>> cache) {
        this.cache = cache;
    }

    @Override
    public Optional<List<Elixir>> getElixirs(List<String> ingredients) {
        return Optional.ofNullable(cache.getUnchecked(ingredients));
    }
}
