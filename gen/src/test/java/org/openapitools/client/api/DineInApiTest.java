/*
 * Lamenu documentation Client
 * Lamenu documentation Clint allows to you view the schema of Client Lamenu project
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
import org.openapitools.client.model.BookDto;
import org.junit.Test;
import org.junit.Ignore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for DineInApi
 */
@Ignore
public class DineInApiTest {

    private final DineInApi api = new DineInApi();

    
    /**
     * 
     *
     * Deletes a booking by its id
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void clientBookIdDeleteTest() throws ApiException {
        Long id = null;
        api.clientBookIdDelete(id);

        // TODO: test validations
    }
    
    /**
     * 
     *
     * Returns a book by its id
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void clientBookIdGetTest() throws ApiException {
        Long id = null;
        BookDto response = api.clientBookIdGet(id);

        // TODO: test validations
    }
    
    /**
     * 
     *
     * Adds a new booking
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void clientBookPostTest() throws ApiException {
        BookDto bookDto = null;
        api.clientBookPost(bookDto);

        // TODO: test validations
    }
    
    /**
     * 
     *
     * Update a booking&#39;s information
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void clientBookPutTest() throws ApiException {
        BookDto bookDto = null;
        api.clientBookPut(bookDto);

        // TODO: test validations
    }
    
    /**
     * 
     *
     * Returns a list of all books
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void clientBooksGetTest() throws ApiException {
        List<BookDto> response = api.clientBooksGet();

        // TODO: test validations
    }
    
}
