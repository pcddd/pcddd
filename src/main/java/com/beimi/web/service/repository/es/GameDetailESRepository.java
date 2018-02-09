package com.beimi.web.service.repository.es;

import com.beimi.web.model.GameDetail;
import com.beimi.web.model.PlayUser;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;

import java.util.List;

/**
 * Created by fanling on 2018/2/9.
 */
public abstract interface GameDetailESRepository extends ElasticsearchCrudRepository<GameDetail, String> {
    public abstract List<GameDetail> findByPeriods(String periods);

}
