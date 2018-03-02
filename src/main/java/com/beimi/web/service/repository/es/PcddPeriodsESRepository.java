package com.beimi.web.service.repository.es;

import com.beimi.web.model.PcddPeriods;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;

import java.util.List;

public abstract interface PcddPeriodsESRepository extends ElasticsearchCrudRepository<PcddPeriods, String>{

  public abstract List<PcddPeriods> findByTypeAndPeriods(int type,int periods);

  public abstract List<PcddPeriods> findByType(int type, Sort sort);

  public abstract List<PcddPeriods> findByType(int type,Pageable page);
}
