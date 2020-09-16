package com.atguigu.crowd.mvc.config;

import com.atguigu.crowd.exception.LoginAcctAlreadyInUseException;
import com.atguigu.crowd.exception.LoginAcctAlreadyInUseForUpdateException;
import com.atguigu.crowd.exception.LoginFailedException;
import com.atguigu.crowd.util.CrowdConstant;
import com.atguigu.crowd.util.CrowdUtil;
import com.atguigu.crowd.util.ResultEntity;
import com.google.gson.Gson;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.GenericServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static com.atguigu.crowd.util.ResultEntity.failed;

@ControllerAdvice
/**
 * @ControllerAdvice这个注解表示当前类为一个基于注解的异常处理类
 */
public class CrowdExceptionResoler {

    //重复代码太多提取一下
    private ModelAndView commonResolve(String viewName,Exception exception,HttpServletResponse httpServletResponse,HttpServletRequest httpServletRequest) throws IOException {
        //获取异常的消息
        String message = exception.getMessage();
        // 1.判断当前的请求类型时Ajax还是普通的页面的请求
        boolean judgeRequestType = CrowdUtil.judgeRequestType(httpServletRequest);

        //如果是Ajax请求
        if (judgeRequestType) {
            //3.创建ResultEntity对象返回信息
            ResultEntity resultEntity = ResultEntity.failed(message);

            //4.将对象转为Gson对象
            Gson gson = new Gson();
            //5.将resultEntity转为json字符串
            String json = gson.toJson(resultEntity);

            //6.将json字符串作为响应体返回给浏览器
            httpServletResponse.getWriter().write(json);

            //7.已经通过原生的response对象对请求做了响应，所以不提供ModelAndView对象
            return null;
        }//如果不是Ajax请求则创建ModelAndView
        else {
            //创建对象
            ModelAndView modelAndView = new ModelAndView();
            //将Exception对象存入模型
            modelAndView.addObject(CrowdConstant.ATTR_NAME_EXCEPTION, exception);
            //是指对应的视图名称
            modelAndView.setViewName(viewName);
            return modelAndView;
        }
    }
    /**
     * 将具体的异常类型和处理函数相互绑定
     *
     * @param
     * @return
     */
    @ExceptionHandler(value = LoginFailedException.class)
    public ModelAndView resolveNullPointException(
            //实际捕获的异常类型                           //请求的对象
            LoginFailedException loginFailedException,
            HttpServletRequest httpServletRequest,
            //响应对象
            HttpServletResponse httpServletResponse) throws IOException {
        String viewName = "admin-login";
        return commonResolve(viewName,loginFailedException,httpServletResponse,httpServletRequest);
    }
    @ExceptionHandler(value = ArithmeticException.class)
    public ModelAndView resolveMathException(
            //实际捕获的异常类型                           //请求的对象
            ArithmeticException arithmeticException,
            HttpServletRequest httpServletRequest,
            //响应对象
            HttpServletResponse httpServletResponse) throws IOException {
        String viewName = "system-error";
        return commonResolve(viewName,arithmeticException,httpServletResponse,httpServletRequest);
    }
    @ExceptionHandler(value = Exception.class)
    public ModelAndView Exception(
            //实际捕获的异常类型                           //请求的对象
            Exception ception,
            HttpServletRequest httpServletRequest,
            //响应对象
            HttpServletResponse httpServletResponse) throws IOException {
        String viewName = "system-error";
        return commonResolve(viewName,ception,httpServletResponse,httpServletRequest);
    }
    @ExceptionHandler(value = LoginAcctAlreadyInUseException.class)
    public ModelAndView resolveLoginAcctAlreadyInUseException(
            //实际捕获的异常类型                           //请求的对象
            LoginAcctAlreadyInUseException loginAcctAlreadyInUseException,
            HttpServletRequest httpServletRequest,
            //响应对象
            HttpServletResponse httpServletResponse) throws IOException {
        String viewName = "admin-add";
        return commonResolve(viewName,loginAcctAlreadyInUseException,httpServletResponse,httpServletRequest);
    }
    @ExceptionHandler(value = LoginAcctAlreadyInUseForUpdateException.class)
    public ModelAndView resolveLoginAcctAlreadyInUseForUpdateException(
            //实际捕获的异常类型                           //请求的对象
            LoginAcctAlreadyInUseForUpdateException loginAcctAlreadyInUseForUpdateException,
            HttpServletRequest httpServletRequest,
            //响应对象
            HttpServletResponse httpServletResponse) throws IOException {
        String viewName = "admin-edit";
        return commonResolve(viewName,loginAcctAlreadyInUseForUpdateException,httpServletResponse,httpServletRequest);
    }


}
