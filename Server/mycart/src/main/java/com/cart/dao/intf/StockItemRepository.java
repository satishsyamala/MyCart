package com.cart.dao.intf;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cart.pojo.StockItems;

public interface StockItemRepository extends JpaRepository<StockItems, Long>  {

}
