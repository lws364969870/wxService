package com.lws.domain.service;

import com.lws.domain.dao.SubscribeDAO;
import com.lws.domain.entity.Subscribe;
import com.lws.domain.model.Page;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class SubscribeService
{

  @Resource
  SubscribeDAO subscribeDAO;

  public void save(Subscribe subscribe)
    throws Exception
  {
    this.subscribeDAO.save(subscribe);
  }

  public Subscribe findById(Long id) throws Exception {
    return this.subscribeDAO.findById(id);
  }

  public int deleteById(Long id) throws Exception {
    return this.subscribeDAO.deleteById(id);
  }

  public int deleteAll() throws Exception {
    return this.subscribeDAO.deleteAll();
  }

  public Page queryForPageCondition(Subscribe subscribe, int pageNo, int pageSize) throws Exception {
    Page page = new Page();

    int allRow = this.subscribeDAO.queryAllCounts(subscribe);

    int offset = page.countOffset(pageNo, pageSize);

    List list = this.subscribeDAO.queryForPageCondition(subscribe, offset, pageSize);

    page.setPageNo(pageNo);
    page.setPageSize(pageSize);
    page.setTotalRecords(allRow);
    page.setList(list);
    return page;
  }

  public List findSubscribeByOpenid(String openid) throws Exception {
    return this.subscribeDAO.findSubscribeByOpenid(openid);
  }
}