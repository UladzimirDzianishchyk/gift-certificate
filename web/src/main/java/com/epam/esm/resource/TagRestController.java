package com.epam.esm.resource;

import com.epam.esm.Tag;
import com.epam.esm.exception.EntityNotFoundByIdException;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.service.TagService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "tags")
public class TagRestController {

    private final TagService tagService;

    public TagRestController(TagService tagService) {
        this.tagService = tagService;
    }


    @GetMapping(path = "{id}")
    public ResponseEntity<Tag> findById(@PathVariable long id) throws EntityNotFoundByIdException {
        return new ResponseEntity<>(tagService.findById(id), HttpStatus.OK);
    }

    
    @GetMapping(path = "find-by-name/{name}")
    public ResponseEntity<Tag> findByName(@PathVariable String name) throws EntityNotFoundException {
        return new ResponseEntity<>(tagService.findByName(name),HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Tag>> showAll(){
        return new ResponseEntity<>(tagService.find(),HttpStatus.OK);
    }

    @DeleteMapping(path = "{id}")
    public void deleteById(@PathVariable Long id) throws EntityNotFoundByIdException {
        tagService.deleteById(id);
    }

    @PostMapping
    public ResponseEntity<Tag> save(@RequestBody Tag tag){
        return new ResponseEntity<>(tagService.save(tag),HttpStatus.OK);
    }
}
