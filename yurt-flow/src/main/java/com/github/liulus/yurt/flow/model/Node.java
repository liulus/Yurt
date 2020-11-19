package com.github.liulus.yurt.flow.model;

import lombok.Data;

import java.util.List;

/**
 * @author liulu
 * @version V1.0
 * @since 2020/10/29 10:27
 */

@Data
public class Node {
    private String type;
    private String color;
    private String shape;
    private Integer offsetX;
    private Integer offsetY;
    private Double x;
    private Double y;
    private List<List<Integer>> inPoints;
    private List<List<Integer>> outPoints;

}
