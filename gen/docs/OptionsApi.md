# OptionsApi

All URIs are relative to *http://0.0.0.0:8181/api/v1*

Method | HTTP request | Description
------------- | ------------- | -------------
[**optionIdDelete**](OptionsApi.md#optionIdDelete) | **DELETE** /option/{id} | 
[**optionIdGet**](OptionsApi.md#optionIdGet) | **GET** /option/{id} | 
[**optionPost**](OptionsApi.md#optionPost) | **POST** /option | 
[**optionPut**](OptionsApi.md#optionPut) | **PUT** /option | 
[**optionsGet**](OptionsApi.md#optionsGet) | **GET** /options | 


<a name="optionIdDelete"></a>
# **optionIdDelete**
> optionIdDelete(id)



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
    defaultClient.setBasePath("http://0.0.0.0:8181/api/v1");

    OptionsApi apiInstance = new OptionsApi(defaultClient);
    Long id = 56L; // Long | 
    try {
      apiInstance.optionIdDelete(id);
    } catch (ApiException e) {
      System.err.println("Exception when calling OptionsApi#optionIdDelete");
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

<a name="optionIdGet"></a>
# **optionIdGet**
> OptionDto optionIdGet(id)



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
    defaultClient.setBasePath("http://0.0.0.0:8181/api/v1");

    OptionsApi apiInstance = new OptionsApi(defaultClient);
    Long id = 56L; // Long | 
    try {
      OptionDto result = apiInstance.optionIdGet(id);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling OptionsApi#optionIdGet");
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

<a name="optionPost"></a>
# **optionPost**
> optionPost(optionDto)



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
    defaultClient.setBasePath("http://0.0.0.0:8181/api/v1");

    OptionsApi apiInstance = new OptionsApi(defaultClient);
    OptionDto optionDto = new OptionDto(); // OptionDto | A JSON object containing updated option information
    try {
      apiInstance.optionPost(optionDto);
    } catch (ApiException e) {
      System.err.println("Exception when calling OptionsApi#optionPost");
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

<a name="optionPut"></a>
# **optionPut**
> optionPut(optionDto)



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
    defaultClient.setBasePath("http://0.0.0.0:8181/api/v1");

    OptionsApi apiInstance = new OptionsApi(defaultClient);
    OptionDto optionDto = new OptionDto(); // OptionDto | A JSON object containing updated option information
    try {
      apiInstance.optionPut(optionDto);
    } catch (ApiException e) {
      System.err.println("Exception when calling OptionsApi#optionPut");
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

<a name="optionsGet"></a>
# **optionsGet**
> List&lt;OptionDto&gt; optionsGet()



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
    defaultClient.setBasePath("http://0.0.0.0:8181/api/v1");

    OptionsApi apiInstance = new OptionsApi(defaultClient);
    try {
      List<OptionDto> result = apiInstance.optionsGet();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling OptionsApi#optionsGet");
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

