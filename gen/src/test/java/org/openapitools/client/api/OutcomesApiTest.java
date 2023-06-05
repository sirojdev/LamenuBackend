/*
 * Lamenu documentation Merchant
 * Lamenu documentation Merchant allows to you view the schema of Merchant Lamenu project
 *
 * The version of the OpenAPI document: 1.0.0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


package org.openapitools.client.api;

import org.openapitools.client.ApiException;
import org.openapitools.client.model.OutcomeDto;
import org.junit.Test;
import org.junit.Ignore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for OutcomesApi
 */
@Ignore
public class OutcomesApiTest {

    private final OutcomesApi api = new OutcomesApi();

    
    /**
     * 
     *
     * Deletes a outcome by its ID
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void merchantFinanceOutcomeIdDeleteTest() throws ApiException {
        Long id = null;
        api.merchantFinanceOutcomeIdDelete(id);

        // TODO: test validations
    }
    
    /**
     * 
     *
     * Returns a outcome by its ID
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void merchantFinanceOutcomeIdGetTest() throws ApiException {
        Long id = null;
        OutcomeDto response = api.merchantFinanceOutcomeIdGet(id);

        // TODO: test validations
    }
    
    /**
     * 
     *
     * Adds a new outcome
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void merchantFinanceOutcomePostTest() throws ApiException {
        OutcomeDto outcomeDto = null;
        api.merchantFinanceOutcomePost(outcomeDto);

        // TODO: test validations
    }
    
    /**
     * 
     *
     * Update the outcomes
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void merchantFinanceOutcomePutTest() throws ApiException {
        OutcomeDto outcomeDto = null;
        api.merchantFinanceOutcomePut(outcomeDto);

        // TODO: test validations
    }
    
    /**
     * 
     *
     * Returns a list of all Outcomes
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void merchantFinanceOutcomesGetTest() throws ApiException {
        List<OutcomeDto> response = api.merchantFinanceOutcomesGet();

        // TODO: test validations
    }
    
}
