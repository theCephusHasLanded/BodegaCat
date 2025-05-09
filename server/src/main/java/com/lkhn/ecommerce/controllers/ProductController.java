package com.lkhn.ecommerce.controllers;

import com.lkhn.ecommerce.dto.ProductDTO;
import com.lkhn.ecommerce.models.BenefitsCategory;
import com.lkhn.ecommerce.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductDTO productDTO) {
        return ResponseEntity.ok(productService.createProduct(productDTO));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductDTO productDTO) {
        return ResponseEntity.ok(productService.updateProduct(id, productDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }

    // Benefits-specific endpoints
    @GetMapping("/benefits/snap")
    public ResponseEntity<List<ProductDTO>> getSnapEligibleProducts() {
        return ResponseEntity.ok(productService.getSnapEligibleProducts());
    }

    @GetMapping("/benefits/{category}")
    public ResponseEntity<List<ProductDTO>> getProductsByBenefitsCategory(@PathVariable String category) {
        BenefitsCategory benefitsCategory = BenefitsCategory.valueOf(category.toUpperCase());
        return ResponseEntity.ok(productService.getProductsByBenefitsCategory(benefitsCategory));
    }

    @GetMapping("/benefits/program/{programCode}")
    public ResponseEntity<List<ProductDTO>> getProductsByDiscountProgram(@PathVariable String programCode) {
        return ResponseEntity.ok(productService.getProductsByDiscountProgram(programCode));
    }
}
