package com.thoughtworks.capability.gtb.entrancequiz.api;

import com.thoughtworks.capability.gtb.entrancequiz.domain.Group;
import com.thoughtworks.capability.gtb.entrancequiz.domain.Student;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wzw on 2020/9/7.
 */
@RestController
public class ServerApi {
    private List<Student> studentList = new ArrayList<>();
    private List<Group> groupList = new ArrayList<>();
    public ServerApi(){

    }
}
