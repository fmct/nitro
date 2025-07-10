package org.nitro.model;

import java.util.List;

public record Elixir(String id, String name, String effect, List<Ingredient> ingredients) {
}
