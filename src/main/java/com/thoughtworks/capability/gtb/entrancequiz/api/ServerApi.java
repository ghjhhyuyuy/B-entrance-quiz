package com.thoughtworks.capability.gtb.entrancequiz.api;

import com.thoughtworks.capability.gtb.entrancequiz.domain.Group;
import com.thoughtworks.capability.gtb.entrancequiz.domain.Student;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by wzw on 2020/9/7.
 */
@RestController
public class ServerApi {
    private List<Student> studentList = new ArrayList<>();
    private List<Group> groupList = new ArrayList<>();
    private final String[] names = {"沈乐棋", "徐慧慧", "陈思聪", "王江林", "王登宇",
            "杨思雨", "江雨舟", "廖燊", "胡晓", "但杰", "盖迈达", "肖美琦",
            "黄云洁", "齐瑾浩", "刘亮亮", "肖逸凡", "王作文", "郭瑞凌", "李明豪",
            "党泽", "肖伊佐", "贠晨曦", "李康宁", "马庆", "商婕", "余榕",
            "谌哲", "董翔锐", "陈泰宇", "赵允齐", "张柯", "廖文强", "刘轲",
            "廖浚斌", "凌凤仪"};

    public ServerApi() {
        createOriginList(studentList);
    }

    private void createOriginList(List<Student> studentList) {
        Student student;
        for (int i = 0; i < names.length; i++) {
            student = new Student(i + 1, names[i]);
            studentList.add(student);
        }
    }

    @GetMapping(path = "/getStudents")
    @ResponseStatus(HttpStatus.OK)
    @CrossOrigin
    public List<Student> getStudentList() {
        return studentList;
    }

    @PostMapping(path = "/addStudent/{studentName}")
    @CrossOrigin
    public ResponseEntity addStudent(@PathVariable String studentName) {
        Student student = new Student(studentList.size() + 1, studentName);
        studentList.add(student);
        return ResponseEntity.ok().body(studentList);
    }

    @GetMapping(path = "/getGroups")
    @ResponseStatus(HttpStatus.OK)
    @CrossOrigin
    public List<Group> getGroups() throws ClassNotFoundException, IOException {
        List<Student> newStudentList = deepCopy(studentList);
        randomlySortedList(newStudentList);
        int numberOfLine = studentList.size() / 6;
        int moreInLine = studentList.size() % 6;

        if (groupList.isEmpty()) {
            createNewGroup(newStudentList,moreInLine,numberOfLine);
        } else {
            keepGroupNameGetNewGroup(newStudentList,moreInLine,numberOfLine);
        }
        return groupList;
    }

    private List<Student> deepCopy(List<Student> studentList) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteArrayOutputStream);
        out.writeObject(studentList);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        ObjectInputStream in = new ObjectInputStream(byteArrayInputStream);
        return (List<Student>) in.readObject();
    }

    private void keepGroupNameGetNewGroup(List<Student> newStudentList,int moreInLine,int numberOfLine) {
        int index = 0;
        for (int i = 0; i < moreInLine; i++) {
            List<Student> groupStudents = newStudentList.subList(index, index + numberOfLine + 1);
            groupList.get(i).setMembers(groupStudents);
            index += numberOfLine + 1;
        }
        for (int i = 0; i < 6 - moreInLine; i++) {
            List<Student> groupStudents = newStudentList.subList(index, index + numberOfLine);
            groupList.get(i + moreInLine).setMembers(groupStudents);
            index += numberOfLine;
        }
    }

    private void createNewGroup(List<Student> newStudentList,int moreInLine,int numberOfLine) {
        int index = 0;
        for (int i = 0; i < moreInLine; i++) {
            List<Student> groupStudents = newStudentList.subList(index, index + numberOfLine + 1);
            Group group = new Group(i,"Team " + (i + 1), groupStudents);
            index += numberOfLine + 1;
            groupList.add(group);
        }
        for (int i = 0; i < 6 - moreInLine; i++) {
            List<Student> groupStudents = newStudentList.subList(index, index + numberOfLine);
            Group group = new Group(i + moreInLine,"Team " + (i + moreInLine + 1), groupStudents);
            index += numberOfLine;
            groupList.add(group);
        }
    }

    @PostMapping(path = "/updateTeamName/{groupId}")
    @CrossOrigin
    public ResponseEntity updateTeamName(@PathVariable int groupId,@RequestBody(required = false) String groupName) {
        List<Group> groups = groupList.stream().filter(theGroup -> theGroup.getGroupName().equals(groupName)).collect(Collectors.toList());
        if (groups.isEmpty()) {
            Group group = groupList.get(groupId);
            group.setGroupName(groupName);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    private void randomlySortedList(List<Student> studentList) {
        Collections.shuffle(studentList);
    }
}
