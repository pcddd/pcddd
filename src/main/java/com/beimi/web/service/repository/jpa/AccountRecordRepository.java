package com.beimi.web.service.repository.jpa;

import com.beimi.web.model.AccountConfig;
import com.beimi.web.model.AccountRecordModel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public abstract interface AccountRecordRepository extends JpaRepository<AccountRecordModel, String> {
  public abstract List<AccountRecordModel> findByUserid(String userid, Pageable paramPageable);
}
