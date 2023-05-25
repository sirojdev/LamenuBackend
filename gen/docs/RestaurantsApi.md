# RestaurantsApi

All URIs are relative to *http://0.0.0.0:8181/api/v1*

Method | HTTP request | Description
------------- | ------------- | -------------
[**staffRestaurantIdDelete**](RestaurantsApi.md#staffRestaurantIdDelete) | **DELETE** /staff/restaurant/{id} | 
[**staffRestaurantIdGet**](RestaurantsApi.md#staffRestaurantIdGet) | **GET** /staff/restaurant/{id} | 
[**staffRestaurantPost**](RestaurantsApi.md#staffRestaurantPost) | **POST** /staff/restaurant | 
[**staffRestaurantPut**](RestaurantsApi.md#staffRestaurantPut) | **PUT** /staff/restaurant | 
[**staffRestaurantsGet**](RestaurantsApi.md#staffRestaurantsGet) | **GET** /staff/restaurants | 


<a name="staffRestaurantIdDelete"></a>
# **staffRestaurantIdDelete**
> staffRestaurantIdDelete(id)



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
      apiInstance.staffRestaurantIdDelete(id);
    } catch (ApiException e) {
      System.err.println("Exception when calling RestaurantsApi#staffRestaurantIdDelete");
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

<a name="staffRestaurantIdGet"></a>
# **staffRestaurantIdGet**
> RestaurantDto staffRestaurantIdGet(id)



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
      RestaurantDto result = apiInstance.staffRestaurantIdGet(id);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling RestaurantsApi#staffRestaurantIdGet");
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

<a name="staffRestaurantPost"></a>
# **staffRestaurantPost**
> staffRestaurantPost(restaurantDto)



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
      apiInstance.staffRestaurantPost(restaurantDto);
    } catch (ApiException e) {
      System.err.println("Exception when calling RestaurantsApi#staffRestaurantPost");
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

<a name="staffRestaurantPut"></a>
# **staffRestaurantPut**
> staffRestaurantPut(restaurantDto)



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
      apiInstance.staffRestaurantPut(restaurantDto);
    } catch (ApiException e) {
      System.err.println("Exception when calling RestaurantsApi#staffRestaurantPut");
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

<a name="staffRestaurantsGet"></a>
# **staffRestaurantsGet**
> List&lt;RestaurantDto&gt; staffRestaurantsGet()



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
      List<RestaurantDto> result = apiInstance.staffRestaurantsGet();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling RestaurantsApi#staffRestaurantsGet");
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

