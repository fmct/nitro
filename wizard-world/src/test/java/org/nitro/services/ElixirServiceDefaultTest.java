package org.nitro.services;

import org.junit.jupiter.api.Test;
import org.nitro.cache.WizardWorldCache;
import org.nitro.model.Elixir;
import org.nitro.model.Ingredient;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ElixirServiceDefaultTest {

    private final WizardWorldCache cache;
    private final ElixirServiceDefault service;

    ElixirServiceDefaultTest() {
        this.cache = mock();
        this.service = new ElixirServiceDefault(cache);
    }

    @Test
    void findMatchableElixirs_elixirExistsInCacheButWithoutIngredients_returnsEmptyList() {
        // arrange
        List<String> ingredients = List.of("testIngredient");
        List<Elixir> elixirsWithoutIngredients = List.of(mock(Elixir.class));
        when(cache.getElixirs(ingredients)).thenReturn(Optional.of(elixirsWithoutIngredients));

        List<Elixir> expectedResult = List.of();

        // act
        List<Elixir> result = service.findMatchableElixirs(ingredients);

        // assert
        assertEquals(expectedResult, result);
    }

    @Test
    void findMatchableElixirs_elixirDoNotExistsInCache_returnsEmptyList() {
        // arrange
        List<String> ingredients = List.of("testIngredient");
        when(cache.getElixirs(ingredients)).thenReturn(Optional.empty());

        List<Elixir> expectedResult = List.of();

        // act
        List<Elixir> result = service.findMatchableElixirs(ingredients);

        // assert
        assertEquals(expectedResult, result);
    }

    @Test
    void findMatchableElixirs_elixirExistsInCacheWithIngredients_returnsEmptyList() {
        // arrange
        String testIngredient = "testIngredient";
        List<String> ingredients = List.of(testIngredient);
        Elixir elixir = mock(Elixir.class);
        when(elixir.ingredients()).thenReturn(List.of(new Ingredient("1", testIngredient)));
        List<Elixir> elixirsWithoutIngredients = List.of(elixir);
        when(cache.getElixirs(ingredients)).thenReturn(Optional.of(elixirsWithoutIngredients));

        List<Elixir> expectedResult = List.of(elixir);

        // act
        List<Elixir> result = service.findMatchableElixirs(ingredients);

        // assert
        assertEquals(expectedResult, result);
    }
}
