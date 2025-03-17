package com.shinhan.zoomoney.moneyplan;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Integer>{
	CategoryEntity findByCategoryNum(int categoryNum);
}
