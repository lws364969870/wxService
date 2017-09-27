package com.lws.domain.service;

import com.lws.domain.dao.SyconfigDAO;
import com.lws.domain.dao.WordbookDAO;
import com.lws.domain.entity.Syconfig;
import com.lws.domain.entity.Wordbook;
import com.lws.domain.model.Page;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class SyconfigService
{

  @Resource
  SyconfigDAO syconfigDAO;

  @Resource
  WordbookDAO wordbookDAO;

  public void save(Syconfig syconfig)
    throws Exception
  {
    this.syconfigDAO.save(syconfig);
  }

  public Syconfig findById(Long id) throws Exception {
    return this.syconfigDAO.findById(id);
  }

  public Page queryForPageCondition(Syconfig syconfig, int pageNo, int pageSize) throws Exception {
    Page page = new Page();

    int allRow = this.syconfigDAO.queryAllCounts(syconfig);

    int offset = page.countOffset(pageNo, pageSize);

    List list = this.syconfigDAO.queryForPageCondition(syconfig, offset, pageSize);

    page.setPageNo(pageNo);
    page.setPageSize(pageSize);
    page.setTotalRecords(allRow);
    page.setList(list);
    return page;
  }

  public String getValueByKey(String key) throws Exception {
    return this.syconfigDAO.getValueByKey(key);
  }

  public List queryForCondition(Syconfig syconfig) throws Exception {
    return this.syconfigDAO.queryForCondition(syconfig);
  }

  public int updateByNetWork(String key, String value) throws Exception {
    return this.syconfigDAO.updateByNetWork(key, value);
  }

  public List queryForWordBookCondition(Wordbook wordbook) throws Exception {
    return this.wordbookDAO.queryForCondition(wordbook);
  }
}