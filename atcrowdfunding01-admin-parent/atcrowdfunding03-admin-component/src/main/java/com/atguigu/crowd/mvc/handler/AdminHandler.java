package com.atguigu.crowd.mvc.handler;

import com.atguigu.crowd.entity.Admin;
import com.atguigu.crowd.exception.LoginAcctAlreadyInUseException;
import com.atguigu.crowd.service.api.AdminService;
import com.atguigu.crowd.util.CrowdConstant;
import com.atguigu.crowd.util.CrowdUtil;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jmx.export.naming.IdentityNamingStrategy;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.print.DocFlavor;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class AdminHandler {
    private Logger logger = LoggerFactory.getLogger(AdminHandler.class);
    @Autowired
    private AdminService adminService;
    @RequestMapping("/admin/update.html")
    public String update(
            Admin admin,
            @RequestParam("pageNum") Integer pageNum,
            @RequestParam("keyword") String keyword
    ){
        adminService.updata(admin);

        return "redirect:/admin/get/page.html?pageNum="+pageNum+"&keyword="+keyword;
    }
    @RequestMapping("/admin/to/edit/page.html")
    public String toEditPage(
            @RequestParam("adminId") Integer adminId,
            ModelMap modelMap
            ){
        Admin admin = adminService.getAdminById(adminId);
        modelMap.addAttribute("admin", admin);

        return "admin-edit";
    }
    @PreAuthorize("hasAuthority('user:save')")
    @RequestMapping("/admin/save.html")
    public String save(Admin admin){
        // 1.密码的加密
        String userPswd = admin.getUserPswd();
        userPswd = CrowdUtil.md5(userPswd);

        admin.setUserPswd(userPswd);
        // 2.生成一创建时间
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String CreateTime = format.format(date);

        admin.setCreateTime(CreateTime);

        try {
            adminService.saveAdmin(admin);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("异常全类名" + e.getClass().getName());
            if(e instanceof DuplicateKeyException){
                throw new LoginAcctAlreadyInUseException("用户名重复了");
            }
        }

        return "redirect:/admin/get/page.html?pageNum="+Integer.MAX_VALUE;
    }
    @RequestMapping("/admin/remove/{adminId}/{pageNum}/{keyword}.html")
    public String remove(
            @PathVariable("adminId")Integer adminId,
            @PathVariable("pageNum")Integer pageNum,
            @PathVariable("keyword")String  keyword
    ){
        // 执行删除
        adminService.remove(adminId);

        // 页面跳转:回到分页页面

        // 尝试方案1:直接转发到admin-page.jsp会无法显示分页数据
        // return "admin-page"

        // 尝试方案2:转发到/admin/get/page.html,一旦刷新页面会重复执行删除浪费性能
        // return "forward:/admin/get/page.html";

        // 尝试方案3:重定向到/admin/get/page.html
        // 同时为了保持原本的查询关键词，再附加到pageNum和keyworda两个请求参数

        return "redirect:/admin/get/page.html?pageNum="+pageNum+"&keyword="+keyword ;
    }
    @RequestMapping("/admin/get/page.html")
    public String getPageInfo(
            // 用defaultValue可以在请求没有数据时传入使用默认值
            @RequestParam(value = "keyword",defaultValue = "") String keyword,
            @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize",defaultValue = "5") Integer pageSize,
            ModelMap modelMap
    ){
        PageInfo<Admin> pageInfo = adminService.getPageInfo(keyword, pageNum, pageSize);

        modelMap.addAttribute(CrowdConstant.ATTR_NAME_PAGE_INFO,pageInfo);
        return  "admin-page";
    }
    @RequestMapping("/admin/do/logout.html")
    public String doLogout(HttpSession session){
        //强制session失效
        session.invalidate();

        return "redirect:/admin/to/login/page.html";
    }
    @RequestMapping("/admin/do/login.html")
    public String doLogin(
            @RequestParam("loginAcct") String loginAcct,
            @RequestParam("userPswd") String userPswd,
            HttpSession session
    ){
        // 调用service方法进行登录检查
        // 这个方法如果能够返回admin对象说明登陆成功，如果账户，密码登录不正确则会自动抛出异常
        Admin admin = adminService.getAdminByLoginAcct(loginAcct,userPswd);

        //将登陆成功返回的admin对象存入Session域
        session.setAttribute(CrowdConstant.ATTR_NAME_LOGIN_ADMIN,admin);

        return "redirect:/admin/to/main/page.html";
    }
}
