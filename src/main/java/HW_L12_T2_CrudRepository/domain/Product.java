package HW_L12_T2_CrudRepository.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public class Product {
    //@Id
    private UUID id;
    //@Column
    private String name;
    //@Column
    private String description;
    //@Column
    //@Enumerated(EnumType.STRING)
    private ProductCategory category;
    //@Column
    private LocalDateTime manufactureDateTime;
    //@Column
    private String manufacturer;
    //@Column
    private Boolean hasExpiryTime;
    //@Column
    private int stock;

    //
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ProductCategory getCategory() {
        return category;
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }

    public LocalDateTime getManufactureDateTime() {
        return manufactureDateTime;
    }

    public void setManufactureDateTime(LocalDateTime manufactureDateTime) {
        this.manufactureDateTime = manufactureDateTime;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public Boolean getHasExpiryTime() {
        return hasExpiryTime;
    }

    public void setHasExpiryTime(Boolean hasExpiryTime) {
        this.hasExpiryTime = hasExpiryTime;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", category=" + category +
                ", manufactureDateTime=" + manufactureDateTime +
                ", manufacturer='" + manufacturer + '\'' +
                ", hasExpiryTime=" + hasExpiryTime +
                ", stock=" + stock +
                '}';
    }


}
