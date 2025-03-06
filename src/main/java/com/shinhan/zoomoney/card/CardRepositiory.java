package com.shinhan.zoomoney.card;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepositiory extends JpaRepository<CardEntity, String> {
}
