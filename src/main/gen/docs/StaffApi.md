# StaffApi

All URIs are relative to *http://127.0.0.1:9000/v1*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**merchantSettingsStaffGet**](StaffApi.md#merchantSettingsStaffGet) | **GET** /merchant/settings/staff |  |
| [**merchantSettingsStaffIdDelete**](StaffApi.md#merchantSettingsStaffIdDelete) | **DELETE** /merchant/settings/staff/{id} |  |
| [**merchantSettingsStaffIdGet**](StaffApi.md#merchantSettingsStaffIdGet) | **GET** /merchant/settings/staff/{id} |  |
| [**merchantSettingsStaffPost**](StaffApi.md#merchantSettingsStaffPost) | **POST** /merchant/settings/staff |  |
| [**merchantSettingsStaffPut**](StaffApi.md#merchantSettingsStaffPut) | **PUT** /merchant/settings/staff |  |


<a name="merchantSettingsStaffGet"></a>
# **merchantSettingsStaffGet**
> List&lt;StaffDto&gt; merchantSettingsStaffGet()



Returns a list of all staff

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.StaffApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://127.0.0.1:9000/v1");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    StaffApi apiInstance = new StaffApi(defaultClient);
    try {
      List<StaffDto> result = apiInstance.merchantSettingsStaffGet();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling StaffApi#merchantSettingsStaffGet");
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

[**List&lt;StaffDto&gt;**](StaffDto.md)

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

<a name="merchantSettingsStaffIdDelete"></a>
# **merchantSettingsStaffIdDelete**
> merchantSettingsStaffIdDelete(id)



Deletes a staff by its ID

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.StaffApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://127.0.0.1:9000/v1");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    StaffApi apiInstance = new StaffApi(defaultClient);
    Long id = 56L; // Long | 
    try {
      apiInstance.merchantSettingsStaffIdDelete(id);
    } catch (ApiException e) {
      System.err.println("Exception when calling StaffApi#merchantSettingsStaffIdDelete");
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

<a name="merchantSettingsStaffIdGet"></a>
# **merchantSettingsStaffIdGet**
> StaffDto merchantSettingsStaffIdGet(id)



Returns a staff by its ID

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.StaffApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://127.0.0.1:9000/v1");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    StaffApi apiInstance = new StaffApi(defaultClient);
    Long id = 56L; // Long | 
    try {
      StaffDto result = apiInstance.merchantSettingsStaffIdGet(id);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling StaffApi#merchantSettingsStaffIdGet");
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

[**StaffDto**](StaffDto.md)

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

<a name="merchantSettingsStaffPost"></a>
# **merchantSettingsStaffPost**
> merchantSettingsStaffPost(staffDto)



Adds a new staff

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.StaffApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://127.0.0.1:9000/v1");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    StaffApi apiInstance = new StaffApi(defaultClient);
    StaffDto staffDto = new StaffDto(); // StaffDto | A JSON object containing updated outcome staff information
    try {
      apiInstance.merchantSettingsStaffPost(staffDto);
    } catch (ApiException e) {
      System.err.println("Exception when calling StaffApi#merchantSettingsStaffPost");
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
| **staffDto** | [**StaffDto**](StaffDto.md)| A JSON object containing updated outcome staff information | |

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

<a name="merchantSettingsStaffPut"></a>
# **merchantSettingsStaffPut**
> merchantSettingsStaffPut(staffDto)



Adds a new staff

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.StaffApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://127.0.0.1:9000/v1");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    StaffApi apiInstance = new StaffApi(defaultClient);
    StaffDto staffDto = new StaffDto(); // StaffDto | A JSON object containing updated staff's information
    try {
      apiInstance.merchantSettingsStaffPut(staffDto);
    } catch (ApiException e) {
      System.err.println("Exception when calling StaffApi#merchantSettingsStaffPut");
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
| **staffDto** | [**StaffDto**](StaffDto.md)| A JSON object containing updated staff&#39;s information | |

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

