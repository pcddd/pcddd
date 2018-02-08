package com.beimi.web.service.repository.es;

import com.beimi.web.model.Lottery;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;

/**
 * Created by fanling on 2018/2/8.
 */
public abstract interface GameESRepository extends ElasticsearchCrudRepository<Lottery, String> {

}
