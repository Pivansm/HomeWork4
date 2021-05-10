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
