package com.huajie.unittest;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.hjedu.course.dao.ILessonDAO;
import com.hjedu.course.vo.LessonVO;
import com.huajie.util.SpringHelper;

public class LessonDAOTest {
	private static ILessonDAO dao = SpringHelper.getSpringBean("LessonDAO");
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testFindLessonVO() {
		List<LessonVO> lvs = dao.findLessonVO(0, 10);
		lvs.size();
	}

}
