package org.example.shop.service;

import org.example.shop.entities.Account;
import org.example.shop.entities.Color;
import org.example.shop.entities.Product;
import org.example.shop.entities.Size;
import org.example.shop.models.AccountModel;
import org.example.shop.models.ProductModel;
import org.example.shop.repo.AccountRepo;
import org.example.shop.repo.ColorRepo;
import org.example.shop.repo.ProductRepo;
import org.example.shop.repo.SizeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductServiceImpl implements  ProductService {

    @Autowired
    ProductRepo productRepo;
    @Autowired
    AccountRepo accountRepo;
    @Autowired
    SizeRepo sizeRepo;
    @Autowired
    ColorRepo colorRepo;

    public  AccountModel mapToModel (Account account){
        return  AccountModel.builder()
                .userId(account.getUserId())
                .username(account.getUsername())
                .phone(account.getPhone())
                .addressDetail(account.getAddressDetail())
                .firstName(account.getFirstName())
                .lastName(account.getLastName())
                .isSellers(account.isSellers())
                .province(account.getProvince())
                .district(account.getDistrict())
                .avatar(account.getAvatar())
                .build();
    }

    @Override
    public List<Product> getProducts(int page, int limit) throws Exception {
        try {
            Pageable pageable = PageRequest.of(page, limit);
            return  productRepo.getProducts(pageable);
        }catch (Exception e){
            throw  new Exception(e.getMessage());
        }
    }

    @Override
    public List<Product> getProductByName(int page, int limit, String name) throws Exception {
        try {
            Pageable pageable = PageRequest.of(page, limit);
            return  productRepo.getProductByTitle(pageable,name);
        }catch (Exception e){
            throw  new Exception(e.getMessage());
        }
    }

    @Override
    public void create(ProductModel p) throws Exception {
        try {
            Optional<Account> user = accountRepo.findById(p.getSellerId());
            Product product = new Product();
            product.setPid(p.getId());
            product.setTitle(p.getTitle());
            product.setDescription(p.getDescription());
            product.setPrice(p.getPrice());
            product.setSeller(user.get());
            List<Color>  colors = colorRepo.findByCidIn(p.getColor());
            List<Size> sizes = sizeRepo.findBySidIn(p.getSize());
            product.setColor(colors);
            product.setSize(sizes);
            productRepo.save(product);
        }catch (Exception e){
            throw  new Exception(e.getMessage());
        }
    }

    @Override
    public void update(Product p) throws Exception {
        try {
            Optional<Product>  product =  productRepo.findById(p.getPid());
            if(product.isEmpty()){
            productRepo.save(p);
            }
        }catch (Exception e){
            throw  new Exception(e.getMessage());
        }
    }

    @Override
    public Product getProductById(String id) throws Exception {
        try {
            return  productRepo.findById(id).get();
        }catch (Exception e){
            throw  new Exception(e.getMessage());
        }
    }

    @Override
    public List<Product> getProductBySeller(int page, int limit, String userId) throws  Exception{
        try {
            Pageable pageable = PageRequest.of(page, limit);
            return  productRepo.getProductBySeller(pageable,userId);
        }catch (Exception e){
            throw  new Exception(e.getMessage());
        }
    }
}
