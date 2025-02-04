package com.min.memberapp;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.min.memberapp.mapper.IMemberMapper;

@SpringBootTest
class ApplicationTests {

  @Autowired
  IMemberMapper memberMapper;
	@Test
	void select_member_test() {
	  int memId = 1;
	  Assertions.assertThat(memberMapper.selectMemberById(memId).getMemName()).isEqualTo("Gimblett");
	}

}
