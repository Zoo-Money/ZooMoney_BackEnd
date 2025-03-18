package com.shinhan.zoomoney.stock;

import java.sql.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StockHistoryService {

	@Autowired
	StockHistoryRepository historyRepo;
	
	public List<StockHistoryDto> selectStockHitory(Integer MemberNum){
		List<StockHistoryEntity> historyEntityList = historyRepo.findByMember_MemberNum(MemberNum);
		List<StockHistoryDto> historyDTOList = historyEntityList.stream().map(entity->EntityToDTO(entity)).toList();
		return historyDTOList;
	}
	
	//entity -> dto
	public StockHistoryDto EntityToDTO(StockHistoryEntity entity) {
		ModelMapper mapper = new ModelMapper();
		StockHistoryDto dto = mapper.map(entity, StockHistoryDto.class);
		dto.setChild_num(entity.getMember().getMemberNum());
		dto.setStock_num(entity.getStock().getStockNum());
		dto.setStockhist_amount(entity.getStockhistAmount());
		dto.setStockhist_date(new Date(entity.getStockHistDate().getTime()));
		dto.setStockhist_num(entity.getStockhistNum());
		dto.setStockhist_price(entity.getStockhistPrice());
		dto.setStockhist_type(entity.getStockhistType());
		return dto;
	}
}
