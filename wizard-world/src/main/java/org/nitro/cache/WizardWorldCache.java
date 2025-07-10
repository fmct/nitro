package org.nitro.cache;

import org.nitro.model.Elixir;

import java.util.List;
import java.util.Optional;

/**
 * A cache for retrieving elixirs from the Wizard World.
 */
public interface WizardWorldCache {

    /**
     * Retrieves all elixirs that can be created from the given ingredients.
     * <p>
     * If the elixirs are not found in the cache, they must be fetched from the
     * corresponding REST API and cached for future access.
     *
     * @param ingredients the list of available ingredient names.
     * @return an {@link Optional} containing a list of possible {@link Elixir} instances
     *         that can be made with the provided ingredients, or an empty Optional if none are found.
     */
    Optional<List<Elixir>> getElixirs(List<String> ingredients);
}
