# uediter
- Support spring mvc upload
- Support spring boot upload
- Support local storage
- Support Alibaba Cloud Storage
# Download

Gradle:
```gradle
wait
```
Maven:
```xml
wait
```
# Usage
Configure your Controller url 
 - /ueditoru/editor.config.js 
```js
...
window.UEDITOR_CONFIG = {
    UEDITOR_HOME_URL: URL
, serverUrl:window.location.protocol  + "//" + window.location.host + "/ueditor/controller"
...
```
Local storage
```java
@ResponseBody
@RequestMapping(value = "/ueditor/aliyun", method = {RequestMethod.POST, RequestMethod.GET})
public String aliyun(HttpServletRequest request) {
    String value = new ActionEnter(request, new LocalSpringFileManager(
            new File("D:/java/nginx-1.15.8/html"),
            "http://localhost/")
    ).exec();
    return value;
}
```

Alibaba Cloud
```java
@ResponseBody
@RequestMapping(value = "aliyun", method = {RequestMethod.POST, RequestMethod.GET})
public String aliyun(HttpServletRequest request) {

    if ( isLoad++ == 0) {
        OSSConfig.image_access = "http://xxx.aliyuncs.com/";
        OSSConfig.accessKeyId = "accessKeyId";
        OSSConfig.accessKeySecret = "accessKeySecret";
        OSSConfig.endpoint = "endpoint";
        OSSConfig.bucketName = "bucketName";
    }

    String value = new ActionEnter(request, new OSSSpringFileManager()).exec();
    return value;
}
```
