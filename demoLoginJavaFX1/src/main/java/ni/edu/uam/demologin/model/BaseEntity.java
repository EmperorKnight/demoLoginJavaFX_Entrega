package ni.edu.uam.demologin.model;

import java.util.UUID;

public class BaseEntity {
    private UUID id;

    public BaseEntity() {
        this.id = UUID.randomUUID();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
