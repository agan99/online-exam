package com.agan.exam.controller.rest;

import com.agan.exam.model.Type;
import com.agan.exam.server.TypeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/type")
public class TypeController {

    @Resource
    private TypeService typeService;

    @GetMapping
    public List<Type> list() {
        return this.typeService.list();
    }
}
