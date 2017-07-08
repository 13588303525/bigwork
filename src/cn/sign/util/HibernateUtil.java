package cn.sign.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class HibernateUtil {
	private static SessionFactory sf=null;
	private static ThreadLocal<Session> threadLocal=new ThreadLocal<Session>();
	public static  Session getSession(){
		Session session=threadLocal.get();
		if(session!=null && session.isOpen()) return session;
		if(sf==null){
			 ApplicationContext context = new ClassPathXmlApplicationContext("classpath:config/beans.xml");
			 sf=context.getBean("sessionFactory", SessionFactory.class);;//得到工厂 
		}
		session= sf.openSession();
		threadLocal.set(session);
		return session;
	}
	public static void closeSession(){
		Session session=threadLocal.get();
		threadLocal.set(null);
		if(session!=null) session.close();
	}
}
