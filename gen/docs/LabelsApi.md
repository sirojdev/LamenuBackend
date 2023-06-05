# LabelsApi

All URIs are relative to *http://0.0.0.0:9000/api/v1*

Method | HTTP request | Description
------------- | ------------- | -------------
[**merchantSettingsLabelIdDelete**](LabelsApi.md#merchantSettingsLabelIdDelete) | **DELETE** /merchant/settings/label/{id} | 
[**merchantSettingsLabelIdGet**](LabelsApi.md#merchantSettingsLabelIdGet) | **GET** /merchant/settings/label/{id} | 
[**merchantSettingsLabelPost**](LabelsApi.md#merchantSettingsLabelPost) | **POST** /merchant/settings/label | 
[**merchantSettingsLabelPut**](LabelsApi.md#merchantSettingsLabelPut) | **PUT** /merchant/settings/label | 
[**merchantSettingsLabelsGet**](LabelsApi.md#merchantSettingsLabelsGet) | **GET** /merchant/settings/labels | 


<a name="merchantSettingsLabelIdDelete"></a>
# **merchantSettingsLabelIdDelete**
> merchantSettingsLabelIdDelete(id)



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
    defaultClient.setBasePath("http://0.0.0.0:9000/api/v1");

    LabelsApi apiInstance = new LabelsApi(defaultClient);
    Long id = 56L; // Long | 
    try {
      apiInstance.merchantSettingsLabelIdDelete(id);
    } catch (ApiException e) {
      System.err.println("Exception when calling LabelsApi#merchantSettingsLabelIdDelete");
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

<a name="merchantSettingsLabelIdGet"></a>
# **merchantSettingsLabelIdGet**
> LabelDto merchantSettingsLabelIdGet(id)



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
    defaultClient.setBasePath("http://0.0.0.0:9000/api/v1");

    LabelsApi apiInstance = new LabelsApi(defaultClient);
    Long id = 56L; // Long | 
    try {
      LabelDto result = apiInstance.merchantSettingsLabelIdGet(id);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling LabelsApi#merchantSettingsLabelIdGet");
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

<a name="merchantSettingsLabelPost"></a>
# **merchantSettingsLabelPost**
> merchantSettingsLabelPost(labelDto)



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
    defaultClient.setBasePath("http://0.0.0.0:9000/api/v1");

    LabelsApi apiInstance = new LabelsApi(defaultClient);
    LabelDto labelDto = new LabelDto(); // LabelDto | A JSON object containing updated lable information
    try {
      apiInstance.merchantSettingsLabelPost(labelDto);
    } catch (ApiException e) {
      System.err.println("Exception when calling LabelsApi#merchantSettingsLabelPost");
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

<a name="merchantSettingsLabelPut"></a>
# **merchantSettingsLabelPut**
> merchantSettingsLabelPut(labelDto)



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
    defaultClient.setBasePath("http://0.0.0.0:9000/api/v1");

    LabelsApi apiInstance = new LabelsApi(defaultClient);
    LabelDto labelDto = new LabelDto(); // LabelDto | A JSON object containing updated lable information
    try {
      apiInstance.merchantSettingsLabelPut(labelDto);
    } catch (ApiException e) {
      System.err.println("Exception when calling LabelsApi#merchantSettingsLabelPut");
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

<a name="merchantSettingsLabelsGet"></a>
# **merchantSettingsLabelsGet**
> List&lt;LabelDto&gt; merchantSettingsLabelsGet()



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
    defaultClient.setBasePath("http://0.0.0.0:9000/api/v1");

    LabelsApi apiInstance = new LabelsApi(defaultClient);
    try {
      List<LabelDto> result = apiInstance.merchantSettingsLabelsGet();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling LabelsApi#merchantSettingsLabelsGet");
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

