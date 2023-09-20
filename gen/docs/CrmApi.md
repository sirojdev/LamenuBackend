# CrmApi

All URIs are relative to *http://0.0.0.0:9000/api/v1*

Method | HTTP request | Description
------------- | ------------- | -------------
[**merchantCrmBadgeIdGet**](CrmApi.md#merchantCrmBadgeIdGet) | **GET** /merchant/crm/badge/{id} | 
[**merchantCrmBadgePost**](CrmApi.md#merchantCrmBadgePost) | **POST** /merchant/crm/badge | Create a new B
[**merchantCrmMessagePost**](CrmApi.md#merchantCrmMessagePost) | **POST** /merchant/crm/message | Create a new message
[**merchantCrmMessagesGet**](CrmApi.md#merchantCrmMessagesGet) | **GET** /merchant/crm/messages | Get all messages
[**merchantCrmSmsIdDelete**](CrmApi.md#merchantCrmSmsIdDelete) | **DELETE** /merchant/crm/sms/{id} | Delete a single SMS message by ID
[**merchantCrmSmsIdGet**](CrmApi.md#merchantCrmSmsIdGet) | **GET** /merchant/crm/sms/{id} | Get a single SMS message by ID
[**merchantCrmSmsPost**](CrmApi.md#merchantCrmSmsPost) | **POST** /merchant/crm/sms | Create a new SMS message
[**merchantCrmSmssGet**](CrmApi.md#merchantCrmSmssGet) | **GET** /merchant/crm/smss | Get all SMS messages
[**messageIdDelete**](CrmApi.md#messageIdDelete) | **DELETE** /message/{id} | Delete a single message by ID
[**messageIdGet**](CrmApi.md#messageIdGet) | **GET** /message/{id} | Get a single message by ID


<a name="merchantCrmBadgeIdGet"></a>
# **merchantCrmBadgeIdGet**
> BadgeDto merchantCrmBadgeIdGet(id)



Returns a badge by ID

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.CrmApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://0.0.0.0:9000/api/v1");

    CrmApi apiInstance = new CrmApi(defaultClient);
    Long id = 56L; // Long | 
    try {
      BadgeDto result = apiInstance.merchantCrmBadgeIdGet(id);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling CrmApi#merchantCrmBadgeIdGet");
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

[**BadgeDto**](BadgeDto.md)

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

<a name="merchantCrmBadgePost"></a>
# **merchantCrmBadgePost**
> Integer merchantCrmBadgePost(badgeDto)

Create a new B

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.CrmApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://0.0.0.0:9000/api/v1");

    CrmApi apiInstance = new CrmApi(defaultClient);
    BadgeDto badgeDto = new BadgeDto(); // BadgeDto | 
    try {
      Integer result = apiInstance.merchantCrmBadgePost(badgeDto);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling CrmApi#merchantCrmBadgePost");
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
 **badgeDto** | [**BadgeDto**](BadgeDto.md)|  | [optional]

### Return type

**Integer**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | OK |  -  |
**400** | Bad request |  -  |

<a name="merchantCrmMessagePost"></a>
# **merchantCrmMessagePost**
> Integer merchantCrmMessagePost(messageDto)

Create a new message

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.CrmApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://0.0.0.0:9000/api/v1");

    CrmApi apiInstance = new CrmApi(defaultClient);
    MessageDto messageDto = new MessageDto(); // MessageDto | 
    try {
      Integer result = apiInstance.merchantCrmMessagePost(messageDto);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling CrmApi#merchantCrmMessagePost");
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
 **messageDto** | [**MessageDto**](MessageDto.md)|  | [optional]

### Return type

**Integer**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | OK |  -  |
**400** | Bad request |  -  |

<a name="merchantCrmMessagesGet"></a>
# **merchantCrmMessagesGet**
> List&lt;MessageDto&gt; merchantCrmMessagesGet()

Get all messages

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.CrmApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://0.0.0.0:9000/api/v1");

    CrmApi apiInstance = new CrmApi(defaultClient);
    try {
      List<MessageDto> result = apiInstance.merchantCrmMessagesGet();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling CrmApi#merchantCrmMessagesGet");
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

[**List&lt;MessageDto&gt;**](MessageDto.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | OK |  -  |
**204** | No content |  -  |

<a name="merchantCrmSmsIdDelete"></a>
# **merchantCrmSmsIdDelete**
> merchantCrmSmsIdDelete(id)

Delete a single SMS message by ID

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.CrmApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://0.0.0.0:9000/api/v1");

    CrmApi apiInstance = new CrmApi(defaultClient);
    Long id = 56L; // Long | The ID of the SMS message
    try {
      apiInstance.merchantCrmSmsIdDelete(id);
    } catch (ApiException e) {
      System.err.println("Exception when calling CrmApi#merchantCrmSmsIdDelete");
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
 **id** | **Long**| The ID of the SMS message |

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
**400** | Bad request |  -  |

<a name="merchantCrmSmsIdGet"></a>
# **merchantCrmSmsIdGet**
> SmsDto merchantCrmSmsIdGet(id)

Get a single SMS message by ID

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.CrmApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://0.0.0.0:9000/api/v1");

    CrmApi apiInstance = new CrmApi(defaultClient);
    Long id = 56L; // Long | The ID of the SMS message
    try {
      SmsDto result = apiInstance.merchantCrmSmsIdGet(id);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling CrmApi#merchantCrmSmsIdGet");
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
 **id** | **Long**| The ID of the SMS message |

### Return type

[**SmsDto**](SmsDto.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | OK |  -  |
**204** | No content |  -  |
**400** | Bad request |  -  |

<a name="merchantCrmSmsPost"></a>
# **merchantCrmSmsPost**
> Integer merchantCrmSmsPost(smsDto)

Create a new SMS message

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.CrmApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://0.0.0.0:9000/api/v1");

    CrmApi apiInstance = new CrmApi(defaultClient);
    SmsDto smsDto = new SmsDto(); // SmsDto | 
    try {
      Integer result = apiInstance.merchantCrmSmsPost(smsDto);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling CrmApi#merchantCrmSmsPost");
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
 **smsDto** | [**SmsDto**](SmsDto.md)|  | [optional]

### Return type

**Integer**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | OK |  -  |
**400** | Bad request |  -  |

<a name="merchantCrmSmssGet"></a>
# **merchantCrmSmssGet**
> List&lt;SmsDto&gt; merchantCrmSmssGet()

Get all SMS messages

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.CrmApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://0.0.0.0:9000/api/v1");

    CrmApi apiInstance = new CrmApi(defaultClient);
    try {
      List<SmsDto> result = apiInstance.merchantCrmSmssGet();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling CrmApi#merchantCrmSmssGet");
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

[**List&lt;SmsDto&gt;**](SmsDto.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | OK |  -  |
**204** | No content |  -  |

<a name="messageIdDelete"></a>
# **messageIdDelete**
> messageIdDelete(id)

Delete a single message by ID

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.CrmApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://0.0.0.0:9000/api/v1");

    CrmApi apiInstance = new CrmApi(defaultClient);
    Long id = 56L; // Long | The ID of the message
    try {
      apiInstance.messageIdDelete(id);
    } catch (ApiException e) {
      System.err.println("Exception when calling CrmApi#messageIdDelete");
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
 **id** | **Long**| The ID of the message |

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
**400** | Bad request |  -  |

<a name="messageIdGet"></a>
# **messageIdGet**
> MessageDto messageIdGet(id)

Get a single message by ID

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.CrmApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://0.0.0.0:9000/api/v1");

    CrmApi apiInstance = new CrmApi(defaultClient);
    Long id = 56L; // Long | The ID of the message
    try {
      MessageDto result = apiInstance.messageIdGet(id);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling CrmApi#messageIdGet");
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
 **id** | **Long**| The ID of the message |

### Return type

[**MessageDto**](MessageDto.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | OK |  -  |
**204** | No content |  -  |
**400** | Bad request |  -  |

