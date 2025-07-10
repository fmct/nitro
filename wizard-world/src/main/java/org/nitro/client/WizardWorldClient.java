package org.nitro.client;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.nitro.model.Elixir;
import org.nitro.model.Ingredient;

import java.util.List;

@RegisterRestClient(configKey = "wizard-world-client")
public interface WizardWorldClient {

    @Path("ingredients")
    @GET
    List<Ingredient> getIngredients();

    @Path("Elixirs")
    @GET
    List<Elixir> getElixirs();
}
