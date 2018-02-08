package com.beimi.web.service.repository.es;

import com.beimi.web.model.Lottery;
import com.beimi.web.model.PlayUserClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;

public abstract interface LotteryResESRepository extends ElasticsearchCrudRepository<Lottery, String>{

  public abstract Lottery findById(String paramString);

  public abstract Lottery findByPreno(String preno);
}
