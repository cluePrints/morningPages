package com.mpages.hbmloc;

import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
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
		session.get(SimpleGift.class, 1);
	}
}
