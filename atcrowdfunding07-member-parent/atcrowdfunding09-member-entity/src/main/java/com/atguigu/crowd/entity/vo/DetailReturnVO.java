package com.atguigu.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailReturnVO {

    // 回报信息的主键
    private Integer returnId;

    // 支持的当前挡位支持的金额
    private Integer supporterMoney;

    // 单笔限购，取值为0时不限购 取值为一是有限额
    private Integer signalPurchase;

    // 表示具体限额的数量
    private Integer purchase;

    // 当前挡位的支持者数量
    private Integer supporterCount;

    // 取值为零时表示表示包邮
    private Integer freight;

    // 众筹成功后多久发货
    private Integer returnDate;

    // 众筹的内容
    private String content;

}
