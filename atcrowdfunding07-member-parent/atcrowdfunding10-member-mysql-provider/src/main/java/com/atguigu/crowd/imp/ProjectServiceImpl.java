package com.atguigu.crowd.imp;

import com.atguigu.crowd.entity.po.MemberConfirmInfoPO;
import com.atguigu.crowd.entity.po.MemberLaunchInfoPO;
import com.atguigu.crowd.entity.po.ProjectPO;
import com.atguigu.crowd.entity.po.ReturnPO;
import com.atguigu.crowd.entity.vo.*;
import com.atguigu.crowd.mapper.*;
import com.atguigu.crowd.servcie.ProjectService;
import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Transactional(readOnly = true)
@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectPOMapper projectPOMapper;

    @Autowired
    private ProjectItemPicPOMapper projectItemPicPOMapper;

    @Autowired
    private MemberLaunchInfoPOMapper memberLaunchInfoPOMapper;

    @Autowired
    private ReturnPOMapper returnPOMapper;

    @Autowired
    private MemberConfirmInfoPOMapper memberConfirmInfoPOMapper;

    @SneakyThrows
    @Override
    public DetailProjectVO getDetailProjectVO(Integer projectId) {


        // 获得detailProjectVO对象
        DetailProjectVO detailProjectVO = projectPOMapper.selectDetailProjectVO(projectId);

        Integer status = detailProjectVO.getStatus();

        // 设置statusText
        switch (status) {
            case 0:
                detailProjectVO.setStatusText("审核中");
                break;
            case 1:
                detailProjectVO.setStatusText("众筹中");
                break;
            case 2:
                detailProjectVO.setStatusText("众筹成功");
                break;
            case 3:
                detailProjectVO.setStatusText("众筹失败，已关闭");
                break;
        }

        // 设置众筹截止时间lastDay
        // 格式2010-20-11
        String deployDate = detailProjectVO.getDeployDate();


        // 计算当前日期
        Date currentDate = new Date();

        // 将众筹日期解析为Date类型
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date deployDay = format.parse(deployDate);

        // 获取当前日期的时时间戳
        long currentTimeStamp = currentDate.getTime();

        // 获取众筹日期的时间戳
        long deployTimeStamp = deployDay.getTime();

        // 两个时间相减得到当前已经开始了得时间日期
        Long passDays = (currentTimeStamp - deployTimeStamp) / 1000 / 60 / 60 / 24;


        // 得到众筹的总时间来得到当前还剩余的时
        Integer totalCrowdDays = detailProjectVO.getDay();
        Integer leaveTime = Math.toIntExact(totalCrowdDays - passDays);

        detailProjectVO.setLastDay(leaveTime);

        // 返回处理好得对象
        return detailProjectVO;
    }

    @Override
    public List<PortalTypeVO> getPortalTypeVO() {
        return projectPOMapper.selectPortalTypeVOList();
    }

    @Transactional(readOnly = false,propagation = Propagation.REQUIRES_NEW,rollbackFor = Exception.class)
    @Override
    public void saveProject(ProjectVO proVO, Integer memberId) {


        // 第一部分保存projectPO
        // 1.保存ProjectPO对象
        // 创建一个ProjectPO得空对象
        ProjectPO projectPO = new ProjectPO();

        // 2.复制属性把ProjectVO的属性中复制到ProjectVO中
        BeanUtils.copyProperties(proVO,projectPO);


        // 设置成员id
        projectPO.setMemberid(memberId);

        // 设置一下创建时间
        String createDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        projectPO.setCreatedate(createDate);

        // 设置status为0
        projectPO.setStatus(0);  // 表示即将开始

        // 3.保存ProjectPO
        // 为了获得projectPO保存后获得的主键，需要修改相应的配置文件
        //  <insert 设置以下属性可以存入的实体获得自增的id ，useGeneratedKeys="true" keyProperty="id"
        projectPOMapper.insertSelective(projectPO);

        // 4.可以从projectPO中获得自增的主键
        Integer projectPOId = projectPO.getId();

        // 二.保存项目分类的信息关联信息
        List<Integer> typeIdList = proVO.getTypeIdList();

        projectPOMapper.insertTypeRelationship(typeIdList, projectPOId);

        // 三.保存项目的标签的关联信息
        List<Integer> tagIdList = proVO.getTagIdList();

        projectPOMapper.insertTagRelationship(tagIdList, projectPOId);

        // 四.保存项目详情图片的路径信息
        List<String> detailPicturePathList = proVO.getDetailPicturePathList();

        projectItemPicPOMapper.insertPathList(projectPOId,detailPicturePathList);

        // 五.保存项目发起人信息
        MemberLaunchInfoVo memberLaunchInfoVO = proVO.getMemberLaunchInfoVO();

        MemberLaunchInfoPO memberLaunchInfoPO = new MemberLaunchInfoPO();

        BeanUtils.copyProperties(memberLaunchInfoVO,memberLaunchInfoPO);

        memberLaunchInfoPO.setMemberid(memberId);

        memberLaunchInfoPOMapper.insert(memberLaunchInfoPO);

        // 六.保存项目的回报信息
        List<ReturnVO> returnVOList = proVO.getReturnVOList();

        List<ReturnPO> returnPOList = new ArrayList<>();

        for (ReturnVO returnVO : returnVOList) {

            ReturnPO returnPO = new ReturnPO();

            BeanUtils.copyProperties(returnVO, returnPO);

            returnPOList.add(returnPO);
        }


        returnPOMapper.insertReturnBatch(returnPOList,projectPOId);

        // 七、保存项目的确认信息
        MemberConfirmInfoVO memberConfirmInfoVO = proVO.getMemberConfirmInfoVO();

        MemberConfirmInfoPO memberConfirmInfoPO = new MemberConfirmInfoPO();

        BeanUtils.copyProperties(memberConfirmInfoVO,memberConfirmInfoPO);


        memberConfirmInfoPO.setMemberid(memberId);

        memberConfirmInfoPOMapper.insert(memberConfirmInfoPO);



    }
}
