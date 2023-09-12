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
import org.openapitools.client.model.StaffDto;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for StaffApi
 */
@Disabled
public class StaffApiTest {

    private final StaffApi api = new StaffApi();

    /**
     * Returns a list of all staff
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void merchantSettingsStaffGetTest() throws ApiException {
        List<StaffDto> response = api.merchantSettingsStaffGet();
        // TODO: test validations
    }

    /**
     * Deletes a staff by its ID
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void merchantSettingsStaffIdDeleteTest() throws ApiException {
        Long id = null;
        api.merchantSettingsStaffIdDelete(id);
        // TODO: test validations
    }

    /**
     * Returns a staff by its ID
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void merchantSettingsStaffIdGetTest() throws ApiException {
        Long id = null;
        StaffDto response = api.merchantSettingsStaffIdGet(id);
        // TODO: test validations
    }

    /**
     * Adds a new staff
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void merchantSettingsStaffPostTest() throws ApiException {
        StaffDto staffDto = null;
        api.merchantSettingsStaffPost(staffDto);
        // TODO: test validations
    }

    /**
     * Adds a new staff
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void merchantSettingsStaffPutTest() throws ApiException {
        StaffDto staffDto = null;
        api.merchantSettingsStaffPut(staffDto);
        // TODO: test validations
    }

}
