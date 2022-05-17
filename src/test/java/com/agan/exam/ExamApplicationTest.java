package com.agan.exam;

import com.agan.exam.model.Question;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ExamApplicationTest {
    @Test
    public void contextLoads() {
        LambdaQueryWrapper<Question> qw = new LambdaQueryWrapper<>();

    }

}
