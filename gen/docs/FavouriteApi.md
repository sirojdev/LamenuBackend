# FavouriteApi

All URIs are relative to *http://127.0.0.1:9000/v1*

Method | HTTP request | Description
------------- | ------------- | -------------
[**clientFavouriteDelete**](FavouriteApi.md#clientFavouriteDelete) | **DELETE** /client/favourite | 
[**clientFavouriteGet**](FavouriteApi.md#clientFavouriteGet) | **GET** /client/favourite | 
[**clientFavouriteIdDelete**](FavouriteApi.md#clientFavouriteIdDelete) | **DELETE** /client/favourite/{id} | 
[**clientFavouritePost**](FavouriteApi.md#clientFavouritePost) | **POST** /client/favourite | 
[**clientFavouritePut**](FavouriteApi.md#clientFavouritePut) | **PUT** /client/favourite | 


<a name="clientFavouriteDelete"></a>
# **clientFavouriteDelete**
> clientFavouriteDelete()



Deletes a favourite by its id

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.FavouriteApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://127.0.0.1:9000/v1");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    FavouriteApi apiInstance = new FavouriteApi(defaultClient);
    try {
      apiInstance.clientFavouriteDelete();
    } catch (ApiException e) {
      System.err.println("Exception when calling FavouriteApi#clientFavouriteDelete");
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

null (empty response body)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | OK |  -  |
**400** | Bad Request |  -  |

<a name="clientFavouriteGet"></a>
# **clientFavouriteGet**
> List&lt;FavouriteDto&gt; clientFavouriteGet()



Returns a list of all favourites

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.FavouriteApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://127.0.0.1:9000/v1");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    FavouriteApi apiInstance = new FavouriteApi(defaultClient);
    try {
      List<FavouriteDto> result = apiInstance.clientFavouriteGet();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling FavouriteApi#clientFavouriteGet");
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

[**List&lt;FavouriteDto&gt;**](FavouriteDto.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | OK |  -  |
**204** | No Content |  -  |

<a name="clientFavouriteIdDelete"></a>
# **clientFavouriteIdDelete**
> clientFavouriteIdDelete(id)



Deletes a favourite by its id

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.FavouriteApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://127.0.0.1:9000/v1");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    FavouriteApi apiInstance = new FavouriteApi(defaultClient);
    Long id = 56L; // Long | 
    try {
      apiInstance.clientFavouriteIdDelete(id);
    } catch (ApiException e) {
      System.err.println("Exception when calling FavouriteApi#clientFavouriteIdDelete");
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

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | OK |  -  |
**400** | Bad Request |  -  |

<a name="clientFavouritePost"></a>
# **clientFavouritePost**
> clientFavouritePost(favouriteDto)



Adds a new favourite

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.FavouriteApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://127.0.0.1:9000/v1");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    FavouriteApi apiInstance = new FavouriteApi(defaultClient);
    FavouriteDto favouriteDto = new FavouriteDto(); // FavouriteDto | A JSON object containing favourite's information
    try {
      apiInstance.clientFavouritePost(favouriteDto);
    } catch (ApiException e) {
      System.err.println("Exception when calling FavouriteApi#clientFavouritePost");
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
 **favouriteDto** | [**FavouriteDto**](FavouriteDto.md)| A JSON object containing favourite&#39;s information |

### Return type

null (empty response body)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: Not defined

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | OK |  -  |

<a name="clientFavouritePut"></a>
# **clientFavouritePut**
> clientFavouritePut(favouriteDto)



Update a favourite&#39;s information

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.FavouriteApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://127.0.0.1:9000/v1");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    FavouriteApi apiInstance = new FavouriteApi(defaultClient);
    FavouriteDto favouriteDto = new FavouriteDto(); // FavouriteDto | A JSON object containing updated favourite's information
    try {
      apiInstance.clientFavouritePut(favouriteDto);
    } catch (ApiException e) {
      System.err.println("Exception when calling FavouriteApi#clientFavouritePut");
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
 **favouriteDto** | [**FavouriteDto**](FavouriteDto.md)| A JSON object containing updated favourite&#39;s information |

### Return type

null (empty response body)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: Not defined

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | OK |  -  |

