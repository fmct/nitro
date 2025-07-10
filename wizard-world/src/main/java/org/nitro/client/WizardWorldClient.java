package org.nitro.client;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.nitro.model.Elixir;
import org.nitro.model.Ingredient;

import java.util.List;

/**
 * Client responsible to make calls to the wizard-world-api Rest API.
 */
@RegisterRestClient(configKey = "wizard-world-client")
public interface WizardWorldClient {
    /**
     * Get all ingredients.
     *
     * @return a list of ingredients;
     */
    @Path("ingredients")
    @GET
    List<Ingredient> getIngredients();

    /**
     * Get all elixirs.
     *
     * @return a list of elixirs.
     */
    @Path("Elixirs")
    @GET
    List<Elixir> getElixirs();
}
