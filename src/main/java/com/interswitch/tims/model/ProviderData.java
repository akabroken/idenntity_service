/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interswitch.tims.model;

import java.net.URI;
import org.apache.http.Header;

/**
 *
 * @author ndegel
 */
public class ProviderData {
    
    private String url;
    private String httpMethod;
    private String request;
    private String response;
    private URI uri; 
    private Header[] inheaders;
    private Header[] outheaders;
    
    private boolean is2waySSL;
        
    private Integer statuCode; 

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public Header[] getInheaders() {
        return inheaders;
    }

    public void setInheaders(Header[] inheaders) {
        this.inheaders = inheaders;
    }

    public Header[] getOutheaders() {
        return outheaders;
    }

    public void setOutheaders(Header[] outheaders) {
        this.outheaders = outheaders;
    }

    public boolean isIs2waySSL() {
        return is2waySSL;
    }

    public void setIs2waySSL(boolean is2waySSL) {
        this.is2waySSL = is2waySSL;
    }

    public Integer getStatuCode() {
        return statuCode;
    }

    public void setStatuCode(Integer statuCode) {
        this.statuCode = statuCode;
    }
    
    
}
