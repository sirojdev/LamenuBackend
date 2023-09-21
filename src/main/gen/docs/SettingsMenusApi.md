# SettingsMenusApi

All URIs are relative to *http://127.0.0.1:9000/v1*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**merchantSettingsCategoriesGet**](SettingsMenusApi.md#merchantSettingsCategoriesGet) | **GET** /merchant/settings/categories |  |
| [**merchantSettingsCategoryIdDelete**](SettingsMenusApi.md#merchantSettingsCategoryIdDelete) | **DELETE** /merchant/settings/category/{id} |  |
| [**merchantSettingsCategoryIdGet**](SettingsMenusApi.md#merchantSettingsCategoryIdGet) | **GET** /merchant/settings/category/{id} |  |
| [**merchantSettingsCategoryPost**](SettingsMenusApi.md#merchantSettingsCategoryPost) | **POST** /merchant/settings/category |  |
| [**merchantSettingsCategoryPut**](SettingsMenusApi.md#merchantSettingsCategoryPut) | **PUT** /merchant/settings/category |  |
| [**merchantSettingsExtraIdDelete**](SettingsMenusApi.md#merchantSettingsExtraIdDelete) | **DELETE** /merchant/settings/extra/{id} |  |
| [**merchantSettingsExtraIdGet**](SettingsMenusApi.md#merchantSettingsExtraIdGet) | **GET** /merchant/settings/extra/{id} |  |
| [**merchantSettingsExtraPost**](SettingsMenusApi.md#merchantSettingsExtraPost) | **POST** /merchant/settings/extra |  |
| [**merchantSettingsExtraPut**](SettingsMenusApi.md#merchantSettingsExtraPut) | **PUT** /merchant/settings/extra |  |
| [**merchantSettingsExtrasGet**](SettingsMenusApi.md#merchantSettingsExtrasGet) | **GET** /merchant/settings/extras |  |
| [**merchantSettingsLabelIdDelete**](SettingsMenusApi.md#merchantSettingsLabelIdDelete) | **DELETE** /merchant/settings/label/{id} |  |
| [**merchantSettingsLabelIdGet**](SettingsMenusApi.md#merchantSettingsLabelIdGet) | **GET** /merchant/settings/label/{id} |  |
| [**merchantSettingsLabelPost**](SettingsMenusApi.md#merchantSettingsLabelPost) | **POST** /merchant/settings/label |  |
| [**merchantSettingsLabelPut**](SettingsMenusApi.md#merchantSettingsLabelPut) | **PUT** /merchant/settings/label |  |
| [**merchantSettingsLabelsGet**](SettingsMenusApi.md#merchantSettingsLabelsGet) | **GET** /merchant/settings/labels |  |
| [**merchantSettingsOptionGet**](SettingsMenusApi.md#merchantSettingsOptionGet) | **GET** /merchant/settings/option |  |
| [**merchantSettingsOptionIdDelete**](SettingsMenusApi.md#merchantSettingsOptionIdDelete) | **DELETE** /merchant/settings/option/{id} |  |
| [**merchantSettingsOptionIdGet**](SettingsMenusApi.md#merchantSettingsOptionIdGet) | **GET** /merchant/settings/option/{id} |  |
| [**merchantSettingsOptionPost**](SettingsMenusApi.md#merchantSettingsOptionPost) | **POST** /merchant/settings/option |  |
| [**merchantSettingsOptionPut**](SettingsMenusApi.md#merchantSettingsOptionPut) | **PUT** /merchant/settings/option |  |
| [**merchantSettingsProductIdDelete**](SettingsMenusApi.md#merchantSettingsProductIdDelete) | **DELETE** /merchant/settings/product/{id} |  |
| [**merchantSettingsProductIdGet**](SettingsMenusApi.md#merchantSettingsProductIdGet) | **GET** /merchant/settings/product/{id} |  |
| [**merchantSettingsProductPost**](SettingsMenusApi.md#merchantSettingsProductPost) | **POST** /merchant/settings/product |  |
| [**merchantSettingsProductPut**](SettingsMenusApi.md#merchantSettingsProductPut) | **PUT** /merchant/settings/product |  |
| [**merchantSettingsProductsGet**](SettingsMenusApi.md#merchantSettingsProductsGet) | **GET** /merchant/settings/products |  |


<a name="merchantSettingsCategoriesGet"></a>
# **merchantSettingsCategoriesGet**
> List&lt;CategoryDto&gt; merchantSettingsCategoriesGet()



Returns a list of all categories

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.SettingsMenusApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://127.0.0.1:9000/v1");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    SettingsMenusApi apiInstance = new SettingsMenusApi(defaultClient);
    try {
      List<CategoryDto> result = apiInstance.merchantSettingsCategoriesGet();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SettingsMenusApi#merchantSettingsCategoriesGet");
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

[**List&lt;CategoryDto&gt;**](CategoryDto.md)

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

<a name="merchantSettingsCategoryIdDelete"></a>
# **merchantSettingsCategoryIdDelete**
> merchantSettingsCategoryIdDelete(id)



Deletes a category by its ID

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.SettingsMenusApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://127.0.0.1:9000/v1");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    SettingsMenusApi apiInstance = new SettingsMenusApi(defaultClient);
    Long id = 56L; // Long | 
    try {
      apiInstance.merchantSettingsCategoryIdDelete(id);
    } catch (ApiException e) {
      System.err.println("Exception when calling SettingsMenusApi#merchantSettingsCategoryIdDelete");
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

<a name="merchantSettingsCategoryIdGet"></a>
# **merchantSettingsCategoryIdGet**
> CategoryDto merchantSettingsCategoryIdGet(id)



Returns a category by its ID

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.SettingsMenusApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://127.0.0.1:9000/v1");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    SettingsMenusApi apiInstance = new SettingsMenusApi(defaultClient);
    Long id = 56L; // Long | 
    try {
      CategoryDto result = apiInstance.merchantSettingsCategoryIdGet(id);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SettingsMenusApi#merchantSettingsCategoryIdGet");
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

[**CategoryDto**](CategoryDto.md)

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

<a name="merchantSettingsCategoryPost"></a>
# **merchantSettingsCategoryPost**
> merchantSettingsCategoryPost(categoryDto)



Adds a new category

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.SettingsMenusApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://127.0.0.1:9000/v1");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    SettingsMenusApi apiInstance = new SettingsMenusApi(defaultClient);
    CategoryDto categoryDto = new CategoryDto(); // CategoryDto | A JSON object containing updated category information
    try {
      apiInstance.merchantSettingsCategoryPost(categoryDto);
    } catch (ApiException e) {
      System.err.println("Exception when calling SettingsMenusApi#merchantSettingsCategoryPost");
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
| **categoryDto** | [**CategoryDto**](CategoryDto.md)| A JSON object containing updated category information | |

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

<a name="merchantSettingsCategoryPut"></a>
# **merchantSettingsCategoryPut**
> merchantSettingsCategoryPut(categoryDto)



Adds a new category

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.SettingsMenusApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://127.0.0.1:9000/v1");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    SettingsMenusApi apiInstance = new SettingsMenusApi(defaultClient);
    CategoryDto categoryDto = new CategoryDto(); // CategoryDto | A JSON object containing updated category information
    try {
      apiInstance.merchantSettingsCategoryPut(categoryDto);
    } catch (ApiException e) {
      System.err.println("Exception when calling SettingsMenusApi#merchantSettingsCategoryPut");
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
| **categoryDto** | [**CategoryDto**](CategoryDto.md)| A JSON object containing updated category information | |

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

<a name="merchantSettingsExtraIdDelete"></a>
# **merchantSettingsExtraIdDelete**
> merchantSettingsExtraIdDelete(id)



Deletes a extra by its ID

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.SettingsMenusApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://127.0.0.1:9000/v1");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    SettingsMenusApi apiInstance = new SettingsMenusApi(defaultClient);
    Long id = 56L; // Long | 
    try {
      apiInstance.merchantSettingsExtraIdDelete(id);
    } catch (ApiException e) {
      System.err.println("Exception when calling SettingsMenusApi#merchantSettingsExtraIdDelete");
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

<a name="merchantSettingsExtraIdGet"></a>
# **merchantSettingsExtraIdGet**
> ExtraDto merchantSettingsExtraIdGet(id)



Returns a extra by its ID

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.SettingsMenusApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://127.0.0.1:9000/v1");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    SettingsMenusApi apiInstance = new SettingsMenusApi(defaultClient);
    Long id = 56L; // Long | 
    try {
      ExtraDto result = apiInstance.merchantSettingsExtraIdGet(id);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SettingsMenusApi#merchantSettingsExtraIdGet");
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

[**ExtraDto**](ExtraDto.md)

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

<a name="merchantSettingsExtraPost"></a>
# **merchantSettingsExtraPost**
> merchantSettingsExtraPost(extraDto)



Adds a new extra

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.SettingsMenusApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://127.0.0.1:9000/v1");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    SettingsMenusApi apiInstance = new SettingsMenusApi(defaultClient);
    ExtraDto extraDto = new ExtraDto(); // ExtraDto | A JSON object containing updated extra information
    try {
      apiInstance.merchantSettingsExtraPost(extraDto);
    } catch (ApiException e) {
      System.err.println("Exception when calling SettingsMenusApi#merchantSettingsExtraPost");
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
| **extraDto** | [**ExtraDto**](ExtraDto.md)| A JSON object containing updated extra information | |

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

<a name="merchantSettingsExtraPut"></a>
# **merchantSettingsExtraPut**
> merchantSettingsExtraPut(extraDto)



Adds a new extra

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.SettingsMenusApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://127.0.0.1:9000/v1");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    SettingsMenusApi apiInstance = new SettingsMenusApi(defaultClient);
    ExtraDto extraDto = new ExtraDto(); // ExtraDto | A JSON object containing updated extra information
    try {
      apiInstance.merchantSettingsExtraPut(extraDto);
    } catch (ApiException e) {
      System.err.println("Exception when calling SettingsMenusApi#merchantSettingsExtraPut");
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
| **extraDto** | [**ExtraDto**](ExtraDto.md)| A JSON object containing updated extra information | |

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

<a name="merchantSettingsExtrasGet"></a>
# **merchantSettingsExtrasGet**
> List&lt;ExtraDto&gt; merchantSettingsExtrasGet()



Returns a list of all extras

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.SettingsMenusApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://127.0.0.1:9000/v1");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    SettingsMenusApi apiInstance = new SettingsMenusApi(defaultClient);
    try {
      List<ExtraDto> result = apiInstance.merchantSettingsExtrasGet();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SettingsMenusApi#merchantSettingsExtrasGet");
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

[**List&lt;ExtraDto&gt;**](ExtraDto.md)

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
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.SettingsMenusApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://127.0.0.1:9000/v1");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    SettingsMenusApi apiInstance = new SettingsMenusApi(defaultClient);
    Long id = 56L; // Long | 
    try {
      apiInstance.merchantSettingsLabelIdDelete(id);
    } catch (ApiException e) {
      System.err.println("Exception when calling SettingsMenusApi#merchantSettingsLabelIdDelete");
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
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.SettingsMenusApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://127.0.0.1:9000/v1");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    SettingsMenusApi apiInstance = new SettingsMenusApi(defaultClient);
    Long id = 56L; // Long | 
    try {
      LabelDto result = apiInstance.merchantSettingsLabelIdGet(id);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SettingsMenusApi#merchantSettingsLabelIdGet");
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

[**LabelDto**](LabelDto.md)

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
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.SettingsMenusApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://127.0.0.1:9000/v1");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    SettingsMenusApi apiInstance = new SettingsMenusApi(defaultClient);
    LabelDto labelDto = new LabelDto(); // LabelDto | A JSON object containing updated lable information
    try {
      apiInstance.merchantSettingsLabelPost(labelDto);
    } catch (ApiException e) {
      System.err.println("Exception when calling SettingsMenusApi#merchantSettingsLabelPost");
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
| **labelDto** | [**LabelDto**](LabelDto.md)| A JSON object containing updated lable information | |

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
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.SettingsMenusApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://127.0.0.1:9000/v1");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    SettingsMenusApi apiInstance = new SettingsMenusApi(defaultClient);
    LabelDto labelDto = new LabelDto(); // LabelDto | A JSON object containing updated lable information
    try {
      apiInstance.merchantSettingsLabelPut(labelDto);
    } catch (ApiException e) {
      System.err.println("Exception when calling SettingsMenusApi#merchantSettingsLabelPut");
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
| **labelDto** | [**LabelDto**](LabelDto.md)| A JSON object containing updated lable information | |

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
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.SettingsMenusApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://127.0.0.1:9000/v1");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    SettingsMenusApi apiInstance = new SettingsMenusApi(defaultClient);
    try {
      List<LabelDto> result = apiInstance.merchantSettingsLabelsGet();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SettingsMenusApi#merchantSettingsLabelsGet");
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

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |
| **204** | No Content |  -  |

<a name="merchantSettingsOptionGet"></a>
# **merchantSettingsOptionGet**
> List&lt;OptionDto&gt; merchantSettingsOptionGet()



Returns a list of all option

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.SettingsMenusApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://127.0.0.1:9000/v1");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    SettingsMenusApi apiInstance = new SettingsMenusApi(defaultClient);
    try {
      List<OptionDto> result = apiInstance.merchantSettingsOptionGet();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SettingsMenusApi#merchantSettingsOptionGet");
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

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |
| **204** | No Content |  -  |

<a name="merchantSettingsOptionIdDelete"></a>
# **merchantSettingsOptionIdDelete**
> merchantSettingsOptionIdDelete(id)



Deletes a option by its ID

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.SettingsMenusApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://127.0.0.1:9000/v1");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    SettingsMenusApi apiInstance = new SettingsMenusApi(defaultClient);
    Long id = 56L; // Long | 
    try {
      apiInstance.merchantSettingsOptionIdDelete(id);
    } catch (ApiException e) {
      System.err.println("Exception when calling SettingsMenusApi#merchantSettingsOptionIdDelete");
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

<a name="merchantSettingsOptionIdGet"></a>
# **merchantSettingsOptionIdGet**
> OptionDto merchantSettingsOptionIdGet(id)



Returns a option by its ID

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.SettingsMenusApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://127.0.0.1:9000/v1");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    SettingsMenusApi apiInstance = new SettingsMenusApi(defaultClient);
    Long id = 56L; // Long | 
    try {
      OptionDto result = apiInstance.merchantSettingsOptionIdGet(id);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SettingsMenusApi#merchantSettingsOptionIdGet");
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

[**OptionDto**](OptionDto.md)

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

<a name="merchantSettingsOptionPost"></a>
# **merchantSettingsOptionPost**
> merchantSettingsOptionPost(optionDto)



Adds a new option

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.SettingsMenusApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://127.0.0.1:9000/v1");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    SettingsMenusApi apiInstance = new SettingsMenusApi(defaultClient);
    OptionDto optionDto = new OptionDto(); // OptionDto | A JSON object containing updated option information
    try {
      apiInstance.merchantSettingsOptionPost(optionDto);
    } catch (ApiException e) {
      System.err.println("Exception when calling SettingsMenusApi#merchantSettingsOptionPost");
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
| **optionDto** | [**OptionDto**](OptionDto.md)| A JSON object containing updated option information | |

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

<a name="merchantSettingsOptionPut"></a>
# **merchantSettingsOptionPut**
> merchantSettingsOptionPut(optionDto)



Adds a new option

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.SettingsMenusApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://127.0.0.1:9000/v1");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    SettingsMenusApi apiInstance = new SettingsMenusApi(defaultClient);
    OptionDto optionDto = new OptionDto(); // OptionDto | A JSON object containing updated option information
    try {
      apiInstance.merchantSettingsOptionPut(optionDto);
    } catch (ApiException e) {
      System.err.println("Exception when calling SettingsMenusApi#merchantSettingsOptionPut");
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
| **optionDto** | [**OptionDto**](OptionDto.md)| A JSON object containing updated option information | |

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

<a name="merchantSettingsProductIdDelete"></a>
# **merchantSettingsProductIdDelete**
> merchantSettingsProductIdDelete(id)



Deletes a product by its ID

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.SettingsMenusApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://127.0.0.1:9000/v1");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    SettingsMenusApi apiInstance = new SettingsMenusApi(defaultClient);
    Long id = 56L; // Long | 
    try {
      apiInstance.merchantSettingsProductIdDelete(id);
    } catch (ApiException e) {
      System.err.println("Exception when calling SettingsMenusApi#merchantSettingsProductIdDelete");
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

<a name="merchantSettingsProductIdGet"></a>
# **merchantSettingsProductIdGet**
> ProductDto merchantSettingsProductIdGet(id)



Returns a product by its ID

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.SettingsMenusApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://127.0.0.1:9000/v1");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    SettingsMenusApi apiInstance = new SettingsMenusApi(defaultClient);
    Long id = 56L; // Long | 
    try {
      ProductDto result = apiInstance.merchantSettingsProductIdGet(id);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SettingsMenusApi#merchantSettingsProductIdGet");
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

[**ProductDto**](ProductDto.md)

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

<a name="merchantSettingsProductPost"></a>
# **merchantSettingsProductPost**
> merchantSettingsProductPost(productInfoDto)



Adds a new product

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.SettingsMenusApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://127.0.0.1:9000/v1");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    SettingsMenusApi apiInstance = new SettingsMenusApi(defaultClient);
    ProductInfoDto productInfoDto = new ProductInfoDto(); // ProductInfoDto | A JSON object containing updated product information
    try {
      apiInstance.merchantSettingsProductPost(productInfoDto);
    } catch (ApiException e) {
      System.err.println("Exception when calling SettingsMenusApi#merchantSettingsProductPost");
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
| **productInfoDto** | [**ProductInfoDto**](ProductInfoDto.md)| A JSON object containing updated product information | |

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

<a name="merchantSettingsProductPut"></a>
# **merchantSettingsProductPut**
> merchantSettingsProductPut(productInfoDto)



Adds a new product

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.SettingsMenusApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://127.0.0.1:9000/v1");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    SettingsMenusApi apiInstance = new SettingsMenusApi(defaultClient);
    ProductInfoDto productInfoDto = new ProductInfoDto(); // ProductInfoDto | A JSON object containing updated product information
    try {
      apiInstance.merchantSettingsProductPut(productInfoDto);
    } catch (ApiException e) {
      System.err.println("Exception when calling SettingsMenusApi#merchantSettingsProductPut");
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
| **productInfoDto** | [**ProductInfoDto**](ProductInfoDto.md)| A JSON object containing updated product information | |

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

<a name="merchantSettingsProductsGet"></a>
# **merchantSettingsProductsGet**
> List&lt;ProductDto&gt; merchantSettingsProductsGet()



Returns a list of all products

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.SettingsMenusApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://127.0.0.1:9000/v1");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    SettingsMenusApi apiInstance = new SettingsMenusApi(defaultClient);
    try {
      List<ProductDto> result = apiInstance.merchantSettingsProductsGet();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SettingsMenusApi#merchantSettingsProductsGet");
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

[**List&lt;ProductDto&gt;**](ProductDto.md)

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

