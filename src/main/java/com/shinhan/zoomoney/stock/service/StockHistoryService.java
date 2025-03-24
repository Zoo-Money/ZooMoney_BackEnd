package com.shinhan.zoomoney.stock.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shinhan.zoomoney.stock.dto.StockHistoryBackupDto;
import com.shinhan.zoomoney.stock.entity.StockHistoryBackupEntity;
import com.shinhan.zoomoney.stock.repository.StockHistoryBackupRepository;

@Service
public class StockHistoryService {

	@Autowired
	StockHistoryBackupRepository stockHisBackUpRepo;
	
	// 시즌별 거래내역조회
	public List<StockHistoryBackupDto> selectStockHitory(Integer memberNum) {
		List<StockHistoryBackupEntity> entityList = stockHisBackUpRepo.findByMember_MemberNum(memberNum);
        return entityList.stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
	}

	// entity -> dto
	private StockHistoryBackupDto entityToDto(StockHistoryBackupEntity entity) {
        return StockHistoryBackupDto.builder()
                .stockhist_num(entity.getStockhistNum())
                .child_num(entity.getMember().getMemberNum())
                .stock_num(entity.getStockNum().getStockNum())
                .stock_name(entity.getStockNum().getStockName())
                .stockhist_type(entity.getStockhistType())
                .stockhist_amount(entity.getStockhistAmount())
                .stockhist_price(entity.getStockhistPrice())
                .stockhist_date(new java.sql.Date(entity.getStockhistDate().getTime())) // util.Date -> sql.Date 변환
                .build();
    }
}
