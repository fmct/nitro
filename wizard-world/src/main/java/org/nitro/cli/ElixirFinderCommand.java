package org.nitro.cli;

import io.quarkus.picocli.runtime.annotations.TopCommand;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;
import org.nitro.model.Elixir;
import org.nitro.model.Ingredient;
import org.nitro.services.ElixirService;
import picocli.CommandLine;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@TopCommand
@CommandLine.Command(name = "elixir-finder", mixinStandardHelpOptions = true)
public class ElixirFinderCommand implements Runnable {

    private static final Logger LOGGER =  Logger.getLogger(ElixirFinderCommand.class);

    @CommandLine.Option(names = {"-i", "--ingredients"}, description = "Comma-separated list of ingredients", split = ",")
    List<String> ingredientsOption;

    private final ElixirService service;

    @Inject
    public ElixirFinderCommand(ElixirService service) {
        this.service = service;
    }

    @Override
    public void run() {
        LOGGER.info("Welcome to Elixir Finder CLI! Type 'exit' to quit.\n");
        Scanner scanner = new Scanner(System.in);
        runLoop(scanner::nextLine);
    }

    void runLoop(Supplier<String> inputSupplier) {
        while(true) {
            List<String> ingredients;
            if (ingredientsOption != null && !ingredientsOption.isEmpty()) {
                ingredients = normalize(ingredientsOption);
                ingredientsOption = null;
            } else {
                LOGGER.info("Enter ingredients you have (comma-separated): ");
                String input = inputSupplier.get();

                if ("exit".equalsIgnoreCase(input)) {
                    LOGGER.info("Goodbye!\n");
                    break;
                }
                ingredients = promptUserForIngredients(input);
            }

            try {
                List<Elixir> matchableElixirs = service.findMatchableElixirs(ingredients);
                if (matchableElixirs.isEmpty()) {
                    LOGGER.info("No elixirs can be created with the given ingredients.\n");
                    continue;
                }

                LOGGER.info("Based on your ingredients, you can possible brew:\n");
                for (Elixir elixir : matchableElixirs) {
                    printElixirInformation(elixir);
                }
            } catch(Exception e) {
                LOGGER.info("Not possible to retrieve the elixirs! Please try again later.\n");
            }
        }
    }

    private static void printElixirInformation(Elixir elixir) {
        String format = "%-30s %-40s %n";
        String effect = elixir.effect() == null ? "Unknown" : elixir.effect();
        String ingredients = elixir.ingredients().stream().map(Ingredient::name).collect(Collectors.joining(", "));
        LOGGER.info(format.formatted("Name:", elixir.name()));
        LOGGER.info(format.formatted("Effect:", effect));
        LOGGER.info(format.formatted( "Ingredients:", ingredients));
        LOGGER.info(String.format("%0" + 150 + "d", 0).replace("0", "-"));
    }

    private List<String> promptUserForIngredients(String input) {
        return Arrays.stream(input.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(String::toLowerCase)
                .toList();
    }

    private List<String> normalize(List<String> list) {
        return list.stream()
                .map(String::trim)
                .map(String::toLowerCase)
                .toList();
    }
}