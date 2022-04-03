package com.epam.esm.serviceimpl;

import com.epam.esm.Tag;
import com.epam.esm.exception.EntityNotFoundByIdException;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.daoImpl.TagDAOImpl;
import com.epam.esm.service.TagService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    private final TagDAOImpl tagDAO;

    public TagServiceImpl(TagDAOImpl tagDAO) {
        this.tagDAO = tagDAO;
    }

    @Transactional
    public Tag save(Tag tag){
        if (!tagDAO.findByName(tag.getName()).isPresent()){
            tagDAO.save(tag);
        }
        return tagDAO.findByName(tag.getName()).orElseThrow(() -> new  EntityNotFoundException(tag.getName()));

    }


    public Tag findById(Long id) throws EntityNotFoundByIdException {
        return tagDAO.findById(id).orElseThrow(() -> new EntityNotFoundByIdException(id));
    }


    public Tag findByName(String name) {
        return tagDAO.findByName(name).orElseThrow(() -> new  EntityNotFoundException(name));
    }

    public List<Tag> find(){
        return tagDAO.find();
    }


    @Transactional
    public void deleteById(Long id) {
        if(!tagDAO.findById(id).isPresent()) throw new EntityNotFoundByIdException(id);
        tagDAO.delete(id);
    }
}
