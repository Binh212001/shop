package org.example.shop.service;

import org.example.shop.entities.Account;
import org.example.shop.entities.Product;
import org.example.shop.models.AccountModel;
import org.example.shop.models.ProductModel;

import java.util.List;

public interface ProductService {
    List<Product> getProducts(int page  , int limit) throws Exception;
    List<Product> getProductByName(int page  , int limit , String name) throws Exception;
    void create(ProductModel p) throws Exception;
    void update(Product p) throws Exception;

    Product getProductById(String id)  throws Exception;;

    List<Product> getProductBySeller(int page, int limit, String userId) throws Exception;
}
