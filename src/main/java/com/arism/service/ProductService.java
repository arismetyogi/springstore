package com.arism.service;

import com.arism.dto.ProductDto;
import com.arism.dto.ProductListDto;
import com.arism.exception.ResourceNotFoundException;
import com.arism.mapper.ProductMapper;
import com.arism.model.Product;
import com.arism.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    private static final String UPLOAD_DIR = "src/main/resources/static/images/";

    @Transactional
    public ProductDto createProduct(ProductDto productDto, MultipartFile image) throws IOException {
        Product product = productMapper.toEntity(productDto);
        if (image!=null && !image.isEmpty()) {
            String fileName = saveImage(image);
            product.setImage("/images/"+fileName);
        }
        Product savedProduct = productRepository.save(product);
        return productMapper.toDto(savedProduct);
    }

    @Transactional
    public ProductDto updateProduct(Long id, ProductDto productDto, MultipartFile image) throws IOException {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Product not found"));
        existingProduct.setName(productDto.getName());
        existingProduct.setDescription(productDto.getDescription());
        existingProduct.setPrice(productDto.getPrice());
        existingProduct.setQuantity(productDto.getQuantity());
        if (image!=null && !image.isEmpty()) {
            String fileName = saveImage(image);
            existingProduct.setImage("/images/"+fileName);
        }

        Product updatedProduct =  productRepository.save(existingProduct);
        return productMapper.toDto(updatedProduct);
    }

    @Transactional
    public void deleteProduct(Long id) throws IOException {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found");
        }
        productRepository.deleteById(id);
    }

    public ProductDto getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Product not found"));
        return productMapper.toDto(product);
    }

    public List<ProductListDto> getAllProducts() {
        return productRepository.findAllWithoutComments();
    }

    public String saveImage(MultipartFile image) throws IOException {
        String fileName = UUID.randomUUID().toString()+image.getOriginalFilename();
        Path path = Paths.get(UPLOAD_DIR+fileName);
        Files.createDirectories(path.getParent());

        Files.write(path, image.getBytes());
        return fileName;
    }
}
