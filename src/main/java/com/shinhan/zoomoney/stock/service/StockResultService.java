package com.shinhan.zoomoney.stock.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shinhan.zoomoney.stock.dto.StockResultDto;
import com.shinhan.zoomoney.stock.entity.StockResultEntity;
import com.shinhan.zoomoney.stock.repository.StockResultRepository;

@Service
public class StockResultService {

	@Autowired
	StockResultRepository resultRepo;

	@Transactional
	public List<StockResultDto> selectAllByMemberNum(Integer memberNum) {
		List<StockResultEntity> resultEntityList = resultRepo.findByMember_MemberNum(memberNum);
		List<StockResultDto> resultDTOList = resultEntityList.stream().map(entity -> entityToDTO(entity)).toList();
		return resultDTOList;
	}

	// entity -> dto
	public StockResultDto entityToDTO(StockResultEntity entity) {
		ModelMapper mapper = new ModelMapper();
		StockResultDto dto = mapper.map(entity, StockResultDto.class);
		dto.setMember_num(entity.getMember().getMemberNum());
		dto.setResult_date(entity.getResultDate());
		dto.setResult_num(entity.getResultNum());
		dto.setResult_rank(entity.getResultRank());
		dto.setResult_rate(entity.getResultRate());
		return dto;
	}
}
