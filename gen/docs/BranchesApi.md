# BranchesApi

All URIs are relative to *http://0.0.0.0:8181/api/v1*

Method | HTTP request | Description
------------- | ------------- | -------------
[**branchIdDelete**](BranchesApi.md#branchIdDelete) | **DELETE** /branch/{id} | 
[**branchIdGet**](BranchesApi.md#branchIdGet) | **GET** /branch/{id} | 
[**branchPost**](BranchesApi.md#branchPost) | **POST** /branch | 
[**branchPut**](BranchesApi.md#branchPut) | **PUT** /branch | 
[**branchesGet**](BranchesApi.md#branchesGet) | **GET** /branches | 


<a name="branchIdDelete"></a>
# **branchIdDelete**
> branchIdDelete(id)



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
      apiInstance.branchIdDelete(id);
    } catch (ApiException e) {
      System.err.println("Exception when calling BranchesApi#branchIdDelete");
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

<a name="branchIdGet"></a>
# **branchIdGet**
> BranchDto branchIdGet(id)



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
      BranchDto result = apiInstance.branchIdGet(id);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling BranchesApi#branchIdGet");
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

<a name="branchPost"></a>
# **branchPost**
> branchPost(branchDto)



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
      apiInstance.branchPost(branchDto);
    } catch (ApiException e) {
      System.err.println("Exception when calling BranchesApi#branchPost");
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

<a name="branchPut"></a>
# **branchPut**
> branchPut(branchDto)



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
      apiInstance.branchPut(branchDto);
    } catch (ApiException e) {
      System.err.println("Exception when calling BranchesApi#branchPut");
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

<a name="branchesGet"></a>
# **branchesGet**
> List&lt;BranchDto&gt; branchesGet()



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
      List<BranchDto> result = apiInstance.branchesGet();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling BranchesApi#branchesGet");
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

