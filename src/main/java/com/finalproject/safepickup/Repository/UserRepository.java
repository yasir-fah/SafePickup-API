package com.finalproject.safepickup.Repository;

import com.finalproject.safepickup.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer>
{

}
