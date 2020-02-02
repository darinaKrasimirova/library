package Library;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="user")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    @Column(name="name")
    private String name;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @Column(name="family_name")
    private String family_name;
    public String getFamily_name() { return family_name; }
    public void setFamily_name(String family_name) {
        this.family_name = family_name;
    }

    @Column(name="is_admin")
    private boolean is_admin;
    public boolean getIs_admin(){ return is_admin;}
    public void setIs_admin(boolean is_admin){this.is_admin=is_admin;}

    @OneToMany(cascade = CascadeType.ALL)
    List<Book> books;
}
