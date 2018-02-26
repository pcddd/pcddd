package com.beimi.web.service.repository.jpa;

import com.beimi.web.model.HxConfig;
import com.beimi.web.model.PcddPeriods;
import org.springframework.data.jpa.repository.JpaRepository;

public abstract interface PcddPeriodsRepository extends JpaRepository<PcddPeriods, String>{

  public abstract PcddPeriods findByPeriods(int periods);

}
