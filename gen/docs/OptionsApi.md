# OptionsApi

All URIs are relative to *http://0.0.0.0:8181/api/v1*

Method | HTTP request | Description
------------- | ------------- | -------------
[**staffOptionIdDelete**](OptionsApi.md#staffOptionIdDelete) | **DELETE** /staff/option/{id} | 
[**staffOptionIdGet**](OptionsApi.md#staffOptionIdGet) | **GET** /staff/option/{id} | 
[**staffOptionPost**](OptionsApi.md#staffOptionPost) | **POST** /staff/option | 
[**staffOptionPut**](OptionsApi.md#staffOptionPut) | **PUT** /staff/option | 
[**staffOptionsGet**](OptionsApi.md#staffOptionsGet) | **GET** /staff/options | 


<a name="staffOptionIdDelete"></a>
# **staffOptionIdDelete**
> staffOptionIdDelete(id)



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
      apiInstance.staffOptionIdDelete(id);
    } catch (ApiException e) {
      System.err.println("Exception when calling OptionsApi#staffOptionIdDelete");
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

<a name="staffOptionIdGet"></a>
# **staffOptionIdGet**
> OptionDto staffOptionIdGet(id)



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
      OptionDto result = apiInstance.staffOptionIdGet(id);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling OptionsApi#staffOptionIdGet");
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

<a name="staffOptionPost"></a>
# **staffOptionPost**
> staffOptionPost(optionDto)



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
      apiInstance.staffOptionPost(optionDto);
    } catch (ApiException e) {
      System.err.println("Exception when calling OptionsApi#staffOptionPost");
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

<a name="staffOptionPut"></a>
# **staffOptionPut**
> staffOptionPut(optionDto)



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
      apiInstance.staffOptionPut(optionDto);
    } catch (ApiException e) {
      System.err.println("Exception when calling OptionsApi#staffOptionPut");
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

<a name="staffOptionsGet"></a>
# **staffOptionsGet**
> List&lt;OptionDto&gt; staffOptionsGet()



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
      List<OptionDto> result = apiInstance.staffOptionsGet();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling OptionsApi#staffOptionsGet");
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

