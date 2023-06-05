# ExtrasApi

All URIs are relative to *http://0.0.0.0:9000/api/v1*

Method | HTTP request | Description
------------- | ------------- | -------------
[**merchantSettingsExtraIdDelete**](ExtrasApi.md#merchantSettingsExtraIdDelete) | **DELETE** /merchant/settings/extra/{id} | 
[**merchantSettingsExtraIdGet**](ExtrasApi.md#merchantSettingsExtraIdGet) | **GET** /merchant/settings/extra/{id} | 
[**merchantSettingsExtraPost**](ExtrasApi.md#merchantSettingsExtraPost) | **POST** /merchant/settings/extra | 
[**merchantSettingsExtraPut**](ExtrasApi.md#merchantSettingsExtraPut) | **PUT** /merchant/settings/extra | 
[**merchantSettingsExtrasGet**](ExtrasApi.md#merchantSettingsExtrasGet) | **GET** /merchant/settings/extras | 


<a name="merchantSettingsExtraIdDelete"></a>
# **merchantSettingsExtraIdDelete**
> merchantSettingsExtraIdDelete(id)



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
    defaultClient.setBasePath("http://0.0.0.0:9000/api/v1");

    ExtrasApi apiInstance = new ExtrasApi(defaultClient);
    Long id = 56L; // Long | 
    try {
      apiInstance.merchantSettingsExtraIdDelete(id);
    } catch (ApiException e) {
      System.err.println("Exception when calling ExtrasApi#merchantSettingsExtraIdDelete");
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

<a name="merchantSettingsExtraIdGet"></a>
# **merchantSettingsExtraIdGet**
> ExtraDto merchantSettingsExtraIdGet(id)



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
    defaultClient.setBasePath("http://0.0.0.0:9000/api/v1");

    ExtrasApi apiInstance = new ExtrasApi(defaultClient);
    Long id = 56L; // Long | 
    try {
      ExtraDto result = apiInstance.merchantSettingsExtraIdGet(id);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling ExtrasApi#merchantSettingsExtraIdGet");
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

<a name="merchantSettingsExtraPost"></a>
# **merchantSettingsExtraPost**
> merchantSettingsExtraPost(extraDto)



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
    defaultClient.setBasePath("http://0.0.0.0:9000/api/v1");

    ExtrasApi apiInstance = new ExtrasApi(defaultClient);
    ExtraDto extraDto = new ExtraDto(); // ExtraDto | A JSON object containing updated extra information
    try {
      apiInstance.merchantSettingsExtraPost(extraDto);
    } catch (ApiException e) {
      System.err.println("Exception when calling ExtrasApi#merchantSettingsExtraPost");
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

<a name="merchantSettingsExtraPut"></a>
# **merchantSettingsExtraPut**
> merchantSettingsExtraPut(extraDto)



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
    defaultClient.setBasePath("http://0.0.0.0:9000/api/v1");

    ExtrasApi apiInstance = new ExtrasApi(defaultClient);
    ExtraDto extraDto = new ExtraDto(); // ExtraDto | A JSON object containing updated extra information
    try {
      apiInstance.merchantSettingsExtraPut(extraDto);
    } catch (ApiException e) {
      System.err.println("Exception when calling ExtrasApi#merchantSettingsExtraPut");
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

<a name="merchantSettingsExtrasGet"></a>
# **merchantSettingsExtrasGet**
> List&lt;ExtraDto&gt; merchantSettingsExtrasGet()



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
    defaultClient.setBasePath("http://0.0.0.0:9000/api/v1");

    ExtrasApi apiInstance = new ExtrasApi(defaultClient);
    try {
      List<ExtraDto> result = apiInstance.merchantSettingsExtrasGet();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling ExtrasApi#merchantSettingsExtrasGet");
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

