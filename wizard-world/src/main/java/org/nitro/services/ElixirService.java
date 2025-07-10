package org.nitro.services;

import org.nitro.model.Elixir;

import java.util.List;

/**
 * Service responsible for managing and retrieving elixirs.
 */
public interface ElixirService {

    /**
     * Finds all elixirs that can be created using the given user-provided ingredients.
     * <p>
     * Elixirs that require unknown ingredients (i.e., have an empty ingredient list) must be excluded from the results.
     *
     * @param userIngredients the list of ingredients provided by the user.
     * @return a list of all possible {@link Elixir} instances that can be created with the provided ingredients.
     */
    List<Elixir> findMatchableElixirs(List<String> userIngredients);
}
