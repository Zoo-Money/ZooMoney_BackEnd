package com.shinhan.zoomoney.card.entity;

import java.util.Date;

import com.shinhan.zoomoney.account.AccountEntity;
import com.shinhan.zoomoney.moneyplan.entity.CategoryEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "UseHistory")
public class UseHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int usehistNum;

    @ManyToOne
    @JoinColumn(name = "card_num")
    private CardEntity card;

    @ManyToOne
    @JoinColumn(name = "category_num")
    private CategoryEntity category;

    @ManyToOne
    @JoinColumn(name = "account_num")
    private AccountEntity account;

    private int usehistMoney;
    private String usehistShop;
    private String usehistType;
    private Date usehistDate;
}
