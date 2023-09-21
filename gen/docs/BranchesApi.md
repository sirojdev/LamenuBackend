# BranchesApi

All URIs are relative to *http://0.0.0.0:9000/api/v1*

Method | HTTP request | Description
------------- | ------------- | -------------
[**merchantSettingsBranchIdDelete**](BranchesApi.md#merchantSettingsBranchIdDelete) | **DELETE** /merchant/settings/branch/{id} | 
[**merchantSettingsBranchIdGet**](BranchesApi.md#merchantSettingsBranchIdGet) | **GET** /merchant/settings/branch/{id} | 
[**merchantSettingsBranchPost**](BranchesApi.md#merchantSettingsBranchPost) | **POST** /merchant/settings/branch | 
[**merchantSettingsBranchPut**](BranchesApi.md#merchantSettingsBranchPut) | **PUT** /merchant/settings/branch | 
[**merchantSettingsBranchesGet**](BranchesApi.md#merchantSettingsBranchesGet) | **GET** /merchant/settings/branches | 


<a name="merchantSettingsBranchIdDelete"></a>
# **merchantSettingsBranchIdDelete**
> merchantSettingsBranchIdDelete(id)



Deletes a branch by its ID

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.BranchesApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://0.0.0.0:9000/api/v1");

    BranchesApi apiInstance = new BranchesApi(defaultClient);
    Long id = 56L; // Long | 
    try {
      apiInstance.merchantSettingsBranchIdDelete(id);
    } catch (ApiException e) {
      System.err.println("Exception when calling BranchesApi#merchantSettingsBranchIdDelete");
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

<a name="merchantSettingsBranchIdGet"></a>
# **merchantSettingsBranchIdGet**
> BranchDto merchantSettingsBranchIdGet(id)



Returns a branch by its ID

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.BranchesApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://0.0.0.0:9000/api/v1");

    BranchesApi apiInstance = new BranchesApi(defaultClient);
    Long id = 56L; // Long | 
    try {
      BranchDto result = apiInstance.merchantSettingsBranchIdGet(id);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling BranchesApi#merchantSettingsBranchIdGet");
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

[**BranchDto**](BranchDto.md)

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

<a name="merchantSettingsBranchPost"></a>
# **merchantSettingsBranchPost**
> merchantSettingsBranchPost(branchDto)



Adds a new branch

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.BranchesApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://0.0.0.0:9000/api/v1");

    BranchesApi apiInstance = new BranchesApi(defaultClient);
    BranchDto branchDto = new BranchDto(); // BranchDto | A JSON object containing updated branch information
    try {
      apiInstance.merchantSettingsBranchPost(branchDto);
    } catch (ApiException e) {
      System.err.println("Exception when calling BranchesApi#merchantSettingsBranchPost");
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
 **branchDto** | [**BranchDto**](BranchDto.md)| A JSON object containing updated branch information |

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

<a name="merchantSettingsBranchPut"></a>
# **merchantSettingsBranchPut**
> merchantSettingsBranchPut(branchDto)



Updates an existing branch

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.BranchesApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://0.0.0.0:9000/api/v1");

    BranchesApi apiInstance = new BranchesApi(defaultClient);
    BranchDto branchDto = new BranchDto(); // BranchDto | A JSON object containing updated branch information
    try {
      apiInstance.merchantSettingsBranchPut(branchDto);
    } catch (ApiException e) {
      System.err.println("Exception when calling BranchesApi#merchantSettingsBranchPut");
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
 **branchDto** | [**BranchDto**](BranchDto.md)| A JSON object containing updated branch information |

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

<a name="merchantSettingsBranchesGet"></a>
# **merchantSettingsBranchesGet**
> List&lt;BranchDto&gt; merchantSettingsBranchesGet()



Returns a list of all branches

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.BranchesApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://0.0.0.0:9000/api/v1");

    BranchesApi apiInstance = new BranchesApi(defaultClient);
    try {
      List<BranchDto> result = apiInstance.merchantSettingsBranchesGet();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling BranchesApi#merchantSettingsBranchesGet");
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

[**List&lt;BranchDto&gt;**](BranchDto.md)

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

