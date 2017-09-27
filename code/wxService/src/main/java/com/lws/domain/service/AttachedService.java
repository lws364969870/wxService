package com.lws.domain.service;

import com.lws.domain.dao.AttachedDAO;
import com.lws.domain.entity.Attached;
import java.io.File;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class AttachedService
{

  @Resource
  AttachedDAO attachedDAO;

  public void save(Attached attached)
    throws Exception
  {
    this.attachedDAO.save(attached);
  }

  public Attached findById(Long id) throws Exception {
    return this.attachedDAO.findById(id);
  }

  public List findByAttachedId(Long articleId) throws Exception {
    Attached attached = new Attached();
    attached.setArticleId(articleId);
    return this.attachedDAO.queryForCondition(attached);
  }

  public int deleteById(Long id)
    throws Exception
  {
    Attached attached = findById(id);
    if ((attached != null) && (attached.getAbsoluteURL() != null)) {
      File file = new File(attached.getAbsoluteURL());
      if (file.exists()) {
        file.delete();
      }
    }
    return this.attachedDAO.deleteById(id);
  }

  public List queryNullArticleIdList() throws Exception {
    return this.attachedDAO.queryNullArticleIdList();
  }

  public List queryForCondition(Attached attached) throws Exception {
    return this.attachedDAO.queryForCondition(attached);
  }

  public int updateArticleIdByNetwordURL(Long articleId, String networdURL)
  {
    return this.attachedDAO.updateArticleIdByNetwordURL(articleId, networdURL);
  }

  public int updateArticleIdByWechatURL(Long articleId, String wechatURL)
  {
    return this.attachedDAO.updateArticleIdByWechatURL(articleId, wechatURL);
  }
}