package org.apx.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: oleg
 * Date: 11.03.14
 * Time: 14:54
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "users")
public class User implements Serializable {

    String id;
    String name;

    public User(){
        id = UUID.randomUUID().toString();
        name = "";
    }

    @Id
    @Column(name = "id",length = 36, nullable = false, unique = true)
    public String getId() {
        return id;
    }

    @Column(name = "name",length = 256,nullable = false)
    public String getName() {
        return name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;
        if(!(obj instanceof User)) return false;
        User u = (User) obj;
        return new EqualsBuilder().append(this.id,u.id).isEquals();
    }
}
