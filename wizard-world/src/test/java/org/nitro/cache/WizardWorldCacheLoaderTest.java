package org.nitro.cache;

import org.junit.jupiter.api.Test;
import org.nitro.client.WizardWorldClient;
import org.nitro.model.Elixir;
import org.nitro.model.Ingredient;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class WizardWorldCacheLoaderTest {

    private final WizardWorldClient wizardWorldClient;
    private final WizardWorldCacheLoader loader;

    WizardWorldCacheLoaderTest() {
        this.wizardWorldClient = mock();
        this.loader = new WizardWorldCacheLoader(wizardWorldClient);
    }

    @Test
    void load_ingredientsMatch_returnsListWithThem() {
        // assert
        String ingredientName = "ing1";
        List<Ingredient> ingredients = List.of(new Ingredient("1", ingredientName));
        List<Elixir> expectedResult = List.of(new Elixir("1", "elx1", "eff1", ingredients));
        when(wizardWorldClient.getElixirs()).thenReturn(expectedResult);

        // act
        List<Elixir> result = loader.load(List.of(ingredientName));

        // assert
        assertEquals(expectedResult, result);
    }

    @Test
    void load_ingredientsDoNotMatch_returnsEmptyList() {
        // assert
        String ingredientName = "wrongIngredient";
        List<Ingredient> ingredients = List.of(new Ingredient("1", "ing1"));
        List<Elixir> elixirs = List.of(new Elixir("1", "elx1", "eff1", ingredients));
        when(wizardWorldClient.getElixirs()).thenReturn(elixirs);

        List<Elixir> expectedResult = List.of();

        // act
        List<Elixir> result = loader.load(List.of(ingredientName));

        // assert
        assertEquals(expectedResult, result);
    }
}
