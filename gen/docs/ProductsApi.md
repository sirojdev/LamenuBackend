# ProductsApi

All URIs are relative to *http://0.0.0.0:9000/api/v1*

Method | HTTP request | Description
------------- | ------------- | -------------
[**merchantSettingsProductIdDelete**](ProductsApi.md#merchantSettingsProductIdDelete) | **DELETE** /merchant/settings/product/{id} | 
[**merchantSettingsProductIdGet**](ProductsApi.md#merchantSettingsProductIdGet) | **GET** /merchant/settings/product/{id} | 
[**merchantSettingsProductPost**](ProductsApi.md#merchantSettingsProductPost) | **POST** /merchant/settings/product | 
[**merchantSettingsProductPut**](ProductsApi.md#merchantSettingsProductPut) | **PUT** /merchant/settings/product | 
[**merchantSettingsProductsGet**](ProductsApi.md#merchantSettingsProductsGet) | **GET** /merchant/settings/products | 


<a name="merchantSettingsProductIdDelete"></a>
# **merchantSettingsProductIdDelete**
> merchantSettingsProductIdDelete(id)



Deletes a product by its ID

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.ProductsApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://0.0.0.0:9000/api/v1");

    ProductsApi apiInstance = new ProductsApi(defaultClient);
    Long id = 56L; // Long | 
    try {
      apiInstance.merchantSettingsProductIdDelete(id);
    } catch (ApiException e) {
      System.err.println("Exception when calling ProductsApi#merchantSettingsProductIdDelete");
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

<a name="merchantSettingsProductIdGet"></a>
# **merchantSettingsProductIdGet**
> ProductDto merchantSettingsProductIdGet(id)



Returns a product by its ID

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.ProductsApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://0.0.0.0:9000/api/v1");

    ProductsApi apiInstance = new ProductsApi(defaultClient);
    Long id = 56L; // Long | 
    try {
      ProductDto result = apiInstance.merchantSettingsProductIdGet(id);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling ProductsApi#merchantSettingsProductIdGet");
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

[**ProductDto**](ProductDto.md)

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

<a name="merchantSettingsProductPost"></a>
# **merchantSettingsProductPost**
> merchantSettingsProductPost(productDto)



Adds a new product

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.ProductsApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://0.0.0.0:9000/api/v1");

    ProductsApi apiInstance = new ProductsApi(defaultClient);
    ProductDto productDto = new ProductDto(); // ProductDto | A JSON object containing updated product information
    try {
      apiInstance.merchantSettingsProductPost(productDto);
    } catch (ApiException e) {
      System.err.println("Exception when calling ProductsApi#merchantSettingsProductPost");
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
 **productDto** | [**ProductDto**](ProductDto.md)| A JSON object containing updated product information |

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

<a name="merchantSettingsProductPut"></a>
# **merchantSettingsProductPut**
> merchantSettingsProductPut(productDto)



Adds a new product

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.ProductsApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://0.0.0.0:9000/api/v1");

    ProductsApi apiInstance = new ProductsApi(defaultClient);
    ProductDto productDto = new ProductDto(); // ProductDto | A JSON object containing updated product information
    try {
      apiInstance.merchantSettingsProductPut(productDto);
    } catch (ApiException e) {
      System.err.println("Exception when calling ProductsApi#merchantSettingsProductPut");
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
 **productDto** | [**ProductDto**](ProductDto.md)| A JSON object containing updated product information |

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

<a name="merchantSettingsProductsGet"></a>
# **merchantSettingsProductsGet**
> List&lt;ProductDto&gt; merchantSettingsProductsGet()



Returns a list of all products

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.ProductsApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://0.0.0.0:9000/api/v1");

    ProductsApi apiInstance = new ProductsApi(defaultClient);
    try {
      List<ProductDto> result = apiInstance.merchantSettingsProductsGet();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling ProductsApi#merchantSettingsProductsGet");
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

[**List&lt;ProductDto&gt;**](ProductDto.md)

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

