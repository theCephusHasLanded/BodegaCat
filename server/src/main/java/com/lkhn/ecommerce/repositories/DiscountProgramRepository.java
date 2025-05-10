package com.lkhn.ecommerce.repositories;

import com.lkhn.ecommerce.models.DiscountProgram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DiscountProgramRepository extends JpaRepository<DiscountProgram, Long> {

    Optional<DiscountProgram> findByProgramCode(String programCode);
}
