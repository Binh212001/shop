package org.example.shop.models;

import lombok.Builder;
import lombok.Data;
@Data
@Builder
public class ProductModel {
    private String pid;
    private String title;
    private double price;
    private String description;
    private String image;
    private String size;
    private String color;
    private String seller;
}
