# CategoryApi

All URIs are relative to *http://127.0.0.1:9000/v1*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**merchantCategoryGroupGet**](CategoryApi.md#merchantCategoryGroupGet) | **GET** /merchant/category/group |  |
| [**merchantCategoryGroupIdDelete**](CategoryApi.md#merchantCategoryGroupIdDelete) | **DELETE** /merchant/category/group/{id} |  |
| [**merchantCategoryGroupIdGet**](CategoryApi.md#merchantCategoryGroupIdGet) | **GET** /merchant/category/group/{id} |  |
| [**merchantCategoryGroupPost**](CategoryApi.md#merchantCategoryGroupPost) | **POST** /merchant/category/group |  |
| [**merchantCategoryGroupPut**](CategoryApi.md#merchantCategoryGroupPut) | **PUT** /merchant/category/group |  |


<a name="merchantCategoryGroupGet"></a>
# **merchantCategoryGroupGet**
> List&lt;CategoryGroupDto&gt; merchantCategoryGroupGet()



Returns a list of category-group

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.CategoryApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://127.0.0.1:9000/v1");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    CategoryApi apiInstance = new CategoryApi(defaultClient);
    try {
      List<CategoryGroupDto> result = apiInstance.merchantCategoryGroupGet();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling CategoryApi#merchantCategoryGroupGet");
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

[**List&lt;CategoryGroupDto&gt;**](CategoryGroupDto.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |
| **204** | No Content |  -  |

<a name="merchantCategoryGroupIdDelete"></a>
# **merchantCategoryGroupIdDelete**
> merchantCategoryGroupIdDelete(id)



Deletes a category group by its id

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.CategoryApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://127.0.0.1:9000/v1");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    CategoryApi apiInstance = new CategoryApi(defaultClient);
    Long id = 56L; // Long | 
    try {
      apiInstance.merchantCategoryGroupIdDelete(id);
    } catch (ApiException e) {
      System.err.println("Exception when calling CategoryApi#merchantCategoryGroupIdDelete");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **id** | **Long**|  | |

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
| **200** | OK |  -  |
| **400** | Bad Request |  -  |

<a name="merchantCategoryGroupIdGet"></a>
# **merchantCategoryGroupIdGet**
> CategoryGroupDto merchantCategoryGroupIdGet(id)



Returns a category group by its id

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.CategoryApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://127.0.0.1:9000/v1");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    CategoryApi apiInstance = new CategoryApi(defaultClient);
    Long id = 56L; // Long | 
    try {
      CategoryGroupDto result = apiInstance.merchantCategoryGroupIdGet(id);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling CategoryApi#merchantCategoryGroupIdGet");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **id** | **Long**|  | |

### Return type

[**CategoryGroupDto**](CategoryGroupDto.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |
| **204** | No Content |  -  |
| **400** | Bad Request |  -  |

<a name="merchantCategoryGroupPost"></a>
# **merchantCategoryGroupPost**
> merchantCategoryGroupPost(categoryGroupDto)



Adds a new Category Group

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.CategoryApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://127.0.0.1:9000/v1");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    CategoryApi apiInstance = new CategoryApi(defaultClient);
    CategoryGroupDto categoryGroupDto = new CategoryGroupDto(); // CategoryGroupDto | A JSON object containing category group's information
    try {
      apiInstance.merchantCategoryGroupPost(categoryGroupDto);
    } catch (ApiException e) {
      System.err.println("Exception when calling CategoryApi#merchantCategoryGroupPost");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **categoryGroupDto** | [**CategoryGroupDto**](CategoryGroupDto.md)| A JSON object containing category group&#39;s information | |

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
| **200** | OK |  -  |

<a name="merchantCategoryGroupPut"></a>
# **merchantCategoryGroupPut**
> merchantCategoryGroupPut(categoryGroupDto)



Update a category group&#39;s information

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.CategoryApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://127.0.0.1:9000/v1");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    CategoryApi apiInstance = new CategoryApi(defaultClient);
    CategoryGroupDto categoryGroupDto = new CategoryGroupDto(); // CategoryGroupDto | A JSON object containing updated category group's information
    try {
      apiInstance.merchantCategoryGroupPut(categoryGroupDto);
    } catch (ApiException e) {
      System.err.println("Exception when calling CategoryApi#merchantCategoryGroupPut");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **categoryGroupDto** | [**CategoryGroupDto**](CategoryGroupDto.md)| A JSON object containing updated category group&#39;s information | |

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
| **200** | OK |  -  |

