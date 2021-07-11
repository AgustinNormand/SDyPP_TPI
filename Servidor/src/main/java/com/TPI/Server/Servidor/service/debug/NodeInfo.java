package com.TPI.Server.Servidor.service.debug;

public class NodeInfo {

    private String name;
    private String status;
    private String roles;
    private String age;
    private String version;

    public static NodeInfo build(String nodeInfoLine) {
        NodeInfo nodeInfo = new NodeInfo();
        String[] components = nodeInfoLine.split("\\s+");
        nodeInfo.setName(components[0]);
        nodeInfo.setStatus(components[1]);
        nodeInfo.setRoles(components[2]);
        nodeInfo.setAge(components[3]);
        nodeInfo.setVersion(components[4]);
        return nodeInfo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
