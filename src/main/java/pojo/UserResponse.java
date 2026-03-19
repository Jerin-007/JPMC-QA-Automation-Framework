package pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

// THE ENTERPRISE SECRET: Tell Jackson to ignore the bloated "_meta" stuff we dont't care about!
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserResponse {

    // 1. The exact keys returned by the server
    private String name;
    private String job;
    private String id;
    private String createdAt;

    // 2. We don't need a Constructor here because Jackson will build this for us!

    // 3. Getters and Setters (Jackson needs these to inject the data)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

}
