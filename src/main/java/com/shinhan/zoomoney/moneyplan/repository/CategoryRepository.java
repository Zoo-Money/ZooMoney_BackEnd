package com.shinhan.zoomoney.moneyplan.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shinhan.zoomoney.moneyplan.entity.CategoryEntity;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Integer> {
	CategoryEntity findByCategoryNum(int categoryNum);
}
