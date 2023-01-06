package org.xiaowu.behappy.manager.controller;


import cn.hutool.core.util.StrUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.WebUtils;

import java.util.Map;


public class BaseController {

    //提示信息
    public final static String MESSAGE_SUCCESS = "操作成功！";
    public final static String MESSAGE_FAILURE = "操作失败！";

    /**
     * 成功提示
     *
     * @param message
     * @param redirectAttributes
     */
    protected void successMessage(String message, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", StrUtil.isEmpty(message) ? MESSAGE_SUCCESS : message);
        redirectAttributes.addFlashAttribute("messageType", 1);
    }

    /**
     * 失败提示
     *
     * @param message
     * @param redirectAttributes
     */
    protected void failureMessage(String message, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", StrUtil.isEmpty(message) ? MESSAGE_FAILURE : message);
        redirectAttributes.addFlashAttribute("messageType", 0);
    }

    protected void failureMessage(String message, HttpServletRequest request) {
        request.setAttribute("message", StrUtil.isEmpty(message) ? MESSAGE_SUCCESS : message);
        request.setAttribute("messageType", 0);
    }

    /**
     * 成功页
     *
     * @param message
     * @param request
     */
    protected String successPage(String message, HttpServletRequest request) {
        request.setAttribute("messagePage", StrUtil.isEmpty(message) ? MESSAGE_SUCCESS : message);
        return "common/successPage";
    }

    /**
     * 失败页
     *
     * @param message
     * @param request
     * @return
     */
    protected String failurePage(String message, HttpServletRequest request) {
        request.setAttribute("messagePage", StrUtil.isEmpty(message) ? MESSAGE_FAILURE : message);
        return "common/failurePage";
    }

    protected Map<String, Object> getFilters(HttpServletRequest request) {
        Map<String, Object> filters = WebUtils.getParametersStartingWith(request, "s_");
        return filters;
    }

}
