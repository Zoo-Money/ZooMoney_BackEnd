package com.shinhan.zoomoney.notify;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NotifyRepository extends JpaRepository<NotifyEntity, Integer> {
    /*
    public List<NotifyDto> select(int member_num) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'select'");
    }

    public NotifyDto selectById(int notify_num) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'selectById'");
    }

    public int selectUnread(int member_num) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'selectUnread'");
    }

    public void insert(NotifyDto nofityDTO) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'insert'");
    }

    public void update(int notify_num) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }
    */
}
