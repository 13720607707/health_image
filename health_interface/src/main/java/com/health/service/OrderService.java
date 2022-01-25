package com.health.service;

import com.health.entity.Result;

import java.util.Map;

public interface OrderService {
    public Result order(Map map) throws Exception;

   public Map findById(Integer id);
}
