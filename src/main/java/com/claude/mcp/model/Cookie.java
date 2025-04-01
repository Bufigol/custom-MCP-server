package com.claude.mcp.model;

import com.fasterxml.jackson.annotation.*;
import java.time.Instant;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Cookie {
    private String name;
    private String value;
    private String domain;
    private String path;
    private Instant expires;
    private Integer maxAge;
    private boolean secure;
    private boolean httpOnly;
    private SameSite sameSite;

    public enum SameSite {
        STRICT,
        LAX,
        NONE
    }

    // Constructor por defecto para Jackson
    public Cookie() {
        this.path = "/";
        this.sameSite = SameSite.LAX;
    }

    // Constructor principal
    @JsonCreator
    public Cookie(@JsonProperty("name") String name, @JsonProperty("value") String value) {
        this.name = name;
        this.value = value;
        this.path = "/";
        this.sameSite = SameSite.LAX;
    }

    @JsonGetter("name")
    public String getName() {
        return name;
    }

    @JsonSetter("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonGetter("value")
    public String getValue() {
        return value;
    }

    @JsonSetter("value")
    public void setValue(String value) {
        this.value = value;
    }

    @JsonGetter("domain")
    public String getDomain() {
        return domain;
    }

    @JsonSetter("domain")
    public void setDomain(String domain) {
        this.domain = domain;
    }

    @JsonGetter("path")
    public String getPath() {
        return path;
    }

    @JsonSetter("path")
    public void setPath(String path) {
        this.path = path;
    }

    @JsonGetter("expires")
    public Instant getExpires() {
        return expires;
    }

    @JsonSetter("expires")
    public void setExpires(Instant expires) {
        this.expires = expires;
    }

    @JsonGetter("maxAge")
    public Integer getMaxAge() {
        return maxAge;
    }

    @JsonSetter("maxAge")
    public void setMaxAge(Integer maxAge) {
        this.maxAge = maxAge;
    }

    @JsonGetter("secure")
    public boolean isSecure() {
        return secure;
    }

    @JsonSetter("secure")
    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    @JsonGetter("httpOnly")
    public boolean isHttpOnly() {
        return httpOnly;
    }

    @JsonSetter("httpOnly")
    public void setHttpOnly(boolean httpOnly) {
        this.httpOnly = httpOnly;
    }

    @JsonGetter("sameSite")
    public SameSite getSameSite() {
        return sameSite;
    }

    @JsonSetter("sameSite")
    public void setSameSite(SameSite sameSite) {
        this.sameSite = sameSite;
    }

    @JsonIgnore
    public boolean isExpired() {
        if (expires != null) {
            return Instant.now().isAfter(expires);
        }
        if (maxAge != null) {
            return maxAge <= 0;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append("=").append(value);
        
        if (domain != null) sb.append("; Domain=").append(domain);
        if (path != null) sb.append("; Path=").append(path);
        if (expires != null) sb.append("; Expires=").append(expires);
        if (maxAge != null) sb.append("; Max-Age=").append(maxAge);
        if (secure) sb.append("; Secure");
        if (httpOnly) sb.append("; HttpOnly");
        if (sameSite != null) sb.append("; SameSite=").append(sameSite);
        
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cookie cookie = (Cookie) o;
        return Objects.equals(name, cookie.name) &&
               Objects.equals(domain, cookie.domain) &&
               Objects.equals(path, cookie.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, domain, path);
    }
} 