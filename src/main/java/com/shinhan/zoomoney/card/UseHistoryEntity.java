package com.shinhan.zoomoney.card;

import com.shinhan.zoomoney.account.AccountEntity;
import com.shinhan.zoomoney.moneyplan.CategoryEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="UseHistory")
public class UseHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int usehistNum;

    @ManyToOne
    @JoinColumn(name = "card_num")
    private ChildCardEntity child;

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
    private Timestamp usehistTime;
}
