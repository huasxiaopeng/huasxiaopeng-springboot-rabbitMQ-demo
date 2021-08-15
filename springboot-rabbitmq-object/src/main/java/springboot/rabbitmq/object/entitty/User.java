package springboot.rabbitmq.object.entitty;

import java.io.Serializable;

/**
 * @author lktbz
 * @version 1.0.0
 * @date 2021/8/15
 * @desc
 */
public class User  implements Serializable {
  private Long id;
  private String name;

    public User() {
    }

    public User(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
