# RestaurantsApi

All URIs are relative to *http://0.0.0.0:8181/api/v1*

Method | HTTP request | Description
------------- | ------------- | -------------
[**restaurantIdDelete**](RestaurantsApi.md#restaurantIdDelete) | **DELETE** /restaurant/{id} | 
[**restaurantIdGet**](RestaurantsApi.md#restaurantIdGet) | **GET** /restaurant/{id} | 
[**restaurantPost**](RestaurantsApi.md#restaurantPost) | **POST** /restaurant | 
[**restaurantPut**](RestaurantsApi.md#restaurantPut) | **PUT** /restaurant | 
[**restaurantsGet**](RestaurantsApi.md#restaurantsGet) | **GET** /restaurants | 


<a name="restaurantIdDelete"></a>
# **restaurantIdDelete**
> restaurantIdDelete(id)



Deletes a restaurant by its ID

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.RestaurantsApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://0.0.0.0:8181/api/v1");

    RestaurantsApi apiInstance = new RestaurantsApi(defaultClient);
    Long id = 56L; // Long | 
    try {
      apiInstance.restaurantIdDelete(id);
    } catch (ApiException e) {
      System.err.println("Exception when calling RestaurantsApi#restaurantIdDelete");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **Long**|  |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | OK |  -  |
**400** | Bad Request |  -  |

<a name="restaurantIdGet"></a>
# **restaurantIdGet**
> RestaurantDto restaurantIdGet(id)



Returns a restaurant by its ID

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.RestaurantsApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://0.0.0.0:8181/api/v1");

    RestaurantsApi apiInstance = new RestaurantsApi(defaultClient);
    Long id = 56L; // Long | 
    try {
      RestaurantDto result = apiInstance.restaurantIdGet(id);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling RestaurantsApi#restaurantIdGet");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **Long**|  |

### Return type

[**RestaurantDto**](RestaurantDto.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | OK |  -  |
**204** | No Content |  -  |
**400** | Bad Request |  -  |

<a name="restaurantPost"></a>
# **restaurantPost**
> restaurantPost(restaurantDto)



Adds a new restaurant

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.RestaurantsApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://0.0.0.0:8181/api/v1");

    RestaurantsApi apiInstance = new RestaurantsApi(defaultClient);
    RestaurantDto restaurantDto = new RestaurantDto(); // RestaurantDto | A JSON object containing updated product information
    try {
      apiInstance.restaurantPost(restaurantDto);
    } catch (ApiException e) {
      System.err.println("Exception when calling RestaurantsApi#restaurantPost");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **restaurantDto** | [**RestaurantDto**](RestaurantDto.md)| A JSON object containing updated product information |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: Not defined

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | OK |  -  |

<a name="restaurantPut"></a>
# **restaurantPut**
> restaurantPut(restaurantDto)



Update the restaurant

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.RestaurantsApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://0.0.0.0:8181/api/v1");

    RestaurantsApi apiInstance = new RestaurantsApi(defaultClient);
    RestaurantDto restaurantDto = new RestaurantDto(); // RestaurantDto | A JSON object containing updated product information
    try {
      apiInstance.restaurantPut(restaurantDto);
    } catch (ApiException e) {
      System.err.println("Exception when calling RestaurantsApi#restaurantPut");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **restaurantDto** | [**RestaurantDto**](RestaurantDto.md)| A JSON object containing updated product information |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: Not defined

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | OK |  -  |

<a name="restaurantsGet"></a>
# **restaurantsGet**
> List&lt;RestaurantDto&gt; restaurantsGet()



Returns a list of all Restaurants

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.RestaurantsApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://0.0.0.0:8181/api/v1");

    RestaurantsApi apiInstance = new RestaurantsApi(defaultClient);
    try {
      List<RestaurantDto> result = apiInstance.restaurantsGet();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling RestaurantsApi#restaurantsGet");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**List&lt;RestaurantDto&gt;**](RestaurantDto.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | OK |  -  |
**204** | No Content |  -  |

