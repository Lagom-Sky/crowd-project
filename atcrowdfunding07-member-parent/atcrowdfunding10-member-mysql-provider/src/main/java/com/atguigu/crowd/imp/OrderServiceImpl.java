package com.atguigu.crowd.imp;


import com.atguigu.crowd.entity.po.Address;
import com.atguigu.crowd.entity.po.AddressExample;
import com.atguigu.crowd.entity.po.OrderPO;
import com.atguigu.crowd.entity.po.OrderProject;
import com.atguigu.crowd.entity.vo.AddressVO;
import com.atguigu.crowd.entity.vo.OrderProjectVO;
import com.atguigu.crowd.entity.vo.OrderVO;
import com.atguigu.crowd.mapper.AddressMapper;
import com.atguigu.crowd.mapper.OrderPOMapper;
import com.atguigu.crowd.mapper.OrderProjectMapper;
import com.atguigu.crowd.servcie.OrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {


    @Autowired
    private OrderProjectMapper orderProjectMapper;

    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private OrderPOMapper orderPOMapper;

    @Transactional(readOnly = false,propagation = Propagation.REQUIRES_NEW,rollbackFor = Exception.class)
    @Override
    public void saveOrderVO(OrderVO orderVO) {
        OrderPO orderPO = new OrderPO();

        BeanUtils.copyProperties(orderVO, orderPO);

        OrderProject orderProject = new OrderProject();

        BeanUtils.copyProperties(orderVO.getOrderProjectVO(), orderProject);

        orderPOMapper.insert(orderPO);

        Integer id = orderPO.getId();

        orderProject.setOrderId(id);

        orderProjectMapper.insert(orderProject);

    }

    @Transactional(readOnly = false,propagation = Propagation.REQUIRES_NEW,rollbackFor = Exception.class)
    @Override
    public void saveAddress(AddressVO addressVO) {

        Address address = new Address();

        BeanUtils.copyProperties(addressVO,address);

        addressMapper.insert(address);
    }

    @Override
    public List<AddressVO> getAdderssVO(Integer memberId) {

        AddressExample example = new AddressExample();

        example.createCriteria().andMemberIdEqualTo(memberId);

        List<Address> addressList = addressMapper.selectByExample(example);

        List<AddressVO> addressVOList = new ArrayList<>();

        for (Address address : addressList) {

            AddressVO addressVO =  new AddressVO();

            BeanUtils.copyProperties(address, addressVO);

            addressVOList.add(addressVO);
        }

        return addressVOList;
    }

    @Override
    public OrderProjectVO getOrderProjectVO(Integer projectId, Integer returnId) {

        OrderProjectVO orderProjectVO = orderProjectMapper.selectOrderProjectVO(returnId);

        return orderProjectVO;
    }
}
