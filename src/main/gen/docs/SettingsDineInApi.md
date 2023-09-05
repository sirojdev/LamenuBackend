# SettingsDineInApi

All URIs are relative to *http://127.0.0.1:9000/v1*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**merchantSettingsRoomIdDelete**](SettingsDineInApi.md#merchantSettingsRoomIdDelete) | **DELETE** /merchant/settings/room/{id} |  |
| [**merchantSettingsRoomIdGet**](SettingsDineInApi.md#merchantSettingsRoomIdGet) | **GET** /merchant/settings/room/{id} |  |
| [**merchantSettingsRoomPost**](SettingsDineInApi.md#merchantSettingsRoomPost) | **POST** /merchant/settings/room |  |
| [**merchantSettingsRoomPut**](SettingsDineInApi.md#merchantSettingsRoomPut) | **PUT** /merchant/settings/room |  |
| [**merchantSettingsRoomsGet**](SettingsDineInApi.md#merchantSettingsRoomsGet) | **GET** /merchant/settings/rooms |  |
| [**merchantSettingsTableIdDelete**](SettingsDineInApi.md#merchantSettingsTableIdDelete) | **DELETE** /merchant/settings/table/{id} |  |
| [**merchantSettingsTableIdGet**](SettingsDineInApi.md#merchantSettingsTableIdGet) | **GET** /merchant/settings/table/{id} |  |
| [**merchantSettingsTablePost**](SettingsDineInApi.md#merchantSettingsTablePost) | **POST** /merchant/settings/table |  |
| [**merchantSettingsTablePut**](SettingsDineInApi.md#merchantSettingsTablePut) | **PUT** /merchant/settings/table |  |
| [**merchantSettingsTablesGet**](SettingsDineInApi.md#merchantSettingsTablesGet) | **GET** /merchant/settings/tables |  |


<a name="merchantSettingsRoomIdDelete"></a>
# **merchantSettingsRoomIdDelete**
> merchantSettingsRoomIdDelete(id)



Deletes a room by its ID

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.SettingsDineInApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://127.0.0.1:9000/v1");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    SettingsDineInApi apiInstance = new SettingsDineInApi(defaultClient);
    Long id = 56L; // Long | 
    try {
      apiInstance.merchantSettingsRoomIdDelete(id);
    } catch (ApiException e) {
      System.err.println("Exception when calling SettingsDineInApi#merchantSettingsRoomIdDelete");
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

<a name="merchantSettingsRoomIdGet"></a>
# **merchantSettingsRoomIdGet**
> RoomDto merchantSettingsRoomIdGet(id)



Returns a room by its ID

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.SettingsDineInApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://127.0.0.1:9000/v1");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    SettingsDineInApi apiInstance = new SettingsDineInApi(defaultClient);
    Long id = 56L; // Long | 
    try {
      RoomDto result = apiInstance.merchantSettingsRoomIdGet(id);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SettingsDineInApi#merchantSettingsRoomIdGet");
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

[**RoomDto**](RoomDto.md)

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

<a name="merchantSettingsRoomPost"></a>
# **merchantSettingsRoomPost**
> merchantSettingsRoomPost(roomDto)



Adds a new room

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.SettingsDineInApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://127.0.0.1:9000/v1");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    SettingsDineInApi apiInstance = new SettingsDineInApi(defaultClient);
    RoomDto roomDto = new RoomDto(); // RoomDto | A JSON object containing updated room's information
    try {
      apiInstance.merchantSettingsRoomPost(roomDto);
    } catch (ApiException e) {
      System.err.println("Exception when calling SettingsDineInApi#merchantSettingsRoomPost");
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
| **roomDto** | [**RoomDto**](RoomDto.md)| A JSON object containing updated room&#39;s information | |

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

<a name="merchantSettingsRoomPut"></a>
# **merchantSettingsRoomPut**
> merchantSettingsRoomPut(roomDto)



Adds a new room

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.SettingsDineInApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://127.0.0.1:9000/v1");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    SettingsDineInApi apiInstance = new SettingsDineInApi(defaultClient);
    RoomDto roomDto = new RoomDto(); // RoomDto | A JSON object containing updated room's information
    try {
      apiInstance.merchantSettingsRoomPut(roomDto);
    } catch (ApiException e) {
      System.err.println("Exception when calling SettingsDineInApi#merchantSettingsRoomPut");
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
| **roomDto** | [**RoomDto**](RoomDto.md)| A JSON object containing updated room&#39;s information | |

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

<a name="merchantSettingsRoomsGet"></a>
# **merchantSettingsRoomsGet**
> List&lt;RoomDto&gt; merchantSettingsRoomsGet()



Returns a list of all rooms

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.SettingsDineInApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://127.0.0.1:9000/v1");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    SettingsDineInApi apiInstance = new SettingsDineInApi(defaultClient);
    try {
      List<RoomDto> result = apiInstance.merchantSettingsRoomsGet();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SettingsDineInApi#merchantSettingsRoomsGet");
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

[**List&lt;RoomDto&gt;**](RoomDto.md)

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

<a name="merchantSettingsTableIdDelete"></a>
# **merchantSettingsTableIdDelete**
> merchantSettingsTableIdDelete(id)



Deletes a table by its ID

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.SettingsDineInApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://127.0.0.1:9000/v1");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    SettingsDineInApi apiInstance = new SettingsDineInApi(defaultClient);
    Long id = 56L; // Long | 
    try {
      apiInstance.merchantSettingsTableIdDelete(id);
    } catch (ApiException e) {
      System.err.println("Exception when calling SettingsDineInApi#merchantSettingsTableIdDelete");
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

<a name="merchantSettingsTableIdGet"></a>
# **merchantSettingsTableIdGet**
> TableDto merchantSettingsTableIdGet(id)



Returns a table by its ID

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.SettingsDineInApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://127.0.0.1:9000/v1");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    SettingsDineInApi apiInstance = new SettingsDineInApi(defaultClient);
    Long id = 56L; // Long | 
    try {
      TableDto result = apiInstance.merchantSettingsTableIdGet(id);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SettingsDineInApi#merchantSettingsTableIdGet");
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

[**TableDto**](TableDto.md)

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

<a name="merchantSettingsTablePost"></a>
# **merchantSettingsTablePost**
> merchantSettingsTablePost(tableDto)



Adds a new table

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.SettingsDineInApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://127.0.0.1:9000/v1");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    SettingsDineInApi apiInstance = new SettingsDineInApi(defaultClient);
    TableDto tableDto = new TableDto(); // TableDto | A JSON object containing updated table's information
    try {
      apiInstance.merchantSettingsTablePost(tableDto);
    } catch (ApiException e) {
      System.err.println("Exception when calling SettingsDineInApi#merchantSettingsTablePost");
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
| **tableDto** | [**TableDto**](TableDto.md)| A JSON object containing updated table&#39;s information | |

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

<a name="merchantSettingsTablePut"></a>
# **merchantSettingsTablePut**
> merchantSettingsTablePut(tableDto)



Adds a new table

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.SettingsDineInApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://127.0.0.1:9000/v1");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    SettingsDineInApi apiInstance = new SettingsDineInApi(defaultClient);
    TableDto tableDto = new TableDto(); // TableDto | A JSON object containing updated table's information
    try {
      apiInstance.merchantSettingsTablePut(tableDto);
    } catch (ApiException e) {
      System.err.println("Exception when calling SettingsDineInApi#merchantSettingsTablePut");
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
| **tableDto** | [**TableDto**](TableDto.md)| A JSON object containing updated table&#39;s information | |

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

<a name="merchantSettingsTablesGet"></a>
# **merchantSettingsTablesGet**
> List&lt;TableDto&gt; merchantSettingsTablesGet()



Returns a list of all tables

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.SettingsDineInApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://127.0.0.1:9000/v1");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    SettingsDineInApi apiInstance = new SettingsDineInApi(defaultClient);
    try {
      List<TableDto> result = apiInstance.merchantSettingsTablesGet();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SettingsDineInApi#merchantSettingsTablesGet");
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

[**List&lt;TableDto&gt;**](TableDto.md)

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

