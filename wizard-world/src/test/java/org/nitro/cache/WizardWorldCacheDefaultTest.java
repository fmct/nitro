package org.nitro.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.nitro.model.Elixir;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class WizardWorldCacheDefaultTest {

    private final LoadingCache<List<String>, List<Elixir>> cache;
    private final WizardWorldCacheDefault wizardWorldCache;

    WizardWorldCacheDefaultTest() {
        cache = mock();
        wizardWorldCache = new WizardWorldCacheDefault(cache);
    }

    @Test
    @SuppressWarnings("rawtypes, unchecked")
    void constructor_invoked_cacheIsBuild() {
        // arrange
        int expectedMaximumSize = 300;
        int expectedExpireAfterAccessInMinutes = 30;
        WizardWorldCacheLoader loader = mock();

        CacheBuilder<Object, Object> mockBuilder = mock();
        LoadingCache mockCache = mock();

        when(mockBuilder.maximumSize(expectedMaximumSize)).thenReturn(mockBuilder);
        when(mockBuilder.expireAfterAccess(expectedExpireAfterAccessInMinutes, TimeUnit.MINUTES)).thenReturn(mockBuilder);
        when(mockBuilder.build()).thenReturn(mockCache);

        try (MockedStatic<CacheBuilder> mockedStatic = mockStatic(CacheBuilder.class)) {
            mockedStatic.when(CacheBuilder::newBuilder).thenReturn(mockBuilder);

            // act
            new WizardWorldCacheDefault(loader);

            // assert
            verify(mockBuilder).maximumSize(expectedMaximumSize);
            verify(mockBuilder).expireAfterAccess(expectedExpireAfterAccessInMinutes, TimeUnit.MINUTES);
            verify(mockBuilder).build(loader);
        }
    }

    @Test
    void getElixirs_existsElixirInCache_returnsIt() {
        // arrange
        List<String> ingredients = List.of("ingredient1", "ingredient2", "ingredient3");
        List<Elixir> elixirList = List.of(mock(Elixir.class));
        when(cache.getUnchecked(ingredients)).thenReturn(elixirList);

        Optional<List<Elixir>> expectedResult = Optional.of(elixirList);

        // act
        Optional<List<Elixir>> result = wizardWorldCache.getElixirs(ingredients);

        // assert
        assertEquals(expectedResult, result);
    }

    @Test
    void getElixirs_doNotExistsElixirsInCache_returnsEmpty() {
        // arrange
        List<String> ingredients = List.of("ingredient1", "ingredient2", "ingredient3");
        when(cache.getUnchecked(ingredients)).thenReturn(null);

        Optional<List<Elixir>> expectedResult = Optional.empty();

        // act
        Optional<List<Elixir>> result = wizardWorldCache.getElixirs(ingredients);

        // assert
        assertEquals(expectedResult, result);
    }
}
