package com.shinhan.zoomoney.card.service;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shinhan.zoomoney.card.entity.CardEntity;
import com.shinhan.zoomoney.card.repository.CardRepository;
import com.shinhan.zoomoney.member.MemberEntity;

@Service
public class CardService {

    @Autowired
    private CardRepository cardRepository;

    public CardEntity getCardsByMemberNum(Integer memberNum) {
        MemberEntity member = MemberEntity.builder().memberNum(memberNum).build();
        return cardRepository.findByMember(member);
    }

    // 카드 정보를 DB에 저장하는 메서드
    public CardEntity createCard(Map<String, Object> cardInfo) {
        String cardNum = (String) cardInfo.get("card_num");
        String cardMetadata = (Integer) cardInfo.get("card_metadata") + "";
        Integer cardMoney = (Integer) cardInfo.get("card_money");
        Integer memberNum = Integer.parseInt((String) cardInfo.get("member_num"));

        // MemberEntity 생성
        MemberEntity member = MemberEntity.builder().memberNum(memberNum).build();

        // 카드 정보 저장
        CardEntity card = new CardEntity();
        card.setCardNum(cardNum);
        card.setCardMetadata(cardMetadata);
        card.setCardMoney(cardMoney);
        card.setMember(member); // MemberEntity와 연결

        return cardRepository.save(card);
    }

    // 카드 금액 변경
    public void change(int memberNum, int amount) {
        MemberEntity member = MemberEntity.builder().memberNum(memberNum).build();
        CardEntity entity = cardRepository.findByMember(member);

        if (entity != null) {
            entity.setCardMoney(entity.getCardMoney() + amount); // 금액 변경
            cardRepository.save(entity);
        }
    }

    public void updateCardDate(Integer memberNum, String cardNum) {
        // memberNum으로 카드 조회
        CardEntity card = cardRepository.findByMemberMemberNumAndCardNum(memberNum, cardNum);
        if (card != null) {
            // cardUpdate 필드를 현재 날짜로 업데이트
            card.setCardUpdate(new Date());
            cardRepository.save(card);
        }
    }

}
