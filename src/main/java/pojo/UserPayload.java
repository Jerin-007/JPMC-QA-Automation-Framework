package pojo;

public class UserPayload {
    // 1. Define the exact keys the API expects
    private String name;
    private String job;

    // 2. The Constructor (How we bulid the user)
    public UserPayload(String name, String job) {
        this.name = name;
        this.job = job;
    }

    // 3. Getters and Setters (Required for Jackson to read the data)
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
}
