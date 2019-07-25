package com.tysoft.workflow;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;


import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bstek.uflo.console.provider.ProcessFile;
import com.bstek.uflo.console.provider.ProcessProvider;
import com.tysoft.workflow.entity.DbFlow;



@Component
public class DbFileProcessProvider implements ProcessProvider{
	
	public String prefix="db:";
	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public InputStream loadProcess(String file) {
		if(file.startsWith(prefix)){
			file=file.substring(prefix.length(),file.length());
		}
		
		Session session=sessionFactory.openSession();

		try {
			String hql="from "+DbFlow.class.getName()+" where filename=:filename";
			Query query=session.createQuery(hql);
			query.setString("filename", file);
			DbFlow dbFlow=(DbFlow)query.uniqueResult();
			InputStream is = new ByteArrayInputStream(dbFlow.getContent().getBytes("utf-8"));
			return  is;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}finally {
			session.close();
		}
	}

	@Override
	public List<ProcessFile> loadAllProcesses() {
		 
		List<ProcessFile> list=new ArrayList<ProcessFile>();
		Session session=sessionFactory.openSession();

		try {
			String hql="from "+DbFlow.class.getName()+" order by sdatetime desc";
			Query query=session.createQuery(hql);
			List<DbFlow> flows=query.list();
			
			for(DbFlow f:flows){
				Calendar calendar=Calendar.getInstance();
				calendar.setTimeInMillis(f.getSdatetime());
				list.add(new ProcessFile(f.getFilename(),calendar.getTime()));
			}
		
		} catch (Exception e) {
			throw new RuntimeException(e);
		}finally {
			session.close();
		}
		

//		Collections.sort(list, new Comparator<ProcessFile>(){
//			@Override
//			public int compare(ProcessFile f1, ProcessFile f2) {
//				return f2.getUpdateDate().compareTo(f1.getUpdateDate());
//			}
//		});
		return list;
	}

	@Override
	public void saveProcess(String fileName, String content) {
		if(fileName.startsWith(prefix)){
			fileName=fileName.substring(prefix.length(),fileName.length());
		}
		
		Session session=sessionFactory.openSession();

		try{
			String hql="from "+DbFlow.class.getName()+" where filename='"+fileName+"'";
			Query query=session.createQuery(hql);
			//query.setString("filename", fileName);
			DbFlow dbFlow=(DbFlow)query.uniqueResult();
		
			if(dbFlow==null) {
				dbFlow=new DbFlow();
				dbFlow.setContent(content);
				dbFlow.setFilename(fileName);
				Calendar calendar=Calendar.getInstance();
				dbFlow.setSdatetime(calendar.getTimeInMillis());
				String uuid=UUID.randomUUID().toString();
				dbFlow.setId(uuid);
				session.save(dbFlow);
			}else {
				dbFlow.setContent(content);
				dbFlow.setFilename(fileName);
				Calendar calendar=Calendar.getInstance();
				dbFlow.setSdatetime(calendar.getTimeInMillis());
				session.saveOrUpdate(dbFlow);
			}

			
		}catch(Exception ex){
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}finally{
			session.flush();
			session.close();
		}
	}

	@Override
	public void deleteProcess(String fileName) {
		if(fileName.startsWith(prefix)){
			fileName=fileName.substring(prefix.length(),fileName.length());
		}
		
		Session session=sessionFactory.openSession();

		try {
			String hql="from "+DbFlow.class.getName()+" where filename=:filename";
			Query query=session.createQuery(hql);
			query.setString("filename", fileName);
			DbFlow dbFlow=(DbFlow)query.uniqueResult();
			session.delete(dbFlow);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}finally {
			session.flush();
			session.close();
		}
	}

	@Override
	public String getName() {
		return "数据库存储";
	}

	@Override
	public String getPrefix() {
		return prefix;
	}

	@Override
	public boolean support(String fileName) {
		return fileName.startsWith(prefix);
	}

	@Override
	public boolean isDisabled() {
		return false;
	}

}
