package com.beimi.web.service.repository.es;

import com.beimi.web.model.Lottery;
import com.beimi.web.model.PlayUserClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;

public abstract interface BjLotteryResESRepository extends ElasticsearchCrudRepository<Lottery, String>{

  public abstract Lottery findById(String paramString);

}
