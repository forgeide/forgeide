package org.forgeide.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToOne;

/**
 * Contains the binary content of a resource
 *
 * @author Shane Bryzak
 */
@Entity
public class ResourceContent implements Serializable {

    private static final long serialVersionUID = -8308595869048197031L;

    @Id @OneToOne
    private ProjectResource resource;

    @Lob
    private byte[] content;

    public ProjectResource getResource() {
        return resource;
    }

    public void setResource(ProjectResource resource) {
        this.resource = resource;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
