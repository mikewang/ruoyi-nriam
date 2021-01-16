package com.ruoyi.web.controller.audit;

import com.ruoyi.audit.domain.AudMessage;
import com.ruoyi.audit.service.AudMessageService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.framework.config.ServerConfig;
import com.ruoyi.framework.web.service.TokenService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

@RestController
@RequestMapping("/audit/message")
public class AudMessageController extends BaseController
{
    @Resource
    private AudMessageService audMessageService;


    @Resource
    private TokenService tokenService;

    @Resource
    private ServerConfig serverConfig;

    @PreAuthorize("@ss.hasPermi('audit:message:list')")
    @GetMapping("/list")
    public TableDataInfo list(AudMessage audMessage)
    {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        Long userId = loginUser.getUser().getUserId();

        startPage();
        List<AudMessage> list = audMessageService.selectByUserId(87);
        List<AudMessage> list2 = new List<AudMessage>() {
            @Override
            public int size() {
                return 0;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public boolean contains(Object o) {
                return false;
            }

            @Override
            public Iterator<AudMessage> iterator() {
                return null;
            }

            @Override
            public Object[] toArray() {
                return new Object[0];
            }

            @Override
            public <T> T[] toArray(T[] a) {
                return null;
            }

            @Override
            public boolean add(AudMessage audMessage) {
                return false;
            }

            @Override
            public boolean remove(Object o) {
                return false;
            }

            @Override
            public boolean containsAll(Collection<?> c) {
                return false;
            }

            @Override
            public boolean addAll(Collection<? extends AudMessage> c) {
                return false;
            }

            @Override
            public boolean addAll(int index, Collection<? extends AudMessage> c) {
                return false;
            }

            @Override
            public boolean removeAll(Collection<?> c) {
                return false;
            }

            @Override
            public boolean retainAll(Collection<?> c) {
                return false;
            }

            @Override
            public void clear() {

            }

            @Override
            public AudMessage get(int index) {
                return null;
            }

            @Override
            public AudMessage set(int index, AudMessage element) {
                return null;
            }

            @Override
            public void add(int index, AudMessage element) {

            }

            @Override
            public AudMessage remove(int index) {
                return null;
            }

            @Override
            public int indexOf(Object o) {
                return 0;
            }

            @Override
            public int lastIndexOf(Object o) {
                return 0;
            }

            @Override
            public ListIterator<AudMessage> listIterator() {
                return null;
            }

            @Override
            public ListIterator<AudMessage> listIterator(int index) {
                return null;
            }

            @Override
            public List<AudMessage> subList(int fromIndex, int toIndex) {
                return null;
            }
        };

        return getDataTable(list);
    }


}