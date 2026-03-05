package com.finalproject.safepickup.Controller;

import com.finalproject.safepickup.Api.ApiResponse;
import com.finalproject.safepickup.DTOin.ParentDTO;
import com.finalproject.safepickup.DTOout.ParentResponseDTO;
import com.finalproject.safepickup.Service.ParentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/parent")
@RequiredArgsConstructor
public class ParentController {

    private final ParentService parentService;

    @GetMapping("/get/parents")
    public ResponseEntity<?> getAllParent() {
        return ResponseEntity.status(200).body(parentService.findAll());
    }

    @PostMapping("/register")
    public ResponseEntity<?> parentRegister(@Valid @RequestBody ParentDTO parentDTO) {
        parentService.registerParent(parentDTO);
        return ResponseEntity.status(200).body(new ApiResponse("you successfully registered !"));
    }

    @PutMapping("/update/parent/{parentId}")
    public ResponseEntity<?> updateParent(@PathVariable Integer parentId,
                                       @RequestBody @Valid ParentDTO dto) {
        parentService.updateParent(parentId, dto);
        return ResponseEntity.status(200).body(new ApiResponse("Parent updated successfully"));
    }

    @DeleteMapping("/delete/parent/{parentId}")
    public ResponseEntity<?> deleteParent(@PathVariable Integer parentId) {
        parentService.deleteParent(parentId);
        return ResponseEntity.status(200).body(new ApiResponse("Parent deleted successfully"));
    }

    @GetMapping("/student/assignment")
    public ResponseEntity<?> findAllParentsForStudentAssignment() {
        return ResponseEntity.status(200).body(parentService.findAllParentsForStudentAssignment());
    }


}
