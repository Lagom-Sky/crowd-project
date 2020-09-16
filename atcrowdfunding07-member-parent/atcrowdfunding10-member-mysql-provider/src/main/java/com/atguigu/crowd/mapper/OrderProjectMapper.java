package com.atguigu.crowd.mapper;

import com.atguigu.crowd.entity.po.OrderProject;
import com.atguigu.crowd.entity.po.OrderProjectExample;
import java.util.List;

import com.atguigu.crowd.entity.vo.OrderProjectVO;
import org.apache.ibatis.annotations.Param;

public interface OrderProjectMapper {
    int countByExample(OrderProjectExample example);

    int deleteByExample(OrderProjectExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(OrderProject record);

    int insertSelective(OrderProject record);

    List<OrderProject> selectByExample(OrderProjectExample example);

    OrderProject selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") OrderProject record, @Param("example") OrderProjectExample example);

    int updateByExample(@Param("record") OrderProject record, @Param("example") OrderProjectExample example);

    int updateByPrimaryKeySelective(OrderProject record);

    int updateByPrimaryKey(OrderProject record);

    OrderProjectVO selectOrderProjectVO(@Param("returnId") Integer returnId);
}