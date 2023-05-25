# ExtrasApi

All URIs are relative to *http://0.0.0.0:8181/api/v1*

Method | HTTP request | Description
------------- | ------------- | -------------
[**staffExtraIdDelete**](ExtrasApi.md#staffExtraIdDelete) | **DELETE** /staff/extra/{id} | 
[**staffExtraIdGet**](ExtrasApi.md#staffExtraIdGet) | **GET** /staff/extra/{id} | 
[**staffExtraPost**](ExtrasApi.md#staffExtraPost) | **POST** /staff/extra | 
[**staffExtraPut**](ExtrasApi.md#staffExtraPut) | **PUT** /staff/extra | 
[**staffExtrasGet**](ExtrasApi.md#staffExtrasGet) | **GET** /staff/extras | 


<a name="staffExtraIdDelete"></a>
# **staffExtraIdDelete**
> staffExtraIdDelete(id)



Deletes a extra by its ID

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.ExtrasApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://0.0.0.0:8181/api/v1");

    ExtrasApi apiInstance = new ExtrasApi(defaultClient);
    Long id = 56L; // Long | 
    try {
      apiInstance.staffExtraIdDelete(id);
    } catch (ApiException e) {
      System.err.println("Exception when calling ExtrasApi#staffExtraIdDelete");
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

<a name="staffExtraIdGet"></a>
# **staffExtraIdGet**
> ExtraDto staffExtraIdGet(id)



Returns a extra by its ID

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.ExtrasApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://0.0.0.0:8181/api/v1");

    ExtrasApi apiInstance = new ExtrasApi(defaultClient);
    Long id = 56L; // Long | 
    try {
      ExtraDto result = apiInstance.staffExtraIdGet(id);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling ExtrasApi#staffExtraIdGet");
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

[**ExtraDto**](ExtraDto.md)

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

<a name="staffExtraPost"></a>
# **staffExtraPost**
> staffExtraPost(extraDto)



Adds a new extra

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.ExtrasApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://0.0.0.0:8181/api/v1");

    ExtrasApi apiInstance = new ExtrasApi(defaultClient);
    ExtraDto extraDto = new ExtraDto(); // ExtraDto | A JSON object containing updated extra information
    try {
      apiInstance.staffExtraPost(extraDto);
    } catch (ApiException e) {
      System.err.println("Exception when calling ExtrasApi#staffExtraPost");
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
 **extraDto** | [**ExtraDto**](ExtraDto.md)| A JSON object containing updated extra information |

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

<a name="staffExtraPut"></a>
# **staffExtraPut**
> staffExtraPut(extraDto)



Adds a new extra

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.ExtrasApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://0.0.0.0:8181/api/v1");

    ExtrasApi apiInstance = new ExtrasApi(defaultClient);
    ExtraDto extraDto = new ExtraDto(); // ExtraDto | A JSON object containing updated extra information
    try {
      apiInstance.staffExtraPut(extraDto);
    } catch (ApiException e) {
      System.err.println("Exception when calling ExtrasApi#staffExtraPut");
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
 **extraDto** | [**ExtraDto**](ExtraDto.md)| A JSON object containing updated extra information |

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

<a name="staffExtrasGet"></a>
# **staffExtrasGet**
> List&lt;ExtraDto&gt; staffExtrasGet()



Returns a list of all extras

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.ExtrasApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://0.0.0.0:8181/api/v1");

    ExtrasApi apiInstance = new ExtrasApi(defaultClient);
    try {
      List<ExtraDto> result = apiInstance.staffExtrasGet();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling ExtrasApi#staffExtrasGet");
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

[**List&lt;ExtraDto&gt;**](ExtraDto.md)

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

