package org.nitro.cli;

import org.jboss.logging.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.nitro.model.Elixir;
import org.nitro.model.Ingredient;
import org.nitro.services.ElixirService;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.clearAllCaches;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ElixirFinderCommandTest {

    private static final Logger LOGGER = mock();

    private final ElixirService service;
    private final ElixirFinderCommand command;

    ElixirFinderCommandTest() {
        this.service = mock();
        try (MockedStatic<Logger> loggerStatic = mockStatic(Logger.class)) {
            loggerStatic.when(() -> Logger.getLogger(ElixirFinderCommand.class)).thenReturn(LOGGER);
            this.command = new ElixirFinderCommand(service);
        }
    }

    @AfterEach
    void afterEach() {
        clearInvocations(LOGGER);
    }

    @AfterAll
    static void afterAll() {
        clearAllCaches();
    }

    @Test
    void run_invoked_invokesRunLoop() {
        // arrange
        ElixirFinderCommand spy = spy(command);
        doNothing().when(spy).runLoop(any());

        // act
        spy.run();

        // arrange
        verify(spy).runLoop(argThat(Objects::nonNull));
    }

    @Test
    void runLoop_invokedWithInput_callsServiceFindMatchableElixirsAndLogsItCorrectly() {
        // arrange
        String input = "Mandrake";
        List<String> inputs = List.of(input, "exit");
        Iterator<String> iterator = inputs.iterator();
        Elixir elixir = new Elixir("1", "Elixir1", "effect1", List.of(new Ingredient("1", input)));
        when(service.findMatchableElixirs(any())).thenReturn(List.of(elixir));

        // act
        command.runLoop(iterator::next);

        // arrange
        verify(service).findMatchableElixirs(List.of(input.toLowerCase()));
        verifyLoggerInvokedCorrectly(elixir, LOGGER);
    }

    @Test
    void runLoop_invokedWithInputCommaSeparated_callsServiceFindMatchableElixirsAndLogsItCorrectly() {
        // arrange
        String mandrake = "Mandrake";
        String cheese = "cheese";
        String input = "Mandrake,Cheese";
        List<String> inputs = List.of(input, "exit");
        Iterator<String> iterator = inputs.iterator();
        List<Ingredient> ingredients = List.of(new Ingredient("1", mandrake), new Ingredient("2", cheese));
        Elixir elixir = new Elixir("1", "Elixir1", "effect1", ingredients);
        when(service.findMatchableElixirs(any())).thenReturn(List.of(elixir));

        // act
        command.runLoop(iterator::next);

        // arrange
        verify(service).findMatchableElixirs(List.of(mandrake.toLowerCase(), cheese.toLowerCase()));
        verifyLoggerInvokedCorrectly(elixir, LOGGER);
    }

    @Test
    void runLoop_serviceThrowsException_logsUsefulInformation() {
        // arrange
        String input = "Mandrake";
        List<String> inputs = List.of(input, "exit");
        Iterator<String> iterator = inputs.iterator();
        when(service.findMatchableElixirs(any())).thenThrow(new RuntimeException());

        // act
        command.runLoop(iterator::next);

        // arrange
        verify(service).findMatchableElixirs(List.of(input.toLowerCase()));
        verify(LOGGER).info("Not possible to retrieve the elixirs! Please try again later.\n");
    }

    @Test
    void runLoop_invokedWithExitInput_exitsProgram() {
        // arrange
        String input = "exit";
        List<String> inputs = List.of(input);
        Iterator<String> iterator = inputs.iterator();

        // act
        command.runLoop(iterator::next);

        // arrange
        verify(LOGGER).info("Goodbye!\n");
        verify(service, never()).findMatchableElixirs(List.of(input.toLowerCase()));
    }

    @Test
    void runLoop_invokedWithIngredientsFromPicocli_callsServiceFindMatchableElixirsCorrectly() {
        // arrange
        String input = "Mandrake";
        command.ingredientsOption = List.of(input);
        List<String> inputs = List.of("otherMandrake", "exit");
        Iterator<String> iterator = inputs.iterator();

        // act
        command.runLoop(iterator::next);

        // arrange
        verify(service).findMatchableElixirs(List.of(input.toLowerCase()));
    }

    private static void verifyLoggerInvokedCorrectly(Elixir elixir, Logger logger) {
        String format = "%-30s %-40s %n";
        String effect = elixir.effect() == null ? "Unknown" : elixir.effect();
        String ingredients = elixir.ingredients().stream().map(Ingredient::name).collect(Collectors.joining(", "));
        verify(logger).info(format.formatted("Name:", elixir.name()));
        verify(logger).info(format.formatted("Effect:", effect));
        verify(logger).info(format.formatted("Ingredients:", ingredients));
        verify(logger).info(String.format("%0" + 150 + "d", 0).replace("0", "-"));
    }
}
