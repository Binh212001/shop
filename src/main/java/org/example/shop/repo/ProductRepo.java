package org.example.shop.repo;

import org.example.shop.entities.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product, String> {
    @Query(value = "select * from products", nativeQuery = true)
    List<Product> getProducts(Pageable pageable);
    @Query(value = "select * from products where LOWER(products.title) like %:title%", nativeQuery = true)

    List<Product> getProductByTitle(Pageable pageable , String title);

    @Query(value = "insert into  Product(pid,title,price,description,image,size,color) values(:id,:employeeCode,:date,:reason,:status)", nativeQuery = true)
    void create(String id, String employeeCode, String date, String reason, String status);

    @Query(value = "select * from products where products.user_id like %:userId%", nativeQuery = true)

    List<Product> getProductBySeller(Pageable pageable , String userId);
    @Query(value = "select * from products where products.category = :category", nativeQuery = true)
    List<Product> getByCategory(Pageable pageable , String category);

    long count();
}