# CategoriesApi

All URIs are relative to *http://0.0.0.0:9000/api/v1*

Method | HTTP request | Description
------------- | ------------- | -------------
[**merchantSettingsCategoriesGet**](CategoriesApi.md#merchantSettingsCategoriesGet) | **GET** /merchant/settings/categories | 
[**merchantSettingsCategoryIdDelete**](CategoriesApi.md#merchantSettingsCategoryIdDelete) | **DELETE** /merchant/settings/category/{id} | 
[**merchantSettingsCategoryIdGet**](CategoriesApi.md#merchantSettingsCategoryIdGet) | **GET** /merchant/settings/category/{id} | 
[**merchantSettingsCategoryPost**](CategoriesApi.md#merchantSettingsCategoryPost) | **POST** /merchant/settings/category | 
[**merchantSettingsCategoryPut**](CategoriesApi.md#merchantSettingsCategoryPut) | **PUT** /merchant/settings/category | 


<a name="merchantSettingsCategoriesGet"></a>
# **merchantSettingsCategoriesGet**
> List&lt;CategoryDto&gt; merchantSettingsCategoriesGet()



Returns a list of all categories

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.CategoriesApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://0.0.0.0:9000/api/v1");

    CategoriesApi apiInstance = new CategoriesApi(defaultClient);
    try {
      List<CategoryDto> result = apiInstance.merchantSettingsCategoriesGet();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling CategoriesApi#merchantSettingsCategoriesGet");
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

[**List&lt;CategoryDto&gt;**](CategoryDto.md)

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

<a name="merchantSettingsCategoryIdDelete"></a>
# **merchantSettingsCategoryIdDelete**
> merchantSettingsCategoryIdDelete(id)



Deletes a category by its ID

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.CategoriesApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://0.0.0.0:9000/api/v1");

    CategoriesApi apiInstance = new CategoriesApi(defaultClient);
    Long id = 56L; // Long | 
    try {
      apiInstance.merchantSettingsCategoryIdDelete(id);
    } catch (ApiException e) {
      System.err.println("Exception when calling CategoriesApi#merchantSettingsCategoryIdDelete");
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

<a name="merchantSettingsCategoryIdGet"></a>
# **merchantSettingsCategoryIdGet**
> CategoryDto merchantSettingsCategoryIdGet(id)



Returns a category by its ID

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.CategoriesApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://0.0.0.0:9000/api/v1");

    CategoriesApi apiInstance = new CategoriesApi(defaultClient);
    Long id = 56L; // Long | 
    try {
      CategoryDto result = apiInstance.merchantSettingsCategoryIdGet(id);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling CategoriesApi#merchantSettingsCategoryIdGet");
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

[**CategoryDto**](CategoryDto.md)

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

<a name="merchantSettingsCategoryPost"></a>
# **merchantSettingsCategoryPost**
> merchantSettingsCategoryPost(categoryDto)



Adds a new category

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.CategoriesApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://0.0.0.0:9000/api/v1");

    CategoriesApi apiInstance = new CategoriesApi(defaultClient);
    CategoryDto categoryDto = new CategoryDto(); // CategoryDto | A JSON object containing updated category information
    try {
      apiInstance.merchantSettingsCategoryPost(categoryDto);
    } catch (ApiException e) {
      System.err.println("Exception when calling CategoriesApi#merchantSettingsCategoryPost");
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
 **categoryDto** | [**CategoryDto**](CategoryDto.md)| A JSON object containing updated category information |

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

<a name="merchantSettingsCategoryPut"></a>
# **merchantSettingsCategoryPut**
> merchantSettingsCategoryPut(categoryDto)



Adds a new category

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.CategoriesApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://0.0.0.0:9000/api/v1");

    CategoriesApi apiInstance = new CategoriesApi(defaultClient);
    CategoryDto categoryDto = new CategoryDto(); // CategoryDto | A JSON object containing updated category information
    try {
      apiInstance.merchantSettingsCategoryPut(categoryDto);
    } catch (ApiException e) {
      System.err.println("Exception when calling CategoriesApi#merchantSettingsCategoryPut");
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
 **categoryDto** | [**CategoryDto**](CategoryDto.md)| A JSON object containing updated category information |

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

