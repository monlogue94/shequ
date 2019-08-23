package com.wby.Community.elasticsearch;

import com.wby.Community.entity.DiscussPost;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchProperties;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscussPostRepository extends ElasticsearchRepository <DiscussPost,Integer> {

}
