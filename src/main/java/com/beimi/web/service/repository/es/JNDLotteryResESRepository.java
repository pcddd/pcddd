package com.beimi.web.service.repository.es;

import com.beimi.web.model.Lottery;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;

public abstract interface JNDLotteryResESRepository extends ElasticsearchCrudRepository<Lottery, String>{

  public abstract Lottery findById(String paramString);

}
