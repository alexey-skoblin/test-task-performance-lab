package com.alexey.skoblin;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class Task1Test {

    private static final Logger log = LoggerFactory.getLogger(Task1Test.class);

    @Test
    public void testErrorMsgNoArgs() {
        try {
            String[] args = {};
            new Task1.CircleList(args);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals(Task1.CircleList.ERROR_MSG_NO_ARGS, e.getMessage());
        }
    }

    @Test
    public void testErrorMsgInvalidArgsCount() {
        try {
            String[] args = {"1"};
            new Task1.CircleList(args);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals(Task1.CircleList.ERROR_MSG_INVALID_ARGS_COUNT, e.getMessage());
        }
    }

    @Test
    public void testErrorMsgInvalidNumberFormat() {
        try {
            String[] args = {"1", "2s"};
            new Task1.CircleList(args);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals(Task1.CircleList.ERROR_MSG_INVALID_NUMBER_FORMAT, e.getMessage());
        }
    }

    @Test
    public void testErrorMsgInvalidNumberValue() {
        try {
            String[] args = {"-1", "2"};
            new Task1.CircleList(args);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals(Task1.CircleList.ERROR_MSG_INVALID_NUMBER_VALUE, e.getMessage());
        }
    }

    @Test
    public void testCorrectArgs() {
        String[] args = {"1", "2"};
        Task1.CircleList circleList = new Task1.CircleList(args);
        assertEquals(1, circleList.getN());
        assertEquals(2, circleList.getM());
    }

    @Test
    public void testCorrectArgs_2() {
        String[] args = {"15", "4"};
        Task1.CircleList circleList = new Task1.CircleList(args);
        assertEquals(15, circleList.getN());
        assertEquals(4, circleList.getM());
    }

    @Test
    public void testCorrectList() {
        String[] args = {"4", "3"};
        Task1.CircleList circleList = new Task1.CircleList(args);
        for (int i = 0; i < circleList.getN(); i++) {
            assertEquals(Integer.valueOf(i + 1), circleList.getElement(i));
        }
    }

    @Test
    public void testCircularPath() {
        String[] args = {"4", "3"};
        Task1.CircleList circleList = new Task1.CircleList(args);
        List<Integer> list = circleList.findCircularPathAsList();
        assertEquals(list, List.of(1, 3));
    }

    @Test
    public void testCircularPath2() {
        String[] args = {"5", "4"};
        Task1.CircleList circleList = new Task1.CircleList(args);
        List<Integer> list = circleList.findCircularPathAsList();
        assertEquals(list, List.of(1, 4, 2, 5, 3));
    }

    @Test
    public void testRun() {
        String[] args = {"10", "3"};
        Task1.CircleList circleList = new Task1.CircleList(args);
        List<Integer> list = circleList.findCircularPathAsList();
        Assert.assertEquals(list, List.of(1,3, 5, 7, 9));
    }

    @Test
    public void testRun2() {
        String[] args = {"10", "1"};
        Task1.CircleList circleList = new Task1.CircleList(args);
        List<Integer> list = circleList.findCircularPathAsList();
        Assert.assertEquals(list.size(), 1);
    }

    @Test
    public void testRun3() {
        String[] args = {"20", "2"};
        Task1.CircleList circleList = new Task1.CircleList(args);
        List<Integer> list = circleList.findCircularPathAsList();
        Assert.assertEquals(list.size(), 20);
    }

    @Test
    public void testRun4() {
        String[] args = {"20", "21"};
        Task1.CircleList circleList = new Task1.CircleList(args);
        List<Integer> list = circleList.findCircularPathAsList();
        Assert.assertEquals(list.size(), 1);
    }

}