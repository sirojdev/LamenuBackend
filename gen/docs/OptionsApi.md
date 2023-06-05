# OptionsApi

All URIs are relative to *http://0.0.0.0:9000/api/v1*

Method | HTTP request | Description
------------- | ------------- | -------------
[**merchantSettingsOptionIdDelete**](OptionsApi.md#merchantSettingsOptionIdDelete) | **DELETE** /merchant/settings/option/{id} | 
[**merchantSettingsOptionIdGet**](OptionsApi.md#merchantSettingsOptionIdGet) | **GET** /merchant/settings/option/{id} | 
[**merchantSettingsOptionPost**](OptionsApi.md#merchantSettingsOptionPost) | **POST** /merchant/settings/option | 
[**merchantSettingsOptionPut**](OptionsApi.md#merchantSettingsOptionPut) | **PUT** /merchant/settings/option | 
[**merchantSettingsOptionsGet**](OptionsApi.md#merchantSettingsOptionsGet) | **GET** /merchant/settings/options | 


<a name="merchantSettingsOptionIdDelete"></a>
# **merchantSettingsOptionIdDelete**
> merchantSettingsOptionIdDelete(id)



Deletes a option by its ID

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.OptionsApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://0.0.0.0:9000/api/v1");

    OptionsApi apiInstance = new OptionsApi(defaultClient);
    Long id = 56L; // Long | 
    try {
      apiInstance.merchantSettingsOptionIdDelete(id);
    } catch (ApiException e) {
      System.err.println("Exception when calling OptionsApi#merchantSettingsOptionIdDelete");
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

<a name="merchantSettingsOptionIdGet"></a>
# **merchantSettingsOptionIdGet**
> OptionDto merchantSettingsOptionIdGet(id)



Returns a option by its ID

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.OptionsApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://0.0.0.0:9000/api/v1");

    OptionsApi apiInstance = new OptionsApi(defaultClient);
    Long id = 56L; // Long | 
    try {
      OptionDto result = apiInstance.merchantSettingsOptionIdGet(id);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling OptionsApi#merchantSettingsOptionIdGet");
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

[**OptionDto**](OptionDto.md)

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

<a name="merchantSettingsOptionPost"></a>
# **merchantSettingsOptionPost**
> merchantSettingsOptionPost(optionDto)



Adds a new option

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.OptionsApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://0.0.0.0:9000/api/v1");

    OptionsApi apiInstance = new OptionsApi(defaultClient);
    OptionDto optionDto = new OptionDto(); // OptionDto | A JSON object containing updated option information
    try {
      apiInstance.merchantSettingsOptionPost(optionDto);
    } catch (ApiException e) {
      System.err.println("Exception when calling OptionsApi#merchantSettingsOptionPost");
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
 **optionDto** | [**OptionDto**](OptionDto.md)| A JSON object containing updated option information |

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

<a name="merchantSettingsOptionPut"></a>
# **merchantSettingsOptionPut**
> merchantSettingsOptionPut(optionDto)



Adds a new option

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.OptionsApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://0.0.0.0:9000/api/v1");

    OptionsApi apiInstance = new OptionsApi(defaultClient);
    OptionDto optionDto = new OptionDto(); // OptionDto | A JSON object containing updated option information
    try {
      apiInstance.merchantSettingsOptionPut(optionDto);
    } catch (ApiException e) {
      System.err.println("Exception when calling OptionsApi#merchantSettingsOptionPut");
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
 **optionDto** | [**OptionDto**](OptionDto.md)| A JSON object containing updated option information |

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

<a name="merchantSettingsOptionsGet"></a>
# **merchantSettingsOptionsGet**
> List&lt;OptionDto&gt; merchantSettingsOptionsGet()



Returns a list of all option

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.OptionsApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://0.0.0.0:9000/api/v1");

    OptionsApi apiInstance = new OptionsApi(defaultClient);
    try {
      List<OptionDto> result = apiInstance.merchantSettingsOptionsGet();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling OptionsApi#merchantSettingsOptionsGet");
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

[**List&lt;OptionDto&gt;**](OptionDto.md)

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

