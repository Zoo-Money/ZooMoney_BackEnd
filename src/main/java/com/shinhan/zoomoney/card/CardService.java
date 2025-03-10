package com.shinhan.zoomoney.card;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CardService {
	
	@Autowired
    private CardRepository cardRepository;

	
    public List<CardEntity> getCardsByMemberNum(Integer memberNum) {
        return cardRepository.findByMemberNum(memberNum);
    }
}
