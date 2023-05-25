# BranchesApi

All URIs are relative to *http://0.0.0.0:8181/api/v1*

Method | HTTP request | Description
------------- | ------------- | -------------
[**staffBranchIdDelete**](BranchesApi.md#staffBranchIdDelete) | **DELETE** /staff/branch/{id} | 
[**staffBranchIdGet**](BranchesApi.md#staffBranchIdGet) | **GET** /staff/branch/{id} | 
[**staffBranchPost**](BranchesApi.md#staffBranchPost) | **POST** /staff/branch | 
[**staffBranchPut**](BranchesApi.md#staffBranchPut) | **PUT** /staff/branch | 
[**staffBranchesGet**](BranchesApi.md#staffBranchesGet) | **GET** /staff/branches | 


<a name="staffBranchIdDelete"></a>
# **staffBranchIdDelete**
> staffBranchIdDelete(id)



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
    defaultClient.setBasePath("http://0.0.0.0:8181/api/v1");

    BranchesApi apiInstance = new BranchesApi(defaultClient);
    Long id = 56L; // Long | 
    try {
      apiInstance.staffBranchIdDelete(id);
    } catch (ApiException e) {
      System.err.println("Exception when calling BranchesApi#staffBranchIdDelete");
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

<a name="staffBranchIdGet"></a>
# **staffBranchIdGet**
> BranchDto staffBranchIdGet(id)



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
    defaultClient.setBasePath("http://0.0.0.0:8181/api/v1");

    BranchesApi apiInstance = new BranchesApi(defaultClient);
    Long id = 56L; // Long | 
    try {
      BranchDto result = apiInstance.staffBranchIdGet(id);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling BranchesApi#staffBranchIdGet");
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

<a name="staffBranchPost"></a>
# **staffBranchPost**
> staffBranchPost(branchDto)



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
    defaultClient.setBasePath("http://0.0.0.0:8181/api/v1");

    BranchesApi apiInstance = new BranchesApi(defaultClient);
    BranchDto branchDto = new BranchDto(); // BranchDto | A JSON object containing updated branch information
    try {
      apiInstance.staffBranchPost(branchDto);
    } catch (ApiException e) {
      System.err.println("Exception when calling BranchesApi#staffBranchPost");
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

<a name="staffBranchPut"></a>
# **staffBranchPut**
> staffBranchPut(branchDto)



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
    defaultClient.setBasePath("http://0.0.0.0:8181/api/v1");

    BranchesApi apiInstance = new BranchesApi(defaultClient);
    BranchDto branchDto = new BranchDto(); // BranchDto | A JSON object containing updated branch information
    try {
      apiInstance.staffBranchPut(branchDto);
    } catch (ApiException e) {
      System.err.println("Exception when calling BranchesApi#staffBranchPut");
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

<a name="staffBranchesGet"></a>
# **staffBranchesGet**
> List&lt;BranchDto&gt; staffBranchesGet()



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
    defaultClient.setBasePath("http://0.0.0.0:8181/api/v1");

    BranchesApi apiInstance = new BranchesApi(defaultClient);
    try {
      List<BranchDto> result = apiInstance.staffBranchesGet();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling BranchesApi#staffBranchesGet");
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

