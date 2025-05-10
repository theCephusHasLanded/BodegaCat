package com.lkhn.ecommerce.services;

import com.lkhn.ecommerce.dto.ProductDTO;
import com.lkhn.ecommerce.models.BenefitsCategory;
import com.lkhn.ecommerce.models.DiscountProgram;
import com.lkhn.ecommerce.models.Product;
import com.lkhn.ecommerce.repositories.DiscountProgramRepository;
import com.lkhn.ecommerce.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private DiscountProgramRepository discountProgramRepository;

    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));
        return convertToDTO(product);
    }

    @Transactional
    public ProductDTO createProduct(ProductDTO productDTO) {
        Product product = convertToEntity(productDTO);
        Product savedProduct = productRepository.save(product);
        return convertToDTO(savedProduct);
    }

    @Transactional
    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));

        // Update fields
        existingProduct.setName(productDTO.getName());
        existingProduct.setDescription(productDTO.getDescription());
        existingProduct.setPrice(productDTO.getPrice());
        existingProduct.setImageUrl(productDTO.getImageUrl());
        existingProduct.setStockQuantity(productDTO.getStockQuantity());
        existingProduct.setSnapEligible(productDTO.isSnapEligible());
        existingProduct.setBenefitsCategory(productDTO.getBenefitsCategory());

        // Update discount programs
        Set<DiscountProgram> discountPrograms = new HashSet<>();
        if (productDTO.getDiscountPrograms() != null) {
            for (String programCode : productDTO.getDiscountPrograms()) {
                DiscountProgram program = discountProgramRepository.findByProgramCode(programCode)
                        .orElseThrow(() -> new EntityNotFoundException("Discount program not found with code: " + programCode));
                discountPrograms.add(program);
            }
        }
        existingProduct.setDiscountPrograms(discountPrograms);

        Product updatedProduct = productRepository.save(existingProduct);
        return convertToDTO(updatedProduct);
    }

    @Transactional
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new EntityNotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    // Benefits-specific methods
    public List<ProductDTO> getSnapEligibleProducts() {
        return productRepository.findBySnapEligibleTrue().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ProductDTO> getProductsByBenefitsCategory(BenefitsCategory category) {
        return productRepository.findByBenefitsCategory(category).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ProductDTO> getProductsByDiscountProgram(String programCode) {
        return productRepository.findByDiscountProgramCode(programCode).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Helper methods
    private ProductDTO convertToDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setImageUrl(product.getImageUrl());
        dto.setStockQuantity(product.getStockQuantity());
        dto.setSnapEligible(product.isSnapEligible());
        dto.setBenefitsCategory(product.getBenefitsCategory());

        Set<String> programCodes = product.getDiscountPrograms().stream()
                .map(DiscountProgram::getProgramCode)
                .collect(Collectors.toSet());
        dto.setDiscountPrograms(programCodes);

        return dto;
    }

    private Product convertToEntity(ProductDTO dto) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setImageUrl(dto.getImageUrl());
        product.setStockQuantity(dto.getStockQuantity());
        product.setSnapEligible(dto.isSnapEligible());
        product.setBenefitsCategory(dto.getBenefitsCategory());

        Set<DiscountProgram> discountPrograms = new HashSet<>();
        if (dto.getDiscountPrograms() != null) {
            for (String programCode : dto.getDiscountPrograms()) {
                discountProgramRepository.findByProgramCode(programCode)
                        .ifPresent(discountPrograms::add);
            }
        }
        product.setDiscountPrograms(discountPrograms);

        return product;
    }
}
