package com.finalproject.safepickup.Service;

import com.finalproject.safepickup.Api.ApiException;
import com.finalproject.safepickup.DTOin.ParentDTO;
import com.finalproject.safepickup.DTOout.ParentResponseDTO;
import com.finalproject.safepickup.Model.Parent;
import com.finalproject.safepickup.Model.User;
import com.finalproject.safepickup.Repository.ParentRepository;
import com.finalproject.safepickup.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParentService {

    private final UserRepository userRepository;
    private final ParentRepository parentRepository;

    // 1- get All parent
    public List<Parent> findAll() {
        List<Parent> parents = parentRepository.findAll();
        return parents;
    }

    // 2- Register new parent
    public void registerParent(ParentDTO dto) {

        // 1- adding user attribute
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setEmail(dto.getEmail());
        user.setRole("PARENT");

        // 2- adding parent attribute
        Parent parent = new Parent();
        parent.setNationalId(dto.getNationalId());
        parent.setPhone(dto.getPhone());
         // is accepted is false by default  todo: admin should accept Parent accounts

        // 3- link parent & user
        user.setParent(parent);
        parent.setUser(user);

        // 4- save
        userRepository.save(user);
        parentRepository.save(parent);
    }


    // 3- update a parent
    public void updateParent(Integer parentId, ParentDTO dto) {
        // 1- Find existing parent
        Parent oldParent = parentRepository.findParentById(parentId);
        if(oldParent == null) {
            throw new ApiException("Parent not found");
        }

        // 2- Get the associated user
        User user = oldParent.getUser();
        if(user == null) {
            throw new ApiException("Associated user not found");
        }

        // 3- Update user fields
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword()); // todo: hashing at security
        user.setEmail(dto.getEmail());

        // 4- Update parent fields
        oldParent.setNationalId(dto.getNationalId());
        oldParent.setPhone(dto.getPhone());

        // 5- Save both (cascade should handle this, but being explicit)
        parentRepository.save(oldParent);
        userRepository.save(user);
    }


    // 4- delete a parent
    public void deleteParent(Integer parentId) {
        // 1- Find existing parent
        Parent parent = parentRepository.findParentById(parentId);
        if(parent == null) {
            throw new ApiException("Parent not found");
        }

        if(parent.getUser() == null) {
            throw new ApiException("Associated user not found");
        }
        // 2- delete
        User user = parent.getUser();
        userRepository.delete(user);
    }

    // 5- endpoint will be linked: parent-student-assignment
    public List<ParentResponseDTO> findAllParentsForStudentAssignment() {
        List<Parent> parents = parentRepository.findAll();

        return parents.stream()
                .map(ParentResponseDTO::new)
                .collect(Collectors.toList());
    }

}
