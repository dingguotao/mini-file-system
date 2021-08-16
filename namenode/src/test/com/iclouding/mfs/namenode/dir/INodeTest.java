package com.iclouding.mfs.namenode.dir;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Test;


public class INodeTest {

    @Test
    public void testJson() {
        FileINode file = new FileINode("a.jpg");
        DirectoryINode directory = new DirectoryINode("/a/b");
        directory.setChilds(Lists.newArrayList(file));
        String json = JSONObject.toJSONString(directory, SerializerFeature.WriteClassName);
        System.out.println(json);
        DirectoryINode directoryINode = JSONObject.parseObject(json, DirectoryINode.class);
        Assert.assertTrue(directoryINode.getChilds().get(0) instanceof FileINode);
        Assert.assertTrue(directoryINode.getChilds().get(0) instanceof INode);
        Assert.assertFalse(directoryINode.getChilds().get(0) instanceof DirectoryINode);
    }

}