package com.example.patient.controller;

import com.example.patient.dto.VisitDto;
import com.example.patient.service.VisitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/visit")
public class VisitController {
    private final VisitService visitService;

    @PostMapping
    public ResponseEntity<VisitDto.Response> create(@RequestBody VisitDto.Request request) {
        VisitDto.Response responseDto = visitService.create(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(responseDto.getId())
                .toUri();

        return ResponseEntity.created(location)
                .build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<VisitDto.Response> update(@PathVariable Long id, @RequestBody VisitDto.Update updateDto) {
        VisitDto.Update updateObj = VisitDto.Update.builder()
                .id(id)
                .visitStatusCode(updateDto.getVisitStatusCode())
                .build();

        VisitDto.Response responseDto = visitService.update(updateObj);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        visitService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<VisitDto.Response> read(@PathVariable Long id) {
        VisitDto.Response responseDto = visitService.read(id);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping
    public ResponseEntity<List<VisitDto.Response>> list() {
        List<VisitDto.Response> visits = visitService.getVisits();
        return ResponseEntity.ok(visits);
    }
}