# LabelsApi

All URIs are relative to *http://0.0.0.0:8181/api/v1*

Method | HTTP request | Description
------------- | ------------- | -------------
[**staffLableIdDelete**](LabelsApi.md#staffLableIdDelete) | **DELETE** /staff/lable/{id} | 
[**staffLableIdGet**](LabelsApi.md#staffLableIdGet) | **GET** /staff/lable/{id} | 
[**staffLablesGet**](LabelsApi.md#staffLablesGet) | **GET** /staff/lables | 
[**staffStaffLablePost**](LabelsApi.md#staffStaffLablePost) | **POST** /staff/staff/lable | 
[**staffStaffLablePut**](LabelsApi.md#staffStaffLablePut) | **PUT** /staff/staff/lable | 


<a name="staffLableIdDelete"></a>
# **staffLableIdDelete**
> staffLableIdDelete(id)



Deletes a lable by its ID

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.LabelsApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://0.0.0.0:8181/api/v1");

    LabelsApi apiInstance = new LabelsApi(defaultClient);
    Long id = 56L; // Long | 
    try {
      apiInstance.staffLableIdDelete(id);
    } catch (ApiException e) {
      System.err.println("Exception when calling LabelsApi#staffLableIdDelete");
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

<a name="staffLableIdGet"></a>
# **staffLableIdGet**
> LabelDto staffLableIdGet(id)



Returns a lable by its ID

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.LabelsApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://0.0.0.0:8181/api/v1");

    LabelsApi apiInstance = new LabelsApi(defaultClient);
    Long id = 56L; // Long | 
    try {
      LabelDto result = apiInstance.staffLableIdGet(id);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling LabelsApi#staffLableIdGet");
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

[**LabelDto**](LabelDto.md)

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

<a name="staffLablesGet"></a>
# **staffLablesGet**
> List&lt;LabelDto&gt; staffLablesGet()



Returns a list of all lables

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.LabelsApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://0.0.0.0:8181/api/v1");

    LabelsApi apiInstance = new LabelsApi(defaultClient);
    try {
      List<LabelDto> result = apiInstance.staffLablesGet();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling LabelsApi#staffLablesGet");
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

[**List&lt;LabelDto&gt;**](LabelDto.md)

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

<a name="staffStaffLablePost"></a>
# **staffStaffLablePost**
> staffStaffLablePost(labelDto)



Adds a new lable

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.LabelsApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://0.0.0.0:8181/api/v1");

    LabelsApi apiInstance = new LabelsApi(defaultClient);
    LabelDto labelDto = new LabelDto(); // LabelDto | A JSON object containing updated lable information
    try {
      apiInstance.staffStaffLablePost(labelDto);
    } catch (ApiException e) {
      System.err.println("Exception when calling LabelsApi#staffStaffLablePost");
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
 **labelDto** | [**LabelDto**](LabelDto.md)| A JSON object containing updated lable information |

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

<a name="staffStaffLablePut"></a>
# **staffStaffLablePut**
> staffStaffLablePut(labelDto)



Adds a new lable

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.LabelsApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://0.0.0.0:8181/api/v1");

    LabelsApi apiInstance = new LabelsApi(defaultClient);
    LabelDto labelDto = new LabelDto(); // LabelDto | A JSON object containing updated lable information
    try {
      apiInstance.staffStaffLablePut(labelDto);
    } catch (ApiException e) {
      System.err.println("Exception when calling LabelsApi#staffStaffLablePut");
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
 **labelDto** | [**LabelDto**](LabelDto.md)| A JSON object containing updated lable information |

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

