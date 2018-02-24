package com.beimi.web.service.repository.es;

import com.beimi.web.model.BetGameDetail;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;

import java.util.List;

/**
 * Created by fanling on 2018/2/9.
 */
public abstract interface BetGameDetailESRepository extends ElasticsearchCrudRepository<BetGameDetail, String> {
    public abstract List<BetGameDetail> findByTypeAndPeriods(int type,int periods);

}
