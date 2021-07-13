package com.TPI.Receptionist.Receptionist.debug;

import com.TPI.Receptionist.Receptionist.service.debug.NodeInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class NodeInfoTests {

    @Test
    public void nodeInfoBuildingFromLineTest() {
        String line = "gke-sdypp-316414-gke-sdypp-316414-gke-368aacec-wq44   Ready    <none>   7h45m   v1.19.9-gke.1900";

        NodeInfo nodeInfo = NodeInfo.build(line);

        Assertions.assertEquals("gke-sdypp-316414-gke-sdypp-316414-gke-368aacec-wq44", nodeInfo.getName());
        Assertions.assertEquals("Ready", nodeInfo.getStatus());
        Assertions.assertEquals("<none>", nodeInfo.getRoles());
        Assertions.assertEquals("7h45m", nodeInfo.getAge());
        Assertions.assertEquals("v1.19.9-gke.1900", nodeInfo.getVersion());
    }

}
