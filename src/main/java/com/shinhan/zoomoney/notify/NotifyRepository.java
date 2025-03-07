package com.shinhan.zoomoney.notify;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NotifyRepository extends JpaRepository<NotifyEntity, Integer> {
    public List<NotifyDto> select(int member_num) {
        throw new UnsupportedOperationException("Unimplemented method 'select'");
    }

    public NotifyDto selectById(int notify_num) {
        throw new UnsupportedOperationException("Unimplemented method 'selectById'");
    }

    public int selectUnread(int member_num) {
        throw new UnsupportedOperationException("Unimplemented method 'selectUnread'");
    }

    public void insert(NotifyDto nofityDTO) {
        throw new UnsupportedOperationException("Unimplemented method 'insert'");
    }

    public void update(int notify_num) {
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }
}
