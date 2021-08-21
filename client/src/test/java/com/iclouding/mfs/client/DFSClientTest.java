package com.iclouding.mfs.client;

import com.iclouding.mfs.client.config.Configuration;
import junit.framework.TestCase;
import org.junit.Test;

public class DFSClientTest extends TestCase {

    @Test
    public void testClient(){
        DFSClient dfsClient = new DFSClient(new Configuration());
        dfsClient.mkdirs("/a/b/d",true);
        dfsClient.mkdirs("/a/b/c",false);
        dfsClient.renamedirs("/a/b/c","/a/b/e");

    }

}