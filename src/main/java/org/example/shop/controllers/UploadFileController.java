package org.example.shop.controllers;

import org.example.shop.entities.Product;
import org.example.shop.repo.ProductRepo;
import org.example.shop.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/")
public class UploadFileController {
    @Autowired
    ProductRepo productRepo;

    @PostMapping("product/upload")
    public ResponseEntity<Response<String>> handleFileUpload(@RequestParam("file") MultipartFile file, @RequestParam("productId") String productId) {

        Optional<Product> product = productRepo.findById(productId);
        if (product.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response<String>("Không tìm thấy sản phẩm" + productId, "Failed"));
        }
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response<String>("Vui lòng chọn file .", "Failed"));
        }
        try {
            byte[] bytes = file.getBytes();
            String urlPath = "C:\\Codding\\" + UUID.randomUUID() +file.getOriginalFilename();
            Path path = Paths.get(urlPath);
            Files.write(path, bytes);
            product.get().setImage(urlPath);
            productRepo.save(product.get());
            return ResponseEntity.status(HttpStatus.OK).body(new Response<String>("Ok ", "Success"));

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response<>("Failed to upload file", e.getMessage()));
        }
    }
}
