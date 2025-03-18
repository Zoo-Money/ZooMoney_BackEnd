package com.shinhan.zoomoney.stock;

import java.sql.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StockResultService {
	
	@Autowired
	StockResultRepository resultRepo;
	
	@Transactional
	public List<StockResultDto> selectAllByMemberNum(Integer memberNum){
		List<StockResultEntity> resultEntityList = resultRepo.findByMember_MemberNum(memberNum);
		System.out.println("주식결과>"+resultEntityList);
		List<StockResultDto> resultDTOList = resultEntityList.stream().map(entity->entityToDTO(entity)).toList();
		return resultDTOList;
	}
	
	//entity -> dto
	public StockResultDto entityToDTO(StockResultEntity entity) {
		ModelMapper mapper = new ModelMapper();
		StockResultDto dto = mapper.map(entity, StockResultDto.class);
		dto.setChild_num(entity.getMember().getMemberNum());
		dto.setResult_date(new Date(entity.getResultDate().getTime()));
		dto.setResult_num(entity.getResultNum());
		dto.setResult_rank(entity.getResultRank());
		dto.setResult_rate(entity.getResultRate());
		return dto;
	}
}
