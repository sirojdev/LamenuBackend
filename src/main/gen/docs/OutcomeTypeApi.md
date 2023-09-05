# OutcomeTypeApi

All URIs are relative to *http://127.0.0.1:9000/v1*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**merchantSettingsOutcomeTypeIdDelete**](OutcomeTypeApi.md#merchantSettingsOutcomeTypeIdDelete) | **DELETE** /merchant/settings/outcome_type/{id} |  |
| [**merchantSettingsOutcomeTypeIdGet**](OutcomeTypeApi.md#merchantSettingsOutcomeTypeIdGet) | **GET** /merchant/settings/outcome_type/{id} |  |
| [**merchantSettingsOutcomeTypePost**](OutcomeTypeApi.md#merchantSettingsOutcomeTypePost) | **POST** /merchant/settings/outcome_type |  |
| [**merchantSettingsOutcomeTypePut**](OutcomeTypeApi.md#merchantSettingsOutcomeTypePut) | **PUT** /merchant/settings/outcome_type |  |
| [**merchantSettingsOutcomeTypesGet**](OutcomeTypeApi.md#merchantSettingsOutcomeTypesGet) | **GET** /merchant/settings/outcome_types |  |


<a name="merchantSettingsOutcomeTypeIdDelete"></a>
# **merchantSettingsOutcomeTypeIdDelete**
> merchantSettingsOutcomeTypeIdDelete(id)



Deletes a outcome type by its ID

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.OutcomeTypeApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://127.0.0.1:9000/v1");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    OutcomeTypeApi apiInstance = new OutcomeTypeApi(defaultClient);
    Long id = 56L; // Long | 
    try {
      apiInstance.merchantSettingsOutcomeTypeIdDelete(id);
    } catch (ApiException e) {
      System.err.println("Exception when calling OutcomeTypeApi#merchantSettingsOutcomeTypeIdDelete");
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

<a name="merchantSettingsOutcomeTypeIdGet"></a>
# **merchantSettingsOutcomeTypeIdGet**
> OutcomeTypeDto merchantSettingsOutcomeTypeIdGet(id)



Returns a outcome type by its ID

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.OutcomeTypeApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://127.0.0.1:9000/v1");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    OutcomeTypeApi apiInstance = new OutcomeTypeApi(defaultClient);
    Long id = 56L; // Long | 
    try {
      OutcomeTypeDto result = apiInstance.merchantSettingsOutcomeTypeIdGet(id);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling OutcomeTypeApi#merchantSettingsOutcomeTypeIdGet");
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

[**OutcomeTypeDto**](OutcomeTypeDto.md)

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

<a name="merchantSettingsOutcomeTypePost"></a>
# **merchantSettingsOutcomeTypePost**
> merchantSettingsOutcomeTypePost(outcomeTypeDto)



Adds a new outcome

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.OutcomeTypeApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://127.0.0.1:9000/v1");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    OutcomeTypeApi apiInstance = new OutcomeTypeApi(defaultClient);
    OutcomeTypeDto outcomeTypeDto = new OutcomeTypeDto(); // OutcomeTypeDto | A JSON object containing updated outcome type's information
    try {
      apiInstance.merchantSettingsOutcomeTypePost(outcomeTypeDto);
    } catch (ApiException e) {
      System.err.println("Exception when calling OutcomeTypeApi#merchantSettingsOutcomeTypePost");
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
| **outcomeTypeDto** | [**OutcomeTypeDto**](OutcomeTypeDto.md)| A JSON object containing updated outcome type&#39;s information | |

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

<a name="merchantSettingsOutcomeTypePut"></a>
# **merchantSettingsOutcomeTypePut**
> merchantSettingsOutcomeTypePut(outcomeTypeDto)



Adds a new outcome type

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.OutcomeTypeApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://127.0.0.1:9000/v1");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    OutcomeTypeApi apiInstance = new OutcomeTypeApi(defaultClient);
    OutcomeTypeDto outcomeTypeDto = new OutcomeTypeDto(); // OutcomeTypeDto | A JSON object containing updated outcome type's information
    try {
      apiInstance.merchantSettingsOutcomeTypePut(outcomeTypeDto);
    } catch (ApiException e) {
      System.err.println("Exception when calling OutcomeTypeApi#merchantSettingsOutcomeTypePut");
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
| **outcomeTypeDto** | [**OutcomeTypeDto**](OutcomeTypeDto.md)| A JSON object containing updated outcome type&#39;s information | |

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

<a name="merchantSettingsOutcomeTypesGet"></a>
# **merchantSettingsOutcomeTypesGet**
> List&lt;OutcomeTypeDto&gt; merchantSettingsOutcomeTypesGet()



Returns a list of all outcomr types

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.OutcomeTypeApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://127.0.0.1:9000/v1");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    OutcomeTypeApi apiInstance = new OutcomeTypeApi(defaultClient);
    try {
      List<OutcomeTypeDto> result = apiInstance.merchantSettingsOutcomeTypesGet();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling OutcomeTypeApi#merchantSettingsOutcomeTypesGet");
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

[**List&lt;OutcomeTypeDto&gt;**](OutcomeTypeDto.md)

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

