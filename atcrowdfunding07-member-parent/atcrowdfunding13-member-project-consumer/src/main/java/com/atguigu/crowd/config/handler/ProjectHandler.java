package com.atguigu.crowd.config.handler;

import com.atguigu.crowd.api.MySQLRemoteService;
import com.atguigu.crowd.config.OSSProperties;
import com.atguigu.crowd.entity.vo.*;
import com.atguigu.crowd.util.CrowdConstant;
import com.atguigu.crowd.util.CrowdUtil;
import com.atguigu.crowd.util.ResultEntity;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.bouncycastle.math.raw.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ProjectHandler {

    @Autowired
    private OSSProperties ossProperties;

    @Autowired
    private MySQLRemoteService mySQLRemoteService;

    @RequestMapping("/get/project/detail/{projectId}")
    public String getProjectDetail(
            @PathVariable("projectId") Integer projectId,
            Model model
    ){

        ResultEntity<DetailProjectVO> detailProjectVORemote = mySQLRemoteService.getDetailProjectVORemote(projectId);

        String result = detailProjectVORemote.getResult();

        if (ResultEntity.FAILED.equals(result)) {

            model.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, "远程查询项目详情出现错误");

            return "portal";
        }else {
            model.addAttribute(CrowdConstant.ATTR_NAME_DETAIL_PROJECT, detailProjectVORemote.getData());
            System.out.println(detailProjectVORemote.toString());
        }


        return "project_show_detail";
    }


    @SneakyThrows
    @RequestMapping("/create/project/information")
    public String SaveProjectBasicInfo(

            // 接受除了上传图片外的所有的数据
            ProjectVO projectVO,

            // 接受头图
            MultipartFile headerPicture,

            // 接收上传的详情图片
            List<MultipartFile> detailPictureList,

            // 接受发起人信息

            // 用来收集一部分数据ProjectVO的对象存入Session对象
            HttpSession session,

            // 用来传递出错信息
            ModelMap modelMap
    ) {

        Boolean headPictureIsEmpty = headerPicture.isEmpty();

        if (!headPictureIsEmpty) {
            // 2.如果用户是上传的了不为空的文件，则检查是否上传成功
            ResultEntity<String> uploadHeaderPictureResultEntity = CrowdUtil.uploadFileToOss(
                    ossProperties.getEndPoint(),
                    ossProperties.getAccessKeyId(),
                    ossProperties.getAccessKeySecret(),
                    headerPicture.getInputStream(),
                    ossProperties.getBucketName(),
                    ossProperties.getBucketDomain(),
                    headerPicture.getOriginalFilename());

            String result = uploadHeaderPictureResultEntity.getResult();

            // 判断头图是否是否上传成功
            if (ResultEntity.SUCCESS.equals(result)) {

                // 4.重访问的数据中获得图片的访问路径的网路地址
                String headerPicturePath = uploadHeaderPictureResultEntity.getData();

                // 5.存入到ProjectVO对象中
                projectVO.setHeaderPicturePath(headerPicturePath);

            } else {

                // 如果图片上传失败时给与提示消息，并返回原始页面
                modelMap.addAttribute("message", CrowdConstant.MESSAGE_HEADER_PIC_UPLOAD_FAILED);

                return "start-step-1";
            }
        } else {

            // 如果头图为空直接就返回
            modelMap.addAttribute("message", CrowdConstant.HEAD_PICTURE_IS_NULL);

            return "start-step-1";
        }
        // 创建用来存放详情图片的路径集合

        List<String> mulList = new ArrayList<String>();

        if (detailPictureList == null || detailPictureList.size() == 0) {

            modelMap.addAttribute("message", CrowdConstant.DETAIL_PICTURE_LIST_IS_EMPTY);

            return "start-step-1";
        }

        // 遍历一下详情图片的链接，并上传到oss
        for (MultipartFile file : detailPictureList) {

            // 检查详情图选择的1页面是不是文件大小是空的
            if (file.isEmpty()) {

                // 如果头图的单个文件为空直接就返回
                modelMap.addAttribute("message", CrowdConstant.DETAIL_PICTURE_CONTEXT_IS_EMPTY);

                return "start-step-1";
            }
            ResultEntity<String> uploadDetailPictureResultEntity = CrowdUtil.uploadFileToOss(
                    ossProperties.getEndPoint(),
                    ossProperties.getAccessKeyId(),
                    ossProperties.getAccessKeySecret(),
                    file.getInputStream(),
                    ossProperties.getBucketName(),
                    ossProperties.getBucketDomain(),
                    file.getOriginalFilename()
            );

            String result = uploadDetailPictureResultEntity.getResult();

            if (ResultEntity.SUCCESS.equals(result)) {

                // 取一下图片的访问的路径
                String detailPicturePath = uploadDetailPictureResultEntity.getData();

                // 添加详情图片到ProjectVO
                mulList.add(detailPicturePath);
            } else {
                // 如果头图的单个文件为空直接就返回
                modelMap.addAttribute("message", CrowdConstant.DETAIL_PICTURE_FAILED_TO_UPLOAD);

                return "start-step-1";
            }
        }


        // 遍历完成后将mulList 加入到ProjectVO
        projectVO.setDetailPicturePathList(mulList);

        // 将projectVO存入session域
        session.setAttribute(CrowdConstant.ATTR_TEMPLE_PROJECT, projectVO);

        // 跳转到回报页面收集回报信息的页面
        return "redirect:http://localhost:81/project/member/to/pay/back/page";
    }

    @SneakyThrows
    @ResponseBody
    @RequestMapping("/create/upload/return/picture.json")
    public ResultEntity<String> uploadReturnPictureFile(
             @RequestParam("returnPicture") MultipartFile returnPicture
    ) {

        // 1.执行文件的上传
        Boolean headPictureIsEmpty = returnPicture.isEmpty();

        if (!headPictureIsEmpty) {
            // 2.如果用户是上传的了不为空的文件，则检查是否上传成功
            ResultEntity<String> uploadReturnPictureResultEntity = CrowdUtil.uploadFileToOss(
                    ossProperties.getEndPoint(),
                    ossProperties.getAccessKeyId(),
                    ossProperties.getAccessKeySecret(),
                    returnPicture.getInputStream(),
                    ossProperties.getBucketName(),
                    ossProperties.getBucketDomain(),
                    returnPicture.getOriginalFilename());

            String result = uploadReturnPictureResultEntity.getResult();

            // 判断头图是否是否上传成功
            if (ResultEntity.SUCCESS.equals(result)) {

                // 4.重访问的数据中获得图片的访问路径的网路地址
                String headerPicturePath = uploadReturnPictureResultEntity.getData();

                return ResultEntity.successWithData(headerPicturePath);
            } else {

                // 如果图片上传失败时给与提示消息，并返回原始页面
                return ResultEntity.failed(CrowdConstant.MESSAGE_HEADER_PIC_UPLOAD_FAILED);

            }
        } else {

            // 如果头图为空直接就返回
            return ResultEntity.failed(CrowdConstant.HEAD_PICTURE_IS_NULL.toString());

        }
    }

    @ResponseBody
    @RequestMapping("/create/save/return.json")
    public ResultEntity<ReturnVO> createSaveReturn(
            ReturnVO returnVO,
            HttpSession session
    ) {
        try {
            // 1.从session中获取之前缓存的ProjectVO对象
            ProjectVO proVO = (ProjectVO) session.getAttribute(CrowdConstant.ATTR_TEMPLE_PROJECT);


            // 2.判断projectVO是否为NULL
            if (proVO == null) {
                return ResultEntity.failed(CrowdConstant.MESSAGE_TEMPLE_PROJECT_MISSING);
            }

            // 3.从projectVO中获取存储回报信息的集合
            List<ReturnVO> returnVOList = proVO.getReturnVOList();

            // 4.判断集合是否有效
            if (returnVOList == null || returnVOList.size() == 0) {

                returnVOList = new ArrayList<>();
                proVO.setReturnVOList(returnVOList);
            }

            returnVOList.add(returnVO);

            // 将数据projectVO重新设置回session中
            session.setAttribute(CrowdConstant.ATTR_TEMPLE_PROJECT,proVO);

            return ResultEntity.successWithoutData();
        } catch (Exception e) {

            e.printStackTrace();

            return ResultEntity.failed(e.getMessage());
        }
    }

    @RequestMapping("/create/confirm")
    public String createConfirm(
            MemberConfirmInfoVO memberConfirmInfoVO,
            HttpSession session,
            ModelMap modelMap){

        // 1.从session中获取之前缓存的ProjectVO对象
        ProjectVO proVO = (ProjectVO) session.getAttribute(CrowdConstant.ATTR_TEMPLE_PROJECT);


        // 2.判断projectVO是否为NULL
        if (proVO == null) {
            return "start-step-3";
        }

        proVO.setMemberConfirmInfoVO(memberConfirmInfoVO);

        // 从session域中读取当前登陆的用户的会员Id
        MemberLoginVO attribute = (MemberLoginVO) session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER);

        Integer memberId = attribute.getId();

        // 3.调用远程的方法保存projectVO对象
        ResultEntity<String> saveResultEntity = mySQLRemoteService.saveProjectVORemote(proVO,memberId);


        // 判断远程的返回操作是不是成功的
        String result = saveResultEntity.getResult();

        if (ResultEntity.FAILED.equals(result)) {

            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, "你的众筹项目信息保存失败"+saveResultEntity.getMessage());

            return "start-step-3";
        }
        // 如果数据存储成则没有必要在保留当前session
        session.removeAttribute(CrowdConstant.ATTR_TEMPLE_PROJECT);

        return "redirect:http://localhost:81/project/to/start-step-4";
    }

}
