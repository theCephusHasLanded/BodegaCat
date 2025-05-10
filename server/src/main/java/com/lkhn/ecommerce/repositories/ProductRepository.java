package com.lkhn.ecommerce.repositories;

import com.lkhn.ecommerce.models.BenefitsCategory;
import com.lkhn.ecommerce.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findBySnapEligibleTrue();

    List<Product> findByBenefitsCategory(BenefitsCategory category);

    @Query("SELECT p FROM Product p JOIN p.discountPrograms d WHERE d.programCode = :programCode")
    List<Product> findByDiscountProgramCode(String programCode);
}
