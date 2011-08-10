package com.mpages.hbmloc;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mpages.domain.SimpleGift;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:context.xml")
public class DbTest {
	@Autowired
	SessionFactory f;
	
	@Test
	public void test() throws Exception
	{
		Session session = f.openSession();
		session.enableFilter("locale").setParameter("locale", "en");
		SimpleGift g = new SimpleGift();
		g.setLocale("ua");
	
		session.save(g);
		session.flush();
		Assert.assertEquals(0, session.createCriteria(SimpleGift.class).list().size());
	}
}
