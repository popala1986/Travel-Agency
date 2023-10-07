package com.sda.travelagency.controller;

import com.sda.travelagency.dtos.HotelDto;
import com.sda.travelagency.service.HotelService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hotels")
public class HotelController {
    private final HotelService hotelService;

    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @GetMapping
    public List<HotelDto> getAllHotels() {
        return hotelService.getAllHotels();
    }

    @GetMapping("/{name}")
    public HotelDto getHotel(@PathVariable String name){
        return hotelService.getHotel(name);
    }

    @GetMapping ("/topHotels")
    List<HotelDto> getTopHotels() {
        return hotelService.getTopHotels();
    }

    @GetMapping ("/filterByCity")
    List<HotelDto> getTopHotels(@RequestParam String cityName) {
        return hotelService.getHotelsByCityName(cityName);
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping("/{hotelName}")
    ResponseEntity<String> deleteHotel(@PathVariable String hotelName) {
        hotelService.deleteHotel(hotelName);
        return new ResponseEntity<>("Hotel deleted", HttpStatus.ACCEPTED);
    }

    @Secured("ROLE_ADMIN")
    @PutMapping("/{hotelName}")
    ResponseEntity<String> updateHotel(@PathVariable String hotelName,@Valid @RequestBody HotelDto hotelDto) {
        hotelService.updateHotel(hotelName, hotelDto);
        return new ResponseEntity<>("Hotel updated", HttpStatus.ACCEPTED);
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/addHotel")
    ResponseEntity<String> addHotel(@Valid @RequestBody HotelDto hotelDto) {
        hotelService.addHotel(hotelDto);
        return new ResponseEntity<>("Hotel created", HttpStatus.CREATED);
    }


}
