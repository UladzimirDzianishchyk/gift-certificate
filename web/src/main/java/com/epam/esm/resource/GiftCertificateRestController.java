package com.epam.esm.resource;

import com.epam.esm.GiftCertificate;
import com.epam.esm.exception.*;
import com.epam.esm.service.GiftCertificateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "gift-certificates")
public class GiftCertificateRestController {

    private final GiftCertificateService giftCertificateService;

    public GiftCertificateRestController(GiftCertificateService giftCertificateService) {
        this.giftCertificateService = giftCertificateService;
    }



    @GetMapping(path = "{id}")
    public ResponseEntity<?> showById(@PathVariable long id) {


        return new ResponseEntity<>(giftCertificateService.findById(id), HttpStatus.OK);
    }


    @GetMapping
    public ResponseEntity<List<GiftCertificate>> showAll() throws EntityNotFoundException {
        return new ResponseEntity<>(giftCertificateService.find(), HttpStatus.OK);
    }

    @GetMapping(path = "find-by-part-name/{partName}")
    public ResponseEntity<List<GiftCertificate>> getCertByPartName(@PathVariable String partName) {
        return new ResponseEntity<>(giftCertificateService.findCertByPartNameSortByName(partName),HttpStatus.OK);
    }


    @GetMapping(path ="find-by-part-description/{partDescription}")
    public ResponseEntity<List<GiftCertificate>> getCertByPartDescription(@PathVariable String partDescription) {
        return new ResponseEntity<>(giftCertificateService.getCertByPartDescription(partDescription),HttpStatus.OK);
    }


    @DeleteMapping(path = "{id}")
    public void deleteById (@PathVariable long id) {
        giftCertificateService.deleteById(id);
    }

    @PostMapping
    public ResponseEntity<GiftCertificate> save (@RequestBody GiftCertificate giftCertificate){
        return new ResponseEntity<>(giftCertificateService.newCertificate(giftCertificate),HttpStatus.OK);
    }


    @PutMapping
    public ResponseEntity<GiftCertificate> update (@RequestBody GiftCertificate giftCertificate) {
        return new ResponseEntity<>(giftCertificateService.update(giftCertificate),HttpStatus.OK);
    }

    @GetMapping(path = "get-cert-by-tag/{tagName}")
    public ResponseEntity<List<GiftCertificate>> getCertByTagName(@PathVariable String tagName) {
        return new ResponseEntity<>(giftCertificateService.getCertByTagName(tagName),HttpStatus.OK);
    }

    @GetMapping(path = "find-by-part-name-sort-by-create-date/{partName}")
    public ResponseEntity<List<GiftCertificate>> getCertByPartNameSortByCreateDate(@PathVariable String partName) {
        return new ResponseEntity<>(giftCertificateService.findCertByPartNameSortByCreateDate(partName),HttpStatus.OK);
    }

    @GetMapping(path = "find-by-part-name-sort-by-name/{partName}")
    public ResponseEntity<List<GiftCertificate>> getCertByPartNameSortByName(@PathVariable String partName) {
        return new ResponseEntity<>(giftCertificateService.findCertByPartNameSortByName(partName),HttpStatus.OK);
    }


}
