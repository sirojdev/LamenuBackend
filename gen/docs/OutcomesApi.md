# OutcomesApi

All URIs are relative to *http://0.0.0.0:9000/api/v1*

Method | HTTP request | Description
------------- | ------------- | -------------
[**merchantFinanceOutcomeIdDelete**](OutcomesApi.md#merchantFinanceOutcomeIdDelete) | **DELETE** /merchant/finance/outcome/{id} | 
[**merchantFinanceOutcomeIdGet**](OutcomesApi.md#merchantFinanceOutcomeIdGet) | **GET** /merchant/finance/outcome/{id} | 
[**merchantFinanceOutcomePost**](OutcomesApi.md#merchantFinanceOutcomePost) | **POST** /merchant/finance/outcome | 
[**merchantFinanceOutcomePut**](OutcomesApi.md#merchantFinanceOutcomePut) | **PUT** /merchant/finance/outcome | 
[**merchantFinanceOutcomesGet**](OutcomesApi.md#merchantFinanceOutcomesGet) | **GET** /merchant/finance/outcomes | 


<a name="merchantFinanceOutcomeIdDelete"></a>
# **merchantFinanceOutcomeIdDelete**
> merchantFinanceOutcomeIdDelete(id)



Deletes a outcome by its ID

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.OutcomesApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://0.0.0.0:9000/api/v1");

    OutcomesApi apiInstance = new OutcomesApi(defaultClient);
    Long id = 56L; // Long | 
    try {
      apiInstance.merchantFinanceOutcomeIdDelete(id);
    } catch (ApiException e) {
      System.err.println("Exception when calling OutcomesApi#merchantFinanceOutcomeIdDelete");
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

<a name="merchantFinanceOutcomeIdGet"></a>
# **merchantFinanceOutcomeIdGet**
> OutcomeDto merchantFinanceOutcomeIdGet(id)



Returns a outcome by its ID

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.OutcomesApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://0.0.0.0:9000/api/v1");

    OutcomesApi apiInstance = new OutcomesApi(defaultClient);
    Long id = 56L; // Long | 
    try {
      OutcomeDto result = apiInstance.merchantFinanceOutcomeIdGet(id);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling OutcomesApi#merchantFinanceOutcomeIdGet");
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

[**OutcomeDto**](OutcomeDto.md)

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

<a name="merchantFinanceOutcomePost"></a>
# **merchantFinanceOutcomePost**
> merchantFinanceOutcomePost(outcomeDto)



Adds a new outcome

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.OutcomesApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://0.0.0.0:9000/api/v1");

    OutcomesApi apiInstance = new OutcomesApi(defaultClient);
    OutcomeDto outcomeDto = new OutcomeDto(); // OutcomeDto | A JSON object containing updated outcome information
    try {
      apiInstance.merchantFinanceOutcomePost(outcomeDto);
    } catch (ApiException e) {
      System.err.println("Exception when calling OutcomesApi#merchantFinanceOutcomePost");
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
 **outcomeDto** | [**OutcomeDto**](OutcomeDto.md)| A JSON object containing updated outcome information |

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

<a name="merchantFinanceOutcomePut"></a>
# **merchantFinanceOutcomePut**
> merchantFinanceOutcomePut(outcomeDto)



Update the outcomes

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.OutcomesApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://0.0.0.0:9000/api/v1");

    OutcomesApi apiInstance = new OutcomesApi(defaultClient);
    OutcomeDto outcomeDto = new OutcomeDto(); // OutcomeDto | A JSON object containing updated product information
    try {
      apiInstance.merchantFinanceOutcomePut(outcomeDto);
    } catch (ApiException e) {
      System.err.println("Exception when calling OutcomesApi#merchantFinanceOutcomePut");
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
 **outcomeDto** | [**OutcomeDto**](OutcomeDto.md)| A JSON object containing updated product information |

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

<a name="merchantFinanceOutcomesGet"></a>
# **merchantFinanceOutcomesGet**
> List&lt;OutcomeDto&gt; merchantFinanceOutcomesGet()



Returns a list of all Outcomes

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.OutcomesApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://0.0.0.0:9000/api/v1");

    OutcomesApi apiInstance = new OutcomesApi(defaultClient);
    try {
      List<OutcomeDto> result = apiInstance.merchantFinanceOutcomesGet();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling OutcomesApi#merchantFinanceOutcomesGet");
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

[**List&lt;OutcomeDto&gt;**](OutcomeDto.md)

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

