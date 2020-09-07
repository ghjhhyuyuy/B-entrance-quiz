package com.thoughtworks.capability.gtb.entrancequiz.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by wzw on 2020/9/7.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Group {
    private String groupName;
    private List<Student> members;
}
