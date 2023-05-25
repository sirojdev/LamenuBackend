# ProductsApi

All URIs are relative to *http://0.0.0.0:8181/api/v1*

Method | HTTP request | Description
------------- | ------------- | -------------
[**staffProductIdDelete**](ProductsApi.md#staffProductIdDelete) | **DELETE** /staff/product/{id} | 
[**staffProductIdGet**](ProductsApi.md#staffProductIdGet) | **GET** /staff/product/{id} | 
[**staffProductPost**](ProductsApi.md#staffProductPost) | **POST** /staff/product | 
[**staffProductPut**](ProductsApi.md#staffProductPut) | **PUT** /staff/product | 
[**staffProductsGet**](ProductsApi.md#staffProductsGet) | **GET** /staff/products | 


<a name="staffProductIdDelete"></a>
# **staffProductIdDelete**
> staffProductIdDelete(id)



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
    defaultClient.setBasePath("http://0.0.0.0:8181/api/v1");

    ProductsApi apiInstance = new ProductsApi(defaultClient);
    Long id = 56L; // Long | 
    try {
      apiInstance.staffProductIdDelete(id);
    } catch (ApiException e) {
      System.err.println("Exception when calling ProductsApi#staffProductIdDelete");
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

<a name="staffProductIdGet"></a>
# **staffProductIdGet**
> ProductDto staffProductIdGet(id)



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
    defaultClient.setBasePath("http://0.0.0.0:8181/api/v1");

    ProductsApi apiInstance = new ProductsApi(defaultClient);
    Long id = 56L; // Long | 
    try {
      ProductDto result = apiInstance.staffProductIdGet(id);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling ProductsApi#staffProductIdGet");
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

<a name="staffProductPost"></a>
# **staffProductPost**
> staffProductPost(productDto)



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
    defaultClient.setBasePath("http://0.0.0.0:8181/api/v1");

    ProductsApi apiInstance = new ProductsApi(defaultClient);
    ProductDto productDto = new ProductDto(); // ProductDto | A JSON object containing updated product information
    try {
      apiInstance.staffProductPost(productDto);
    } catch (ApiException e) {
      System.err.println("Exception when calling ProductsApi#staffProductPost");
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

<a name="staffProductPut"></a>
# **staffProductPut**
> staffProductPut(productDto)



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
    defaultClient.setBasePath("http://0.0.0.0:8181/api/v1");

    ProductsApi apiInstance = new ProductsApi(defaultClient);
    ProductDto productDto = new ProductDto(); // ProductDto | A JSON object containing updated product information
    try {
      apiInstance.staffProductPut(productDto);
    } catch (ApiException e) {
      System.err.println("Exception when calling ProductsApi#staffProductPut");
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

<a name="staffProductsGet"></a>
# **staffProductsGet**
> List&lt;ProductDto&gt; staffProductsGet()



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
    defaultClient.setBasePath("http://0.0.0.0:8181/api/v1");

    ProductsApi apiInstance = new ProductsApi(defaultClient);
    try {
      List<ProductDto> result = apiInstance.staffProductsGet();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling ProductsApi#staffProductsGet");
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

