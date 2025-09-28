package ayzekssoft.nextday.dto;

import java.util.Objects;

public class Clothes {
    private Long id;
    private String name;
    private String type; // tshirt, sweatshirt, jeans, shoes, jacket, or "-"
    private String imagePath;

    public Clothes() {}

    public Clothes(Long id, String name, String type, String imagePath) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.imagePath = imagePath;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Clothes clothes = (Clothes) o;
        return Objects.equals(id, clothes.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
