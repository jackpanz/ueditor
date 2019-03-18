//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.github.jackpanz.ueditor.aliyun;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.DeleteObjectsRequest;
import com.aliyun.oss.model.ListObjectsRequest;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;

import java.io.InputStream;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

public class OSSUtils {

    public OSSUtils() {

    }

    // 实时的分页查询
    public static OSSPage<OSSObjectSummary> listPage(String dir, String nextMarker, Integer maxKeys) {
        OSSClient client = new OSSClient(OSSConfig.endpoint, OSSConfig.accessKeyId, OSSConfig.accessKeySecret);
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest(OSSConfig.bucketName);
        listObjectsRequest.setPrefix(dir);
        listObjectsRequest.setMarker(nextMarker);
        listObjectsRequest.setMaxKeys(maxKeys);
        ObjectListing objectListing = client.listObjects(listObjectsRequest);

        List<OSSObjectSummary> summrayList = objectListing.getObjectSummaries();
        OSSPage<OSSObjectSummary> page = new OSSPage();
        page.addAll(summrayList);

        page.setMaxKeys(objectListing.getMaxKeys());
        page.setNewxNextMarker(objectListing.getNextMarker());
        client.shutdown();
        return page;
    }

    public static void copyOSS(String sourceKey, String destinationKey) {
        OSSClient client = new OSSClient(OSSConfig.endpoint, OSSConfig.accessKeyId, OSSConfig.accessKeySecret);
        client.copyObject(OSSConfig.bucketName, sourceKey, OSSConfig.bucketName, destinationKey);
        client.shutdown();
    }

    public static boolean existObject(String ssoKeyName) {
        OSSClient client = new OSSClient(OSSConfig.endpoint, OSSConfig.accessKeyId, OSSConfig.accessKeySecret);
        boolean value = client.doesObjectExist(OSSConfig.bucketName, ssoKeyName);
        client.shutdown();
        return value;
    }

    public static void uploadToOSS(String ssoKeyName, InputStream inputStream) {
        OSSClient client = new OSSClient(OSSConfig.endpoint, OSSConfig.accessKeyId, OSSConfig.accessKeySecret);
        client.putObject(OSSConfig.bucketName, ssoKeyName, inputStream);
        client.shutdown();
    }

    public static void deleteFileToOSS(String ssoKeyName) {
        OSSClient client = new OSSClient(OSSConfig.endpoint, OSSConfig.accessKeyId, OSSConfig.accessKeySecret);
        client.deleteObject(OSSConfig.bucketName, ssoKeyName);
        client.shutdown();
    }

    public static void deleteFileToOSS(List<String> ssoKeyNames) {
        if (CollectionUtils.isNotEmpty(ssoKeyNames)) {
            OSSClient client = new OSSClient(OSSConfig.endpoint, OSSConfig.accessKeyId, OSSConfig.accessKeySecret);
            DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(OSSConfig.bucketName);
            deleteObjectsRequest.setKeys(ssoKeyNames);
            client.deleteObjects(deleteObjectsRequest);
            client.shutdown();
        }

    }
}
