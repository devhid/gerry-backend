package edu.stonybrook.cse308.gerrybackend.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DefaultController {

    @RequestMapping(path="/", method=RequestMethod.GET)
    public String index() {
        return "200\n";
    }

}
